-- Create exchange_rates table
CREATE TABLE IF NOT EXISTS exchange_rates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    rate NUMERIC(19,6) NOT NULL,
    mid_market_rate NUMERIC(19,6) NOT NULL,
    margin NUMERIC(5,2),
    our_rate NUMERIC(19,6) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rate_timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_currency_pair ON exchange_rates(from_currency, to_currency);
CREATE INDEX idx_updated_at_er ON exchange_rates(updated_at);
