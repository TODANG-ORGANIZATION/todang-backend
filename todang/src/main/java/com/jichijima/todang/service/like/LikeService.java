package com.jichijima.todang.service.like;

import com.jichijima.todang.model.dto.like.LikeRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.like.Like;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.repository.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    // 찜 하기
    public boolean createLike(LikeRequest likeRequest){
        Like like = new Like();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (likeRequest.getUserId() != null) like.setUserId(likeRequest.getUserId());
        if (likeRequest.getRestaurantId() != null) like.setRestaurantId(likeRequest.getRestaurantId());

        // 찜을 저장하고 결과 확인
        Like savedLike = likeRepository.save(like);
        return savedLike != null;
    }

    // 찜 삭제
    public boolean deleteLike(Long userId, Long restaurantId){
        Optional<Like> like = likeRepository.findByUserIdAndRestaurantId(userId, restaurantId);
        if (like.isPresent()){
            likeRepository.delete(like.get());
            return true;
        }else
            return false;
    }
}
