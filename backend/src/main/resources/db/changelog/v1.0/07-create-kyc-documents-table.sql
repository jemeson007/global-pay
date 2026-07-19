-- Create kyc_documents table
CREATE TABLE IF NOT EXISTS kyc_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    document_type document_type NOT NULL,
    document_number VARCHAR(255) NOT NULL,
    issue_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    issuing_country VARCHAR(2) NOT NULL,
    file_url VARCHAR(512) NOT NULL,
    file_hash VARCHAR(255),
    verification_status verification_status NOT NULL DEFAULT 'PENDING',
    verification_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP,
    verified_by VARCHAR(255)
);

CREATE INDEX idx_user_id_kyc ON kyc_documents(user_id);
CREATE INDEX idx_status_kyc ON kyc_documents(verification_status);
