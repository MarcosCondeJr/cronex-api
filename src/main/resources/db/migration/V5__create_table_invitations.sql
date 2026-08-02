CREATE TABLE invitations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID NOT NULL,
    email VARCHAR(150) NOT NULL,
    role VARCHAR(20) NOT NULL,
    invited_by UUID NOT NULL,
    token UUID NOT NULL DEFAULT gen_random_uuid(),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_invitations_token UNIQUE (token),

    CONSTRAINT fk_invitations_user FOREIGN KEY (invited_by) REFERENCES users(id),
    CONSTRAINT fk_invitations_organization FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE INDEX idx_invitations_org_email_status ON invitations (organization_id, email, status);