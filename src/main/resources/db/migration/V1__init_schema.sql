CREATE TABLE IF NOT EXISTS drug_record_application_detail
(
    application_number VARCHAR(255) NOT NULL PRIMARY KEY,
    manufacturer_name VARCHAR(255),
    substance_name VARCHAR(255),
    product_numbers VARCHAR(255)
)