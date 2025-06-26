CREATE TABLE author (
    id          uuid         NOT NULL PRIMARY KEY,
    name        varchar(100) NOT NULL,
    birthdate   date         NOT NULL,
    citizenship varchar(50)  NOT NULL,
    created_at  timestamp,
    updated_at  timestamp,
    id_user     uuid
);

CREATE TABLE book (
    id           uuid         NOT NULL PRIMARY KEY,
    isbn         varchar(20)  NOT NULL UNIQUE,
    title        varchar(150) NOT NULL,
    release_date date         NOT NULL,
    genres       varchar(30)  NOT NULL,
    price        numeric(18, 4),
    created_at   timestamp,
    updated_at   timestamp,
    id_user      uuid,
    id_author    uuid         NOT NULL REFERENCES author (id),
    CONSTRAINT check_genre CHECK ( genres IN ('FICTION', 'FANTASY', 'MYSTERY', 'ROMANCE', 'BIBLIOGRAPHY', 'SCIENCE') )
);