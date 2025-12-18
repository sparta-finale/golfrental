#!/usr/bin/env bash
set -euo pipefail

# ===== 필수 환경변수 점검 =====
: "${AWS_REGION:?AWS_REGION required}"
: "${EC2_INSTANCE_ID:?EC2_INSTANCE_ID required}"
: "${FULL_URI:?FULL_URI required}"
: "${CONTAINER_NAME:?CONTAINER_NAME required}"
: "${APP_PORT:?APP_PORT required}"
: "${SPRING_PROFILE:?SPRING_PROFILE required}"
: "${REDIS_HOST:?REDIS_HOST required}"
: "${REDIS_PORT:?REDIS_PORT required}"
: "${MONITORING_EC2_PRIVATE_IP:?MONITORING_EC2_PRIVATE_IP required}"

# ===== ECR 경로 파싱 =====
REG_URI="$(echo "${FULL_URI}" | cut -d/ -f1)"
REPO_AND_TAG="$(echo "${FULL_URI}" | cut -d/ -f2- )"
REPO="${REPO_AND_TAG%:*}"
TAG="${REPO_AND_TAG##*:}"

# ===== promtail config =====
PROMTAIL_CONFIG_CONTENT=$(cat <<EOF
server:
  http_listen_port: 9080
  grpc_listen_port: 0

positions:
  filename: /tmp/positions.yaml

clients:
  - url: http://${MONITORING_EC2_PRIVATE_IP}:3100/loki/api/v1/push

scrape_configs:
- job_name: golfrental-dev-logs
  static_configs:
  - targets:
      - localhost
    labels:
      job: "golfrental-dev"
      environment: "dev"
      server: "dev-api"
      application: "golf-rental"
      __path__: /app-logs/*.log
EOF
)

# SSM 코멘트(100자 제한 방어)
COMMENT="Deploy ${REPO}:${TAG}"
if [ ${#COMMENT} -gt 100 ]; then
  COMMENT="${COMMENT:0:100}"
fi

echo "[INFO] FULL_URI=${FULL_URI}"
echo "[INFO] REG_URI=${REG_URI}"
echo "[INFO] EC2_INSTANCE_ID=${EC2_INSTANCE_ID}"
echo "[INFO] COMMENT=${COMMENT}"

# ===== EC2에서 실행할 커맨드(배열로 안전하게 정의) =====
CMDS=(
  "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${REG_URI}"
  "docker pull ${FULL_URI}"
  "docker network create golf-rental-network || true"
  "docker ps -a --filter name=golf-rental-redis --format '{{.Names}}' | grep -q golf-rental-redis || docker run -d --name golf-rental-redis --restart=always --network golf-rental-network -p 6379:6379 redis/redis-stack-server:latest"
  "sudo mkdir -p /app-logs && sudo chmod 777 /app-logs"
  "docker stop ${CONTAINER_NAME} || true"
  "docker rm   ${CONTAINER_NAME} || true"
  "docker run -d --name ${CONTAINER_NAME} --restart=always --network golf-rental-network -p ${APP_PORT}:${APP_PORT} -v /app-logs:/app-logs -e SPRING_PROFILES_ACTIVE=${SPRING_PROFILE} -e AWS_REGION=${AWS_REGION} -e REDIS_HOST=${REDIS_HOST} -e REDIS_PORT=${REDIS_PORT} -e JAVA_OPTS='-Xms512m -Xmx1024m' ${FULL_URI}"
  "echo '${PROMTAIL_CONFIG_CONTENT}' | sudo tee /etc/promtail-config.yml > /dev/null"
  "docker stop promtail || true"
  "docker rm promtail || true"
  "docker run -d --name promtail --restart=unless-stopped --user root -v /etc/promtail-config.yml:/etc/promtail/config.yml:ro -v /app-logs:/app-logs:ro -v /tmp:/tmp grafana/promtail:3.5.8 -config.file=/etc/promtail/config.yml"
)

# Bash 배열 → JSON 배열 변환 (jq 필수)
COMMANDS_JSON=$(jq -Rn --argjson arr "$(printf '%s\n' "${CMDS[@]}" | jq -R . | jq -s .)" '$arr')
echo "[DEBUG] COMMANDS_JSON=${COMMANDS_JSON}"

# ===== SSM 명령 전송 =====
RESP=$(aws ssm send-command \
  --document-name "AWS-RunShellScript" \
  --comment "${COMMENT}" \
  --targets "Key=instanceIds,Values=${EC2_INSTANCE_ID}" \
  --parameters "{\"commands\": ${COMMANDS_JSON}}" \
  --region "${AWS_REGION}" \
  --output json)

CMD_ID=$(echo "${RESP}" | jq -r '.Command.CommandId')
echo "[INFO] SSM CommandId: ${CMD_ID}"

# ===== 완료 대기/성공 판정 =====
for i in {1..30}; do
  STATUS=$(aws ssm get-command-invocation \
    --command-id "${CMD_ID}" \
    --instance-id "${EC2_INSTANCE_ID}" \
    --query 'Status' \
    --output text \
    --region "${AWS_REGION}") || echo "FailedToFetchStatus"
  echo "[INFO] SSM Status: ${STATUS}"
  case "${STATUS}" in
    Success) exit 0 ;;
    Failed|Cancelled|TimedOut) echo "[ERROR] SSM failed: ${STATUS}"; exit 1 ;;
    FailedToFetchStatus) echo "[ERROR] Failed to fetch SSM command status."; exit 1 ;;
  esac

  sleep 5
done

echo "[ERROR] SSM command did not complete in time"
exit 1