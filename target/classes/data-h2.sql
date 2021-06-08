--Initial data for database 'hangman' 

DROP TABLE IF EXISTS hangman;

DROP SEQUENCE IF EXISTS hangman_id_seq;

CREATE TABLE hangman (key_word_id int primary key, key_word varchar(255) not null, displayed_letters varchar(25), game_counter number, all_chosen_letters varchar(255), precise_chosen_letters varchar(255));

CREATE SEQUENCE hangman_id_seq START WITH 1 INCREMENT BY 1;

INSERT INTO hangman(key_word_id, key_word, displayed_letters, game_counter, all_chosen_letters, precise_chosen_letters) VALUES (hangman_id_seq.nextval, 'hangman', null, 0, null, null), (hangman_id_seq.nextval, 'avocado' , null, 0, null, null), (hangman_id_seq.nextval, 'peperoni' , null, 0, null, null); 