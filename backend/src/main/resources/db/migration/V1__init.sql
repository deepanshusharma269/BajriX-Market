/* Flyway migration V1 - create tables */
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    category VARCHAR(100)
);

CREATE TABLE sellers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    contact_info TEXT
);

CREATE TABLE seller_listings (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    seller_id INTEGER NOT NULL REFERENCES sellers(id) ON DELETE CASCADE,
    price NUMERIC(12,2) NOT NULL CHECK (price > 0),
    stock INTEGER NOT NULL CHECK (stock >= 0),
    min_order_qty INTEGER NOT NULL CHECK (min_order_qty > 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    UNIQUE (product_id, seller_id)
);
