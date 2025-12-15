-- 목적:
-- 사용자별 포인트 거래 내역 조회 성능 개선
-- (WHERE user_id = ? AND deleted_at IS NULL ORDER BY id DESC)

-- 대상 쿼리:
-- PointTransactionRepository.findByUserId

CREATE INDEX idx_pt_user_deleted_id
    ON point_transactions (user_id, deleted_at, id DESC);