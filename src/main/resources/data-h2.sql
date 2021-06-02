--Initial data for database 'hangman' 

DROP TABLE IF EXISTS hangman;

DROP SEQUENCE IF EXISTS hangman_id_seq;

CREATE TABLE hangman (key_word_id int primary key, key_word varchar(255) not null);

CREATE SEQUENCE hangman_id_seq START WITH 1 INCREMENT BY 1;

INSERT INTO hangman(key_word_id, key_word) VALUES (hangman_id_seq.nextval, 'hangman'), (hangman_id_seq.nextval, 'avocado'), (hangman_id_seq.nextval, 'peperoni'); 