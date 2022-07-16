CREATE TABLE users
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    username     VARCHAR(255)          NULL,
    password     VARCHAR(255)          NULL,
    email        VARCHAR(255)          NULL,
    created_date datetime              NULL,
    is_enabled   BIT(1)                NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE subreddits
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NULL,
    `description` VARCHAR(255)          NULL,
    created_date  datetime              NULL,
    CONSTRAINT pk_subreddits PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    title         VARCHAR(255)          NULL,
    url           VARCHAR(255)          NULL,
    `description` LONGTEXT              NULL,
    voice_count   INT                   NULL,
    user_id       BIGINT                NULL,
    created_date  datetime              NULL,
    subreddit_id  BIGINT                NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_SUBREDDIT FOREIGN KEY (subreddit_id) REFERENCES subreddits (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE comments
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    text         VARCHAR(255)          NULL,
    post_id      BIGINT                NULL,
    created_date datetime              NULL,
    user_id      BIGINT                NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE votes
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    vote_type INT                   NULL,
    post_id   BIGINT                NOT NULL,
    user_id   BIGINT                NULL,
    CONSTRAINT pk_votes PRIMARY KEY (id)
);

ALTER TABLE votes
    ADD CONSTRAINT FK_VOTES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE votes
    ADD CONSTRAINT FK_VOTES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE verification_tokens
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    token           VARCHAR(255)          NULL,
    user_id         BIGINT                NULL,
    expiration_date datetime              NULL,
    CONSTRAINT pk_verification_tokens PRIMARY KEY (id)
);

ALTER TABLE verification_tokens
    ADD CONSTRAINT FK_VERIFICATION_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);