package com.jichijima.todang.controller.like;

import com.jichijima.todang.model.dto.like.LikeRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 찜 하기
    @PostMapping("")
    public ResponseEntity<?> insertLike(@RequestBody LikeRequest likeRequest){
        boolean isCreated = likeService.createLike(likeRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 찜 해제하기
    @DeleteMapping("/user/{userId}/restaurant/{restaurantId}")
    public ResponseEntity<?> deleteLike(@PathVariable Long userId, @PathVariable Long restaurantId){
        boolean isDeleted = likeService.deleteLike(userId, restaurantId);
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 찜을 찾지 못했습니다.");
    }

}
