CREATE SCHEMA IF NOT EXISTS data;

CREATE TABLE IF NOT EXISTS data.user_data
(
    user_id  BIGINT PRIMARY KEY NOT NULL,
    language VARCHAR(20)        NOT NULL
);

CREATE TABLE IF NOT EXISTS data.embed
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    data TEXT                              NOT NULL
);

CREATE TABLE IF NOT EXISTS data.punishment
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id  BIGINT                            NOT NULL,
    from_id  BIGINT                            NOT NULL,
    reason   VARCHAR(50)                       NOT NULL,
    `system` varchar(20)                       NOT NULL,
    time     VARCHAR(30)                       NOT NULL,
    embed_id BIGINT                            NOT NULL,
    FOREIGN KEY (embed_id) REFERENCES data.embed (id)
);