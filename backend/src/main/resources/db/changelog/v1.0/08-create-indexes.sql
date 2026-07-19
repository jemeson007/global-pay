-- Create additional indexes for performance
CREATE INDEX idx_transaction_sender_created ON transactions(sender_id, created_at DESC);
CREATE INDEX idx_transaction_receiver_created ON transactions(receiver_id, created_at DESC);
CREATE INDEX idx_transaction_status_created ON transactions(status, created_at DESC);
CREATE INDEX idx_payment_methods_user_default ON payment_methods(user_id, is_default);
CREATE INDEX idx_recipients_user_active ON recipients(user_id, is_active);

-- Create partitioning for transactions table (optional, for very large tables)
-- ALTER TABLE transactions PARTITION BY RANGE (EXTRACT(YEAR FROM created_at));
