/* Flyway migration V2 - sample data */
INSERT INTO sellers (name, status, contact_info) VALUES
    ('Shree Traders', 'APPROVED', 'shree@example.com'),
    ('Bajri Supplies', 'PENDING', 'bajri@example.com'),
    ('Construction Hub', 'APPROVED', 'hub@example.com');

INSERT INTO products (name, description, category) VALUES
    ('UltraTech PPC Cement 50 kg', 'Portland Pozzolana Cement, 50 kg bag', 'Cement'),
    ('Red Brick 250 mm', 'Standard red clay brick', 'Masonry'),
    ('Steel Rod 12 mm', 'High tensile steel rod', 'Steel');

-- Assuming ids: sellers 1,2,3; products 1,2,3
INSERT INTO seller_listings (product_id, seller_id, price, stock, min_order_qty, is_active, version) VALUES
    (1, 1, 390.00, 500, 1, true, 0),
    (1, 3, 385.00, 300, 5, true, 0),
    (1, 2, 405.00, 200, 1, true, 0),
    (2, 1, 12.00, 1000, 10, true, 0),
    (3, 3, 55.00, 150, 2, true, 0);
