package com.itmo.is.lz.pipivo.specification;

import com.itmo.is.lz.pipivo.model.Review;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewsSpecification {
    public static Specification<Review> withFilters(Map<String, String> filters, Long beerId, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (beerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("beer").get("id"), beerId));
            }

            if (userId != null) {
                Join<Object, Object> userJoin = root.join("users");
                predicates.add(criteriaBuilder.equal(userJoin.get("id"), userId));
            }

            filters.forEach((key, value) -> {
                switch (key) {
                    case "rating": {
                        Float ratingValue = Float.parseFloat(value);
                        predicates.add(criteriaBuilder.equal(root.get("rating"), ratingValue));
                        break;
                    }
                    case "created_at": {
                        predicates.add(criteriaBuilder.equal(root.get("created_at"), value));
                        break;
                    }
                    default:
                        break;
                }
            });

            // Применение сортировки
            String sortBy = filters.getOrDefault("sortBy", "created_at");
            String order = filters.getOrDefault("order", "desc");

            switch (order.toLowerCase()) {
                case "asc":
                    query.orderBy(criteriaBuilder.asc(root.get(sortBy)));
                    break;
                case "desc":
                default:
                    query.orderBy(criteriaBuilder.desc(root.get(sortBy)));
                    break;
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
