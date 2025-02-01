package com.jichijima.todang.user;

import com.jichijima.todang.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserEntityTest {

    @Test
    @DisplayName("유저 엔티티 객체 생성 테스트")
    void User_엔티티_생성_테스트() {
        //Given
        User user = User.builder()
                .name("김싸피")
                .email("test@example.com")
                .password("securePass123!")
                .tel("01012345678")
                .role(0)
                .build();

        //Then
        assertThat(user.getName()).isEqualTo("김싸피");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("securePass123!");
        assertThat(user.getTel()).isEqualTo("01012345678");
        assertThat(user.getRole()).isEqualTo(0); // 고객(0) 설정 확인
    }
}
