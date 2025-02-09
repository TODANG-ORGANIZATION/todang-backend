package com.jichijima.todang.service.user;

import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.user.UserRepository;
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

    // 카카오 OAuth2 설정값
    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String kakaoClientSecret;

    /**
     * 네이버 OAuth2 로그인
     */
    public Map<String, String> loginWithNaver(String code) {
        String accessToken = getNaverAccessToken(code);
        Map<String, Object> userInfo = getNaverUserInfo(accessToken);
        return processOAuthLogin(userInfo, "naver");
    }

    /**
     * 카카오 OAuth2 로그인
     */
    public Map<String, String> loginWithKakao(String code) {
        String accessToken = getKakaoAccessToken(code);
        Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
        return processOAuthLogin(userInfo, "kakao");
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
     * 카카오 Access Token 요청
     */
    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&client_secret=" + kakaoClientSecret
                + "&redirect_uri=http://localhost:8080/login/oauth2/code/kakao"
                + "&code=" + code;

        try {
            System.out.println("🔹 카카오 Access Token 응답: dasdasdsad");

            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, new HttpEntity<>(requestBody, headers), Map.class);

            System.out.println("🔹 카카오 Access Token 응답: " + response.getBody());

            if (response.getBody() == null || response.getBody().get("access_token") == null) {
                throw new RuntimeException("❌ 카카오 Access Token을 가져오지 못했습니다.");
            }

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            System.out.println("❌ 카카오 Access Token 요청 실패: " + e.getMessage());
            throw new RuntimeException("❌ 카카오 Access Token 요청 실패: " + e.getMessage());
        }
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
     * 카카오 사용자 정보 요청
     */
    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            System.out.println("🔹 카카오 사용자 정보 응답: " + response.getBody());

            if (response.getBody() == null) {
                throw new RuntimeException("❌ 카카오 사용자 정보를 가져오지 못했습니다.");
            }

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("❌ 카카오 사용자 정보 요청 실패: " + e.getMessage());
        }
    }


    /**
     * OAuth2 로그인 후 회원가입 및 JWT 발급
     */
    private Map<String, String> processOAuthLogin(Map<String, Object> userInfo, String provider) {
        Map<String, Object> response = (provider.equals("naver"))
                ? (Map<String, Object>) userInfo.get("response")
                : (Map<String, Object>) userInfo.get("kakao_account");

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, provider.toUpperCase() + " 사용자 정보 조회 실패");
        }

        String email = (String) response.get("email");
        String nickname = (provider.equals("naver"))
                ? (String) response.get("nickname")
                : (String) ((Map<String, Object>) response.get("profile")).get("nickname");

        if (email == null) {
            System.out.println("❌ 카카오 OAuth2 응답에서 email을 찾을 수 없습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 계정에 이메일이 없습니다. 카카오 계정 설정에서 이메일 제공을 활성화하세요.");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .nickname(nickname != null ? nickname : provider.toUpperCase() + "_" + email.split("@")[0])
                                .role(User.Role.CUSTOMER)
                                .build()
                ));

        return Map.of("token", jwtUtil.generateToken(email));
    }

}