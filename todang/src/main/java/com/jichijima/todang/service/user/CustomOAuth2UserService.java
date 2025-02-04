package com.jichijima.todang.service.user;

import com.jichijima.todang.model.entity.User;
import com.jichijima.todang.repository.UserRepository;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            System.out.println("OAuth2 User Attributes: " + oAuth2User.getAttributes());

            String provider = userRequest.getClientRegistration().getRegistrationId();
            if (!"naver".equals(provider)) {
                throw new RuntimeException("❌ 지원되지 않는 OAuth2 제공자입니다: " + provider);
            }

            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response == null) {
                System.out.println("❌ 네이버 OAuth2 Response가 없습니다. attributes: " + attributes);
                throw new RuntimeException("❌ 네이버 OAuth2 Response가 없습니다.");
            }

            String email = (String) response.get("email");
            String nickname = (String) response.get("nickname");
            String name = (String) response.get("name");
            String profileImage = (String) response.get("profile_image");
            String mobile = (String) response.get("mobile");

            System.out.println("🔍 네이버 로그인 사용자 이메일: " + email);
            System.out.println("🔍 네이버 로그인 사용자 닉네임: " + nickname);
            System.out.println("🔍 네이버 로그인 사용자 이름: " + name);
            System.out.println("🔍 네이버 로그인 사용자 프로필 사진: " + profileImage);
            System.out.println("🔍 네이버 로그인 사용자 휴대전화번호: " + mobile);

            Optional<User> existingUser = userRepository.findByEmail(email);
            User user = existingUser.map(entity -> entity.update(nickname, profileImage, mobile))
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .name(name)
                                .email(email)
                                .nickname(nickname != null ? nickname : "NAVER_" + email.split("@")[0])
                                .userPhoto(profileImage)
                                .tel(mobile)
                                .role(User.Role.CUSTOMER)
                                .build();
                        return userRepository.save(newUser);
                    });

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                    attributes,
                    "email"
            );

        } catch (Exception e) {
            System.err.println("OAuth2 로그인 중 에러 발생: " + e.getMessage());
            throw new RuntimeException("OAuth2 로그인 중 에러 발생", e);
        }
    }
}
