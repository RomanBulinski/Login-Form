--Database reset
DROP DATABASE exerciseLogin;
DROP USER loginn;

--User creation
CREATE USER loginn WITH ENCRYPTED PASSWORD 'loginn';

--Database creation
CREATE DATABASE exerciseLogin WITH OWNER = loginn;
GRANT CONNECT ON DATABASE exerciseLogin TO loginn;
--GRANT USAGE ON SCHEMA public to admin;
--GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin;
--GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin;

--Connection to new created database

\c exerciseLogin loginn localhost



CREATE TABLE
IF NOT EXISTS id_data (
	id SERIAL PRIMARY KEY,
	name TEXT,
	password TEXT,
);

--Default data insertion
INSERT INTO id_data
  ( id,name, password)
  VALUES
    (1,'Rom','xxx'),
    (2,'Tom','xxx'),
    (3,'Dom','xxx');

