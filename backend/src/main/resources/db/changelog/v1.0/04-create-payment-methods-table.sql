-- Create payment_methods table
CREATE TABLE IF NOT EXISTS payment_methods (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type payment_method_type NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    bank_name VARCHAR(255),
    account_holder_name VARCHAR(255),
    account_number VARCHAR(255),
    routing_number VARCHAR(255),
    iban VARCHAR(255),
    card_last_four_digits VARCHAR(4),
    card_brand VARCHAR(50),
    card_holder_name VARCHAR(255),
    expiry_date VARCHAR(255),
    mobile_number VARCHAR(20),
    mobile_provider VARCHAR(50),
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP
);

CREATE INDEX idx_user_id_pm ON payment_methods(user_id);
CREATE INDEX idx_type_pm ON payment_methods(type);
