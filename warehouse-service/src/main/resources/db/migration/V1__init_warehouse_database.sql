CREATE TYPE location AS ENUM('WARSAW', 'TOKYO', 'FRANKFURT');

CREATE TABLE inventory
(
    inventory_id UUID           NOT NULL PRIMARY KEY,
    quantity     NUMERIC(12, 0) NOT NULL DEFAULT 0,
    location     LOCATION       NOT NULL,
    updated_at   TIMESTAMP
);

CREATE TABLE listing
(
    listing_id  UUID      NOT NULL PRIMARY KEY,
    seller_id   UUID,
    active      BOOLEAN   NOT NULL DEFAULT TRUE,
    total_price DECIMAL(10, 2),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE product_category
(
    product_category_id UUID NOT NULL PRIMARY KEY,
    category_type       TEXT NOT NULL,
    name                TEXT NOT NULL,
    description         TEXT
);

CREATE TABLE product
(
    product_id          UUID      NOT NULL PRIMARY KEY,
    product_name        TEXT      NOT NULL,
    description         TEXT,
    price               DECIMAL(10, 2),
    sku                 TEXT      NOT NULL,
    image_url           TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP,
    listing_id          UUID UNIQUE REFERENCES listing (listing_id),
    product_category_id UUID      NOT NULL REFERENCES product_category (product_category_id)
);

CREATE TABLE product_inventory
(
    product_id   UUID NOT NULL REFERENCES product (product_id),
    inventory_id UUID NOT NULL REFERENCES inventory (inventory_id),
    PRIMARY KEY (product_id, inventory_id)
);
