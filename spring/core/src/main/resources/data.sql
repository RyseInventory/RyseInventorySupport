CREATE SCHEMA IF NOT EXISTS data;

CREATE TABLE IF NOT EXISTS data.tag
(
    name    VARCHAR(20) PRIMARY KEY NOT NULL,
    content TEXT                    NOT NULL,
    created VARCHAR(30)             NOT NULL,
    from_id BIGINT                  NOT NULL
);

CREATE TABLE IF NOT EXISTS data.tag_request
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    tag_entity TEXT                              NOT NULL
);

CREATE TABLE IF NOT EXISTS data.tag_alias
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    tag_name VARCHAR(20)                       NOT NULL,
    alias    TEXT                              NOT NULL,
    FOREIGN KEY (tag_name) REFERENCES data.tag (name)
);

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