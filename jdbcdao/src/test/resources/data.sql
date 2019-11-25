insert into AUTHORS (id, `name`) values (1, 'Pushkin');

insert into GENRES(id, `name`) values (1, 'Sci-Fi');

insert into BOOKS(id, `name`, publication_date) values (1, 'Onegin', '2016-08-01');

insert into BOOK_AUTHOR_REL(book_id, author_id) values (1, 1);