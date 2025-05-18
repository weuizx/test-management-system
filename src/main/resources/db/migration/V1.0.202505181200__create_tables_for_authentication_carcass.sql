CREATE SEQUENCE client_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS public.client (
    id bigint PRIMARY KEY DEFAULT nextval('client_id_seq'),
    username varchar(32) NOT NULL UNIQUE,
    password varchar(128) NOT NULL,
    name varchar(32),
    surname varchar(64),
    patronymic varchar(32)
);

CREATE SEQUENCE client_authority_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS public.client_authority (
    id bigint PRIMARY KEY DEFAULT nextval('client_authority_id_seq'),
    client_id bigint NOT NULL REFERENCES client(id),
    role varchar(32)
);

CREATE TABLE IF NOT EXISTS public.deactivated_token (
    id uuid PRIMARY KEY,
    keep_until timestamp with time zone NOT NULL
);