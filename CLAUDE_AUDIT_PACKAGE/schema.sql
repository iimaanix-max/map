-- ============================================================================
-- DIGITAL TASBIH - CLOUDFLARE D1 SQL SCHEMA (Rev 7.0 Final)
-- Database Name: tasbih-auth-db
-- ============================================================================

DROP TABLE IF EXISTS otp_records;
DROP TABLE IF EXISTS rate_limits;
DROP TABLE IF EXISTS user_submit_cooldowns;

CREATE TABLE otp_records (
    email TEXT PRIMARY KEY,
    uid TEXT NOT NULL,
    otp_hmac TEXT NOT NULL,
    attempts INTEGER DEFAULT 0,
    created_at INTEGER NOT NULL,
    expires_at INTEGER NOT NULL
);

CREATE TABLE rate_limits (
    key TEXT PRIMARY KEY,
    last_requested_at INTEGER NOT NULL,
    request_count INTEGER DEFAULT 1,
    day_bucket TEXT NOT NULL
);

CREATE TABLE user_submit_cooldowns (
    uid TEXT PRIMARY KEY,
    last_submitted_at INTEGER NOT NULL,
    last_amount INTEGER NOT NULL
);

CREATE INDEX idx_otp_expires ON otp_records(expires_at);
CREATE INDEX idx_rl_last_req ON rate_limits(last_requested_at);

CREATE TABLE IF NOT EXISTS analytics_metrics (
    metric_key TEXT PRIMARY KEY,
    category TEXT NOT NULL,
    item_name TEXT NOT NULL,
    day_bucket TEXT NOT NULL,
    count INTEGER DEFAULT 1,
    last_updated_at INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_analytics_bucket ON analytics_metrics(day_bucket, category);
