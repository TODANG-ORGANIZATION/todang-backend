package com.jichijima.todang.repository.restaurant;

import com.jichijima.todang.model.entity.restaurant.Restaurant;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 가게 목록 조회 시 동적 필터링을 적용하는 Specification 클래스
 */
public class RestaurantSpecification {
    public static Specification<Restaurant> withFilters(
            Float left, Float right, Float top, Float bottom,
            Boolean isOpen, String address, String category, String restaurant, Long userId, Boolean like
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 위경도 범위 필터링
            if (left != null && right != null && top != null && bottom != null) {
                predicates.add(cb.between(root.get("lon"), left, right));
                predicates.add(cb.between(root.get("lat"), bottom, top));
            }

            // 2. 찜한 가게 필터링 (like == true일 때 userId 필수)
            if (Boolean.TRUE.equals(like)) {
                if (userId != null) {
                    predicates.add(root.join("restaurantLikes").get("user").get("id").in(userId));
                } else {
                    throw new IllegalArgumentException("찜한 가게 조회 시 user_id가 필요합니다.");
                }
            }

            // 3. 주소 필터링 (LIKE 검색)
            if (address != null && !address.isEmpty()) {
                predicates.add(cb.like(root.get("address"), "%" + address + "%"));
            }

            // 4. 카테고리 필터링
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            // 5. 특정 가게 검색 (부분 검색 가능)
            if (restaurant != null && !restaurant.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + restaurant + "%"));
            }

            // 6. 영업 여부 필터링
            if (isOpen != null) {
                predicates.add(cb.equal(root.get("isOpen"), isOpen));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
