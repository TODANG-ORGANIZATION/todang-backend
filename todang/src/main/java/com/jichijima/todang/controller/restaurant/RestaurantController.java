package com.jichijima.todang.controller.restaurant;

import com.jichijima.todang.model.dto.restaurant.RestaurantDetailResponse;
import com.jichijima.todang.model.dto.restaurant.RestaurantPatchRequest;
import com.jichijima.todang.model.dto.restaurant.RestaurantResponse;
import com.jichijima.todang.model.dto.restaurant.RestaurantUpdateRequest;
import com.jichijima.todang.model.entity.restaurant.Restaurant;
import com.jichijima.todang.repository.restaurant.RestaurantSpecification;
import com.jichijima.todang.service.restaurant.RestaurantService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // 가게 목록 조회 (필터링 포함)
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurants(
            @RequestParam(name = "left",required = false) Float left,
            @RequestParam(name = "right",required = false) Float right,
            @RequestParam(name = "top",required = false) Float top,
            @RequestParam(name = "bottom",required = false) Float bottom,
            @RequestParam(name = "isOpen",required = false) Boolean isOpen,
            @RequestParam(name = "address",required = false) String address,
            @RequestParam(name = "category",required = false) String category,
            @RequestParam(name = "restaurant",required = false) String restaurant,
            @RequestParam(name = "userId",required = false) Long userId,
            @RequestParam(name = "like",required = false) Boolean like
    ) {
        Specification<Restaurant> spec = RestaurantSpecification.withFilters(left, right, top, bottom, isOpen, address, category, restaurant, userId, like);
        return ResponseEntity.ok(restaurantService.getRestaurants(spec));
    }

    // 가게 상세 조회
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponse> getRestaurantDetail(@PathVariable ("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getRestaurantDetail(restaurantId));
    }

    // 찜한 가게 목록 조회
    @GetMapping("/users/{userId}/liked-restaurants")
    public ResponseEntity<List<RestaurantResponse>> getLikedRestaurants(@PathVariable ("userId") Long userId) {
        return ResponseEntity.ok(restaurantService.getLikedRestaurants(userId));
    }

    // 가게 전체 수정 (PUT)
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable ("restaurantId") Long restaurantId, @Validated @RequestBody RestaurantUpdateRequest request) {
        restaurantService.updateRestaurant(restaurantId, request);
        return ResponseEntity.ok().build();
    }

    // 가게 부분 수정 (PATCH)
    @PatchMapping("/{restaurantId}")
    public ResponseEntity<Void> patchRestaurant(@PathVariable ("restaurantId") Long restaurantId, @RequestBody RestaurantPatchRequest request) {
        restaurantService.patchRestaurant(restaurantId, request);
        return ResponseEntity.ok().build();
    }
}
