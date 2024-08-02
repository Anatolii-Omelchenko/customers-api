CREATE TABLE customers
(
    id        BIGSERIAL,
    created   BIGINT               NOT NULL,
    updated   BIGINT               NOT NULL,
    full_name VARCHAR(255)         NOT NULL,
    email     VARCHAR(255) UNIQUE  NOT NULL,
    phone     VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    PRIMARY KEY (id)
);

ALTER SEQUENCE customers_id_seq RESTART WITH 100;
