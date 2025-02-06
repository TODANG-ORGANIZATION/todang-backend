package com.jichijima.todang.service.restaurant;

import com.jichijima.todang.model.dto.restaurant.RestaurantDetailResponse;
import com.jichijima.todang.model.dto.restaurant.RestaurantPatchRequest;
import com.jichijima.todang.model.dto.restaurant.RestaurantResponse;
import com.jichijima.todang.model.dto.restaurant.RestaurantUpdateRequest;
import com.jichijima.todang.model.entity.restaurant.Restaurant;
import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.restaurant.RestaurantLikeRepository;
import com.jichijima.todang.repository.restaurant.RestaurantRepository;
import com.jichijima.todang.repository.user.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantLikeRepository restaurantLikeRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantLikeRepository restaurantLikeRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantLikeRepository = restaurantLikeRepository;
        this.userRepository = userRepository;
    }

    // 가게 목록 조회 (필터 적용)
    public List<RestaurantResponse> getRestaurants(Specification<Restaurant> spec) {
        return restaurantRepository.findAll(spec).stream()
                .map(r -> new RestaurantResponse(
                        r.getId(), r.getName(), r.isOpen(), r.isLive(), r.getAddress(),
                        r.getCategory(), r.getStarAvg(), r.getCleannessAvg(), r.getReviewCnt(), r.getPhoto()))
                .collect(Collectors.toList());
    }

    // 가게 상세 조회
    public RestaurantDetailResponse getRestaurantDetail(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));
        return RestaurantDetailResponse.fromEntity(restaurant);
    }

    // 찜한 가게 목록 조회
    public List<RestaurantResponse> getLikedRestaurants(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return restaurantLikeRepository.findByUser(user).stream()
                .map(like -> new RestaurantResponse(
                        like.getRestaurant().getId(),
                        like.getRestaurant().getName(),
                        like.getRestaurant().isOpen(),
                        like.getRestaurant().isLive(),
                        like.getRestaurant().getAddress(),
                        like.getRestaurant().getCategory(),
                        like.getRestaurant().getStarAvg(),
                        like.getRestaurant().getCleannessAvg(),
                        like.getRestaurant().getReviewCnt(),
                        like.getRestaurant().getPhoto()
                ))
                .collect(Collectors.toList());
    }

    // 가게 전체 수정 (PUT)
    public void updateRestaurant(Long restaurantId, RestaurantUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));
        restaurant.setName(request.getName());
        restaurant.setStartDate(request.getStartDate());
        restaurant.setDescription(request.getDescription());
        restaurant.setLat(request.getLat());
        restaurant.setLon(request.getLon());
        restaurant.setBusinessHours(request.getBusinessHours());
        restaurant.setClosedDays(request.getClosedDays());
        restaurant.setOriginCountry(request.getOriginCountry());
        restaurant.setTel(request.getTel());
        restaurant.setPhoto(request.getPhoto());
        restaurant.setThumbnail(request.getThumbnail());
        restaurantRepository.save(restaurant);
    }

    // 가게 부분 수정 (PATCH)
    public void patchRestaurant(Long restaurantId, RestaurantPatchRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));
        if (request.getName() != null) restaurant.setName(request.getName());
        if (request.getStartDate() != null) restaurant.setStartDate(request.getStartDate());
        if (request.getDescription() != null) restaurant.setDescription(request.getDescription());
        if (request.getLat() != null) restaurant.setLat(request.getLat());
        if (request.getLon() != null) restaurant.setLon(request.getLon());
        if (request.getBusinessHours() != null) restaurant.setBusinessHours(request.getBusinessHours());
        if (request.getClosedDays() != null) restaurant.setClosedDays(request.getClosedDays());
        if (request.getOriginCountry() != null) restaurant.setOriginCountry(request.getOriginCountry());
        if (request.getTel() != null) restaurant.setTel(request.getTel());
        if (request.getPhoto() != null) restaurant.setPhoto(request.getPhoto());
        if (request.getThumbnail() != null) restaurant.setThumbnail(request.getThumbnail());
        restaurantRepository.save(restaurant);
    }
}
