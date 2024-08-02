CREATE TABLE customers
(
    id        BIGINT PRIMARY KEY,
    created   BIGINT               NOT NULL,
    updated   BIGINT               NOT NULL,
    full_name VARCHAR(255)         NOT NULL,
    email     VARCHAR(255) UNIQUE  NOT NULL,
    phone     VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE NOT NULL
);
