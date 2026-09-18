CREATE TYPE user_role AS ENUM (
    'ADMIN',
    'CLIENT'
);

CREATE TYPE room_type AS ENUM (
    'SINGLE',
    'DOUBLE',
    'SUITE'
);

-- ...

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       salt VARCHAR(255) NOT NULL,
                       role user_role NOT NULL DEFAULT 'CLIENT',
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- other tables...