package com.itmo.is.lz.pipivo.specification;

import com.itmo.is.lz.pipivo.model.Beer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeerSpecification {
    public static Specification<Beer> withFilters(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        predicates.add(criteriaBuilder.like(root.get("name"), "%" + value + "%"));
                        break;
                    case "price":
                        predicates.add(criteriaBuilder.equal(root.get("price"), Double.valueOf(value)));
                        break;
                    case "volume":
                        predicates.add(criteriaBuilder.equal(root.get("volume"), Double.valueOf(value)));
                        break;
                    case "fermentationType":
                        predicates.add(criteriaBuilder.equal(root.get("fermentationType"), value));
                        break;
                    case "srm":
                        predicates.add(criteriaBuilder.equal(root.get("srm"), Long.valueOf(value)));
                        break;
                    case "ibu":
                        predicates.add(criteriaBuilder.equal(root.get("ibu"), Long.valueOf(value)));
                        break;
                    case "abv":
                        predicates.add(criteriaBuilder.equal(root.get("abv"), Long.valueOf(value)));
                        break;
                    case "og":
                        predicates.add(criteriaBuilder.equal(root.get("og"), Long.valueOf(value)));
                        break;
                    case "country":
                        predicates.add(criteriaBuilder.like(root.get("country"), "%" + value + "%"));
                        break;
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
