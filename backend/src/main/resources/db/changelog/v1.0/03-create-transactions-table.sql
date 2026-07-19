-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_ref VARCHAR(255) NOT NULL UNIQUE,
    sender_id UUID NOT NULL REFERENCES users(id),
    receiver_id UUID NOT NULL REFERENCES users(id),
    send_amount NUMERIC(19,2) NOT NULL,
    send_currency VARCHAR(3) NOT NULL,
    receive_amount NUMERIC(19,2) NOT NULL,
    receive_currency VARCHAR(3) NOT NULL,
    exchange_rate NUMERIC(19,4) NOT NULL,
    fee NUMERIC(19,2) NOT NULL,
    discount NUMERIC(19,2),
    status transaction_status NOT NULL DEFAULT 'PENDING',
    type transaction_type NOT NULL,
    description TEXT,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    failure_reason VARCHAR(255),
    payment_method_id UUID REFERENCES payment_methods(id)
);

CREATE INDEX idx_transaction_ref ON transactions(transaction_ref);
CREATE INDEX idx_sender_id ON transactions(sender_id);
CREATE INDEX idx_receiver_id ON transactions(receiver_id);
CREATE INDEX idx_status ON transactions(status);
CREATE INDEX idx_created_at ON transactions(created_at);
