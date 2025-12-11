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
: "${JAVA_OPTS:-'-Xms512m -Xmx1024m'}"

# ===== ECR 경로 파싱 =====
REG_URI="$(echo "${FULL_URI}" | cut -d/ -f1)"
REPO_AND_TAG="$(echo "${FULL_URI}" | cut -d/ -f2- )"
REPO="${REPO_AND_TAG%:*}"
TAG="${REPO_AND_TAG##*:}"

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
  "docker network create ${REDIS_HOST} || true"
  "docker ps -a --filter name=${REDIS_HOST} --format '{{.Names}}' | grep -q ${REDIS_HOST} || docker run -d --name ${REDIS_HOST} --restart=always --network ${REDIS_HOST} -p ${REDIS_PORT}:${REDIS_PORT} redis:7-alpine"
  "docker stop ${CONTAINER_NAME} || true"
  "docker rm   ${CONTAINER_NAME} || true"
  "docker run -d --name ${CONTAINER_NAME} --restart=always --network golf-rental-network -p ${APP_PORT}:${APP_PORT} -e SPRING_PROFILES_ACTIVE=${SPRING_PROFILE} -e AWS_REGION=${AWS_REGION} -e REDIS_HOST=${REDIS_HOST} -e REDIS_PORT=${REDIS_PORT} -e JAVA_OPTS=\"${JAVA_OPTS}\" ${FULL_URI}"
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