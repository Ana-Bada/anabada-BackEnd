package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.testconfig.TestConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestConfig.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {

        UserEntity user1 = UserEntity.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("1234abCD!")
                .profileImg(".jpg")
                .build();

        UserEntity user2 = UserEntity.builder()
                .email("test1@gmail.com")
                .nickname("test1")
                .password("1234abCD!")
                .profileImg(".jpg")
                .build();

        UserEntity user3 = UserEntity.builder()
                .email("test2@gmail.com")
                .nickname("test2")
                .password("1234abCD!")
                .profileImg(".jpg")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
    @Order(1)
    @DisplayName("전체 조회 성공")
    @Test
    void test1() {
        List<UserEntity> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(3);
    }

    @Order(2)
    @DisplayName("이메일로 조회 성공")
    @Test
    void test2() {
        Optional<UserEntity> user = userRepository.findByEmail("test@gmail.com");
        assertThat(user.get().getEmail()).isEqualTo("test@gmail.com");
    }

    @Order(3)
    @DisplayName("닉네임으로 조회 성공")
    @Test
    void test3() {
        Optional<UserEntity> user = userRepository.findByNickname("test");
        assertThat(user.get().getNickname()).isEqualTo("test");
    }

    @Order(4)
    @DisplayName("존재하지 않는 이메일로 조회")
    @Test
    void test4() {
        assertThat(userRepository.findByEmail("test5@gmail.com")).isEmpty();
    }

    @Order(5)
    @DisplayName("존재하지 않는 닉네임으로 조회")
    @Test
    void test5() {
        assertThat(userRepository.findByEmail("test5")).isEmpty();
    }
}
