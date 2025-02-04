package com.jichijima.todang.service.user;

import com.jichijima.todang.model.entity.User;
import com.jichijima.todang.repository.UserRepository;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    // 환경변수에서 네이버 API 설정 값 가져오기
    @Value("${NAVER_CLIENT_ID}")
    private String naverClientId;

    @Value("${NAVER_CLIENT_SECRET}")
    private String naverClientSecret;

    /**
     * 네이버 OAuth 2.0 로그인 처리
     */
    public Map<String, String> loginWithNaver(String code) {
        String accessToken = getNaverAccessToken(code);
        Map<String, Object> userInfo = getNaverUserInfo(accessToken);
        return processOAuthLogin(userInfo);
    }

    /**
     * 네이버 Access Token 요청
     */
    private String getNaverAccessToken(String code) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=authorization_code"
                + "&client_id=" + naverClientId
                + "&client_secret=" + naverClientSecret
                + "&code=" + code;

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl, HttpMethod.POST, new HttpEntity<>(requestBody, headers), Map.class);

        return (String) response.getBody().get("access_token");
    }

    /**
     * 네이버 사용자 정보 요청
     */
    private Map<String, Object> getNaverUserInfo(String accessToken) {
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        return response.getBody();
    }

    /**
     * 네이버 로그인 후 회원가입 및 JWT 발급
     */
    private Map<String, String> processOAuthLogin(Map<String, Object> userInfo) {
        Map<String, Object> response = (Map<String, Object>) userInfo.get("response");

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "네이버 사용자 정보 조회 실패");
        }

        String email = (String) response.get("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .nickname("NAVER_" + email.split("@")[0])
                                .role(User.Role.CUSTOMER)
                                .build()
                ));

        return Map.of("token", jwtUtil.generateToken(email));
    }
}