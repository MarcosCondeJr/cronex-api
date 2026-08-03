CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID NOT NULL,
    client_id UUID NOT NULL,
    owner_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNING',
    deadline DATE,
    hourly_rate DECIMAL(10,2),
    estimated_hours DECIMAL(10,2),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,

    CONSTRAINT fk_project_user FOREIGN KEY (owner_id) REFERENCES users(id),
    CONSTRAINT fk_project_organization FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_project_client FOREIGN KEY (client_id) REFERENCES clients(id)
);