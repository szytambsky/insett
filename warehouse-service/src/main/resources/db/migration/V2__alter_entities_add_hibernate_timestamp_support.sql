CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

ALTER TABLE inventory
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT NOW();

CREATE TRIGGER inventory_update_timestamp_trg
    BEFORE UPDATE
    ON inventory
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

ALTER TABLE listing
    ALTER COLUMN created_at SET DEFAULT NOW();
ALTER TABLE listing
    ALTER COLUMN created_at SET NOT NULL;

CREATE TRIGGER listing_update_timestamp_trg
    BEFORE UPDATE
    ON listing
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

ALTER TABLE product_category
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT NOW();
ALTER TABLE product_category
    ADD COLUMN updated_at TIMESTAMP;

CREATE TRIGGER product_category_update_timestamp_trg
    BEFORE UPDATE
    ON product_category
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

ALTER TABLE product
    ALTER COLUMN created_at SET DEFAULT NOW();
ALTER TABLE product
    ALTER COLUMN created_at SET NOT NULL;

CREATE TRIGGER product_update_timestamp_trg
    BEFORE UPDATE
    ON product
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();