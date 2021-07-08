CREATE TABLE IF NOT EXISTS product (
    product_id text,
    currency_code text,
    value decimal,
    PRIMARY KEY((product_id), currency_code)
);

INSERT INTO product (product_id, currency_code, value) VALUES ('51509273', 'USD', 10.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('54154777', 'USD', 20.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('13860428', 'USD', 22.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('13860428', 'INR', 1098.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('51446183', 'USD', 30.99);