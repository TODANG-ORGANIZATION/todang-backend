package com.jichijima.todang.controller.review;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.dto.review.ReviewRequest;
import com.jichijima.todang.model.dto.review.ReviewSearchCriteria;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.review.Review;
import com.jichijima.todang.service.menu.MenuService;
import com.jichijima.todang.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 목록 조회
    @GetMapping("")
    public ResponseEntity<List<Review>> getReviews(
            @RequestParam(value = "restaurant_id", required = false) Long restaurantId,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "photo_only", required = false, defaultValue = "false") Boolean photoOnly,
            @RequestParam(value = "sort", required = false, defaultValue = "LATEST") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction
    ) {
        List<Review> reviews = reviewService.getReviews(restaurantId, userId, photoOnly, sort, direction);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId){
        Optional<Review> review = reviewService.getReviewById(reviewId);
        if (review.isPresent())
            return ResponseEntity.ok(review.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 리뷰 등록
    @PostMapping("")
    public ResponseEntity<?> insertReview(@RequestBody ReviewRequest reviewRequest){
        boolean isCreated = reviewService.createReview(reviewRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 리뷰 일부 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> patchReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest){
        boolean isUpdated = reviewService.patchReview(reviewId, reviewRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴를 찾지 못했습니다.");
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId){
        boolean isDeleted = reviewService.deleteReview(reviewId);
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 메뉴를 찾지 못했습니다.");
    }

}
