CREATE TABLE author (
    id          UUID         NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    birthdate   DATE         NOT NULL,
    citizenship VARCHAR(50)  NOT NULL
);

CREATE TABLE book (
    id           UUID         NOT NULL PRIMARY KEY,
    isbn         VARCHAR(20)  NOT NULL UNIQUE,
    title        VARCHAR(150) NOT NULL,
    release_date DATE         NOT NULL,
    genres       VARCHAR(30)  NOT NULL,
    price        NUMERIC(18, 4),
    id_author    UUID         NOT NULL REFERENCES author (id),
    CONSTRAINT check_genre CHECK ( genres IN ('FICTION', 'FANTASY', 'MYSTERY', 'ROMANCE', 'BIBLIOGRAPHY', 'SCIENCE') )
);