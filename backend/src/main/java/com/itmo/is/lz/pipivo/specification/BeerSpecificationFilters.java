package com.itmo.is.lz.pipivo.specification;

import com.itmo.is.lz.pipivo.model.Beer;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class BeerSpecificationFilters {

    public static Specification<Beer> filterBeers(
            Double priceMin, Double priceMax,
            Double ratingMin, Double ratingMax,
            String name, Long srmMin, Long srmMax,
            Long ibuMin, Long ibuMax,
            Long abvMin, Long abvMax,
            String country, String fermentationType) {

        return new Specification<Beer>() {
            @Override
            public Predicate toPredicate(Root<Beer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();

                if (priceMin != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), priceMin));
                }
                if (priceMax != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), priceMax));
                }

                if (ratingMin != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("averageRating"), ratingMin));
                }
                if (ratingMax != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("averageRating"), ratingMax));
                }

                if (srmMin != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("srm"), srmMin));
                }
                if (srmMax != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("srm"), srmMax));
                }

                if (ibuMin != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("ibu"), ibuMin));
                }
                if (ibuMax != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("ibu"), ibuMax));
                }

                if (abvMin != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("abv"), abvMin));
                }
                if (abvMax != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("abv"), abvMax));
                }

                if (country != null && !country.isEmpty()) {
                    predicate = cb.and(predicate, cb.equal(cb.lower(root.get("country")), country.toLowerCase()));
                }

                if (fermentationType != null && !fermentationType.isEmpty()) {
                    predicate = cb.and(predicate, cb.equal(cb.lower(root.get("fermentationType").get("name")), fermentationType.toLowerCase()));
                }

                if (name != null && !name.isEmpty()) {
                    predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
                }

                return predicate;
            }
        };
    }
}
