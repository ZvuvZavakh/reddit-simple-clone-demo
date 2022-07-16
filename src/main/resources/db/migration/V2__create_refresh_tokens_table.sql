CREATE TABLE refresh_tokens
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    token        VARCHAR(255)          NULL,
    created_date datetime              NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);