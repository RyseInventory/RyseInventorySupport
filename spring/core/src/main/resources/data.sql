/*
 * Copyright (c)  2023 Rysefoxx
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

CREATE SCHEMA IF NOT EXISTS data;

CREATE TABLE IF NOT EXISTS data.tag_request
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    tag_entity TEXT                              NOT NULL
);

CREATE TABLE IF NOT EXISTS data.tag
(
    name    VARCHAR(20) PRIMARY KEY NOT NULL,
    content TEXT                    NOT NULL,
    created VARCHAR(30)             NOT NULL,
    from_id BIGINT                  NOT NULL
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