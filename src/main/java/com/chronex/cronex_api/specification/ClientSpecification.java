package com.chronex.cronex_api.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.chronex.cronex_api.dto.client.ClientFilter;
import com.chronex.cronex_api.entity.Client;

import jakarta.persistence.criteria.Predicate;

public class ClientSpecification {
    public static Specification<Client> withFilters(UUID organizationId, ClientFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("organization").get("id"), organizationId));

            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.name().toLowerCase() + "%"
                        )
                );
            }

            if (filter.cpfCnpj() != null && !filter.cpfCnpj().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("cpfCnpj")),
                                "%" + filter.cpfCnpj().toLowerCase() + "%"
                        )
                );
            }

            if (filter.company() != null && !filter.company().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("company")),
                                "%" + filter.company().toLowerCase() + "%"
                        )
                );
            }

            if (filter.email() != null && !filter.email().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + filter.email().toLowerCase() + "%"
                        )
                );
            }

            if (filter.phone() != null && !filter.phone().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("phone")),
                                "%" + filter.phone().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
