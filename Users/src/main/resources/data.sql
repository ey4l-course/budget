CREATE TABLE users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
email VARCHAR UNIQUE,
hashed_email VARCHAR,
uuid VARCHAR UNIQUE,
password VARCHAR,
role VARCHAR
);

INSERT INTO users (email, password, role) VALUES ('admin@app.local', 'password', 'admin');
INSERT INTO users (email, password, role) VALUES ('user@app.local', 'password', 'user')