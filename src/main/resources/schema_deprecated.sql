CREATE TABLE book(
id BIGINT AUTO_INCREMENT,
title VARCHAR(255) NOT NULL,
author_id LONG NOT NULL,
`year` INT NOT NULL,
genre VARCHAR(255) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (author_id) REFERENCES author(id)
);
CREATE TABLE author(
id LONG AUTO_INCREMENT,
name VARCHAR(55) NOT NULL,
birth_year INT
PRIMARY KEY (id)
);
--INSERT INTO book(title, author_id, `year`, genre) VALUES('Hello', 1, 2025, 'FANTASY');