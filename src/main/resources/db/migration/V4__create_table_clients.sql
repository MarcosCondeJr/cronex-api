CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    user_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    cpf_cnpj VARCHAR(14) NOT NULL UNIQUE,
    company VARCHAR(150) NULL,
    email VARCHAR(100) NULL,
    phone VARCHAR(15) NULL,
    notes TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,

    CONSTRAINT fk_client_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_client_organization FOREIGN KEY (organization_id) REFERENCES organizations(id)
);