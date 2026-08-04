CREATE TABLE project_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL,
    hourly_rate DECIMAL(10,2),
    joined_at TIMESTAMP NOT NULL DEFAULT now(),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,
    CONSTRAINT uq_project_members_project_user UNIQUE (project_id, user_id),

    CONSTRAINT fk_project_members_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_project_members_project FOREIGN KEY (project_id) REFERENCES projects(id)
);