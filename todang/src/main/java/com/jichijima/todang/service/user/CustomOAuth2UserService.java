package com.jichijima.todang.service.user;

import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.user.UserRepository;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
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
            Map<String, Object> attributes = oAuth2User.getAttributes();

            if ("naver".equals(provider)) {
                return processNaverUser(attributes);
            } else if ("kakao".equals(provider)) {
                return processKakaoUser(attributes);
            } else {
                throw new RuntimeException("❌ 지원되지 않는 OAuth2 제공자입니다: " + provider);
            }
        } catch (Exception e) {
            System.err.println("OAuth2 로그인 중 에러 발생: " + e.getMessage());
            throw new RuntimeException("OAuth2 로그인 중 에러 발생", e);
        }
    }

    private OAuth2User processNaverUser(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            throw new RuntimeException("❌ 네이버 OAuth2 Response가 없습니다.");
        }

        String email = (String) response.get("email");
        String name = (String) response.get("name");  // ✅ name 추가
        String nickname = (String) response.get("nickname");
        String profileImage = (String) response.get("profile_image");
        String mobile = (String) response.get("mobile");

        User user = saveOrUpdateUser(email, name, nickname, profileImage, mobile, "naver");

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                response,
                "email"
        );
    }


    private OAuth2User processKakaoUser(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            throw new RuntimeException("❌ 카카오 OAuth2 응답에서 'kakao_account'를 찾을 수 없습니다.");
        }

        String email = (String) kakaoAccount.get("email");
        String phoneNumber = kakaoAccount.containsKey("phone_number") ? (String) kakaoAccount.get("phone_number") : null;
        if (phoneNumber == null) {
            throw new RuntimeException("❌ 카카오 OAuth2 응답에서 'phone_number'를 찾을 수 없습니다. 전화번호는 필수 값입니다.");
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (profile != null) ? (String) profile.get("nickname") : "Unknown";
        String profileImage = (profile != null) ? (String) profile.get("profile_image_url") : null;
        String name = (String) kakaoAccount.get("name");

        if (email == null) {
            throw new RuntimeException("❌ 카카오 OAuth2 응답에서 'email'을 찾을 수 없습니다.");
        }
        if (nickname == null) {
            nickname = "User";
        }

        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        modifiedAttributes.put("email", email);  // email을 최상위에 포함

        User user = saveOrUpdateUser(email, name, nickname, profileImage, phoneNumber, "kakao");

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                modifiedAttributes,
                "email"
        );
    }



    private User saveOrUpdateUser(String email, String name, String nickname, String profileImage, String mobile, String provider) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        return existingUser.map(entity -> entity.update(nickname, profileImage, mobile))
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name != null ? name : provider.toUpperCase() + "_USER") // ✅ name 추가
                            .nickname(nickname != null ? nickname : provider.toUpperCase() + "_" + email.split("@")[0])
                            .photo(profileImage)
                            .tel(mobile)
                            .role(User.Role.CUSTOMER)
                            .build();
                    return userRepository.save(newUser);
                });
    }

}