CREATE TABLE users
(
    id         VARCHAR(26)  PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    nickname   VARCHAR(25)  NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    CONSTRAINT unique_nickname UNIQUE (nickname),
    CONSTRAINT unique_email    UNIQUE (email)
);

CREATE TABLE projects
(
    id          VARCHAR(26)  PRIMARY KEY,
    user_id     VARCHAR(26)  NOT NULL,
    name        VARCHAR(25)  NOT NULL,
    slug        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL,
    CONSTRAINT projects_name_unique UNIQUE (name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);