package com.jichijima.todang.service.review;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.dto.review.ReviewRequest;
import com.jichijima.todang.model.dto.review.ReviewSearchCriteria;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.review.Review;
import com.jichijima.todang.repository.menu.MenuRepository;
import com.jichijima.todang.repository.review.ReviewRepository;
import com.jichijima.todang.repository.review.ReviewSpecification;
import com.jichijima.todang.service.menu.MenuOptionCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 필터 통해 리뷰 목록 조회
    public List<Review> getReviews(Long restaurantId, Long userId, Boolean photoOnly, String sort, String direction) {
        Specification<Review> specification = ReviewSpecification.buildSpecification(
                restaurantId, userId, photoOnly, sort, direction);

        return reviewRepository.findAll(specification);
    }


    // 특정 리뷰 조회 (ID 기반)
    public Optional<Review> getReviewById(Long reviewId){
        return reviewRepository.findById(reviewId);
    }

    // 리뷰 추가
    public boolean createReview(ReviewRequest reviewRequest){
        Review review = new Review();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (reviewRequest.getOrderId() != null) review.setOrderId(reviewRequest.getOrderId());
        if (reviewRequest.getPhoto() != null) review.setPhoto(reviewRequest.getPhoto());
        if (reviewRequest.getReview() != null) review.setReview(reviewRequest.getReview());
        if (reviewRequest.getReply() != null) review.setReply(reviewRequest.getReply());
        if (reviewRequest.getStar() != null) review.setStar(reviewRequest.getStar());
        if (reviewRequest.getCleanness() != null) review.setCleanness(reviewRequest.getCleanness());
        if (reviewRequest.getWrittenDatetime() != null) review.setWrittenDatetime(reviewRequest.getWrittenDatetime());

        // 리뷰를 저장하고 결과 확인
        Review savedReview = reviewRepository.save(review);
        return savedReview != null;
    }

    // 리뷰 정보 부분 수정 (PATCH)
    public boolean patchReview(Long reviewId, ReviewRequest reviewRequest){
        return reviewRepository.findById(reviewId)
                .map(review -> {
                    boolean isUpdated = false;
                    if (reviewRequest.getPhoto() != null){
                        review.setPhoto(reviewRequest.getPhoto());
                        isUpdated = true;
                    }
                    if (reviewRequest.getReview() != null){
                        review.setReview(reviewRequest.getReview());
                        isUpdated = true;
                    }
                    if (reviewRequest.getReply() != null){
                        review.setReply(reviewRequest.getReply());
                        isUpdated = true;
                    }
                    if (reviewRequest.getStar() != null){
                        review.setStar(reviewRequest.getStar());
                        isUpdated = true;
                    }
                    if (reviewRequest.getCleanness() != null){
                        review.setCleanness(reviewRequest.getCleanness());
                        isUpdated = true;
                    }

                    if (isUpdated){
                        reviewRepository.save(review);
                        return true;
                    }else{
                        return false;
                    }
                })
                .orElse(false);
    }

    // 리뷰 삭제
    public boolean deleteReview(Long reviewId){
        // 리뷰가 존재하면 삭제
        if (reviewRepository.existsById(reviewId)){
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }
}
