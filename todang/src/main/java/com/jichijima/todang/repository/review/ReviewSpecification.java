package com.jichijima.todang.repository.review;

import com.jichijima.todang.model.dto.review.ReviewSearchCriteria;
import com.jichijima.todang.model.entity.review.Review;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification {

    public static Specification<Review> filterByRestaurantId(Long restaurantId) {
        return (root, query, criteriaBuilder) -> {
            if (restaurantId != null) {
                // "order"라는 관계가 아닌 "orders"라는 필드명이 정확해야 하므로 정확히 Join을 맞춰줘야 함
                Join<Review, Order> orderJoin = root.join("order", JoinType.INNER);
                return criteriaBuilder.equal(orderJoin.get("restaurantId"), restaurantId);
            }
            return criteriaBuilder.conjunction(); // No condition if null
        };
    }

    public static Specification<Review> filterByUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId != null) {
                Join<Review, Order> orderJoin = root.join("order", JoinType.INNER);
                return criteriaBuilder.equal(orderJoin.get("userId"), userId);
            }
            return criteriaBuilder.conjunction(); // No condition if null
        };
    }

    public static Specification<Review> filterByPhotoOnly(Boolean photoOnly) {
        return (root, query, criteriaBuilder) -> {
            if (photoOnly != null && photoOnly) {
                return criteriaBuilder.isNotNull(root.get("photo"));
            }
            return criteriaBuilder.conjunction(); // No condition if false or null
        };
    }

    public static Specification<Review> sortBy(String sort, String direction) {
        return (root, query, criteriaBuilder) -> {
            if (sort == null || direction == null) {
                return null; // No sorting if sort or direction is null
            }

            Order order = null;

            // 정렬 기준에 맞춰 정렬 처리를 해주기
            if ("LATEST".equalsIgnoreCase(sort)) {
                order = "ASC".equalsIgnoreCase(direction) ? criteriaBuilder.asc(root.get("writtenDatetime"))
                        : criteriaBuilder.desc(root.get("writtenDatetime"));
            } else if ("RATING".equalsIgnoreCase(sort)) {
                order = "ASC".equalsIgnoreCase(direction) ? criteriaBuilder.asc(root.get("star"))
                        : criteriaBuilder.desc(root.get("star"));
            } else if ("CLEAN".equalsIgnoreCase(sort)) {
                order = "ASC".equalsIgnoreCase(direction) ? criteriaBuilder.asc(root.get("cleanness"))
                        : criteriaBuilder.desc(root.get("cleanness"));
            }

            if (order != null) {
                // 반환된 정렬을 query에 추가하는 방법
                query.orderBy(order);
            }

            return null; // No condition but apply the sorting
        };
    }

    public static Specification<Review> buildSpecification(Long restaurantId, Long userId, Boolean photoOnly, String sort, String direction) {
        Specification<Review> specification = Specification.where(null);

        if (restaurantId != null) {
            specification = specification.and(filterByRestaurantId(restaurantId));
        }

        if (userId != null) {
            specification = specification.and(filterByUserId(userId));
        }

        if (photoOnly != null) {
            specification = specification.and(filterByPhotoOnly(photoOnly));
        }

        if (sort != null && direction != null) {
            specification = specification.and(sortBy(sort, direction));
        }

        return specification;
    }
}