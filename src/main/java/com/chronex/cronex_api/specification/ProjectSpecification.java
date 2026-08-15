package com.chronex.cronex_api.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.chronex.cronex_api.dto.project.ProjectFilter;
import com.chronex.cronex_api.entity.Project;

import jakarta.persistence.criteria.Predicate;

public class ProjectSpecification {
    public static Specification<Project> withFilters(UUID organizationId, ProjectFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("organizationId"), organizationId));

            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.name().toLowerCase() + "%"
                        )
                );
            }

            if (filter.description() != null && !filter.description().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("description")),
                                "%" + filter.description().toLowerCase() + "%"
                        )
                );
            }

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.deadline() != null) {
                predicates.add(cb.equal(root.get("deadline"), filter.deadline()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
