CREATE TABLE users
(
    id          VARCHAR(26)  PRIMARY KEY,
    created_at  TIMESTAMP    NOT NULL
);

CREATE TABLE projects
(
    id          VARCHAR(26)  PRIMARY KEY,
    name        VARCHAR(25)  NOT NULL,
    slug        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL,
    CONSTRAINT projects_name_unique UNIQUE (name)
);

CREATE TABLE git_device_auth
(
    id             VARCHAR(26)  PRIMARY KEY,
    device_code    VARCHAR(255) NOT NULL,
    status         VARCHAR(16)  NOT NULL ,
    created_at     TIMESTAMP    NOT NULL,
    expires_at     TIMESTAMP    NOT NULL,
    poll_at        TIMESTAMP    NOT NULL,
    last_posted_at TIMESTAMP    NOT NULL
);

CREATE TABLE git_access_token
(
    id           VARCHAR(26)  PRIMARY KEY,
    access_token VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP    NOT NULL
);