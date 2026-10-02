CREATE TABLE customers (
      id BIGSERIAL PRIMARY KEY,
      customer_full_name VARCHAR(255) NOT NULL,
      customer_address VARCHAR(500),
      customer_phone VARCHAR(50)
);

ALTER TABLE orders ADD COLUMN customer_id BIGINT REFERENCES customers(id);
ALTER TABLE orders DROP COLUMN customer_full_name;
ALTER TABLE orders DROP COLUMN customer_address;
ALTER TABLE orders DROP COLUMN customer_phone;

ALTER TABLE order_items ADD COLUMN product_id BIGINT REFERENCES products(id);
ALTER TABLE order_items DROP COLUMN product_name;
ALTER TABLE order_items DROP COLUMN product_price;