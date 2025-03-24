CREATE TABLE categories (
   id       SERIAL PRIMARY KEY,
   title    TEXT UNIQUE NOT NULL
);