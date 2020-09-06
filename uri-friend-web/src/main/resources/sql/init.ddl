CREATE TABLE scheme (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    scheme varchar(10) NOT NULL,
    UNIQUE(scheme)
);

CREATE TABLE authority (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    authority varchar(100) NOT NULL,
    UNIQUE(authority)
);

CREATE TABLE relative_path (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    path text NOT NULL,
    UNIQUE(path)
);