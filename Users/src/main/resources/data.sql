CREATE TABLE users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
email VARCHAR UNIQUE,
hashed_email VARCHAR,
uuid VARCHAR UNIQUE,
password VARCHAR,
role VARCHAR
);

INSERT INTO users (email, hashed_email, password, role) VALUES ('admin@app.local', 'hjk', 'password', 'admin');
INSERT INTO users (email, hashed_email, uuid, password, role) VALUES ('admin@app.com', '$2a$10$UUFXCjcoy2Jn2N768tYVC./h3gt.dRhPV3VF7.LTuDfVRFb4G5IBm',	'admin17',	'$2a$10$9ZonQlVi7feXSvW7Oi4Yr.Cdd8UE5D0MJZEsoOOlRBU2hmKsZ3cLm',	'user');
