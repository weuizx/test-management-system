CREATE TABLE IF NOT EXISTS public.client (
    id bigint PRIMARY KEY,
    username varchar(32) NOT NULL UNIQUE,
    password varchar(128) NOT NULL,
    role varchar(32),
    name varchar(32),
    surname varchar(64),
    patronymic varchar(32)
);

CREATE SEQUENCE client_id_seq START WITH 1 INCREMENT BY 1;
