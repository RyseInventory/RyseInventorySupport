CREATE SCHEMA IF NOT EXISTS data;

CREATE TABLE IF NOT EXISTS data.user_data
(
    user_id  BIGINT PRIMARY KEY NOT NULL,
    language VARCHAR(2)          NOT NULL
);