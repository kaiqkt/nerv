CREATE TABLE projects
(
    id          VARCHAR(26)  PRIMARY KEY,
    name        VARCHAR(25)  NOT NULL,
    slug        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL,
    CONSTRAINT projects_name_unique UNIQUE (name)
);