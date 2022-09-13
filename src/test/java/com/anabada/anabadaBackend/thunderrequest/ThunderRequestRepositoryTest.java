package com.anabada.anabadaBackend.thunderrequest;

import com.anabada.anabadaBackend.testconfig.TestConfig;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestConfig.class)
@DataJpaTest
public class ThunderRequestRepositoryTest {

    @Autowired
    private ThunderRequestRepository thunderRequestRepository;

    @Autowired
    private ThunderPostRepository thunderPostRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {

        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("1234abCD!")
                .profileImg(".jpg")
                .build();

        UserEntity user2 = UserEntity.builder()
                .email("test2@gmail.com")
                .nickname("test2")
                .password("1234abCD!")
                .profileImg(".jpg")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                .title("제목")
                .content("내용")
                .area("강원")
                .address("강원 양양")
                .goalMember(15)
                .currentMember(0)
                .thumbnailUrl(".jpeg")
                .meetDate("2022-10-12")
                .endDate("2022-10-16")
                .viewCount(5)
                .user(user)
                .build();

        ThunderPostEntity thunderPost2 = ThunderPostEntity.builder()
                .title("제목")
                .content("내용")
                .area("강원")
                .address("강원 양양")
                .goalMember(15)
                .currentMember(0)
                .thumbnailUrl(".jpeg")
                .meetDate("2022-10-12")
                .endDate("2022-10-16")
                .viewCount(5)
                .user(user2)
                .build();

        thunderPostRepository.save(thunderPost);
        thunderPostRepository.save(thunderPost2);

        ThunderRequestEntity thunderRequest = ThunderRequestEntity.builder()
                .thunderPost(thunderPost)
                .user(user)
                .build();

        ThunderRequestEntity thunderRequest2 = ThunderRequestEntity.builder()
                .thunderPost(thunderPost)
                .user(user2)
                .build();

        ThunderRequestEntity thunderRequest3 = ThunderRequestEntity.builder()
                .thunderPost(thunderPost2)
                .user(user2)
                .build();

        thunderRequestRepository.save(thunderRequest);
        thunderRequestRepository.save(thunderRequest2);
        thunderRequestRepository.save(thunderRequest3);
    }

    @Order(1)
    @DisplayName("게시글 번호로 조회 성공")
    @Test
    void test1() {
        List<ThunderRequestEntity> thunderRequests = thunderRequestRepository.findAllByThunderPostThunderPostId(1L);
        assertThat(thunderRequests.size()).isEqualTo(2);
        List<ThunderRequestEntity> thunderRequests2 = thunderRequestRepository.findAllByThunderPostThunderPostId(2L);
        assertThat(thunderRequests2.size()).isEqualTo(1);
    }

    @Order(2)
    @DisplayName("게시글 번호, 유저 아이디로 조회 성공")
    @Test
    void test2() {
        ThunderRequestEntity thunderRequest = thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(1L, 1L);
        assertThat(thunderRequest.getThunderPost().getThunderPostId()).isEqualTo(1L);
        assertThat(thunderRequest.getThunderPost().getUser().getUserId()).isEqualTo(1L);
    }
}
