CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR not null,
    login    VARCHAR not null,
    password VARCHAR not null
);