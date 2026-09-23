package com.chronex.cronex_api.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.chronex.cronex_api.dto.projectMember.ProjectMemberFilter;
import com.chronex.cronex_api.entity.ProjectMember;

import jakarta.persistence.criteria.Predicate;

public class ProjectMemberSpecification {
    public static Specification<ProjectMember> withFilters(UUID projectId, UUID organizationId, ProjectMemberFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("project").get("id"), projectId));

            predicates.add(cb.equal(root.get("project").get("organizationId"), organizationId));

            if (filter.role() != null) {

                predicates.add(cb.equal(root.get("role"), filter.role()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
