package com.anabada.anabadaBackend.thunderlike;

import com.anabada.anabadaBackend.testconfig.TestConfig;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestConfig.class)
@DataJpaTest
public class ThunderLikeRepositoryTest {

    @Autowired
    private ThunderLikeRepository thunderLikeRepository;

    @Autowired
    private ThunderLikeRepositoryImpl thunderLikeRepositoryImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThunderPostRepository thunderPostRepository;

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

        ThunderPostEntity thunderPost1 = ThunderPostEntity.builder()
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
                .user(user1)
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
                .viewCount(8)
                .user(user2)
                .build();

        ThunderPostEntity thunderPost3 = ThunderPostEntity.builder()
                .title("제목")
                .content("내용")
                .area("제주")
                .address("제주 표선해비치")
                .goalMember(15)
                .currentMember(0)
                .thumbnailUrl(".jpeg")
                .meetDate("2022-10-12")
                .endDate("2022-10-16")
                .viewCount(1)
                .user(user3)
                .build();

        thunderPostRepository.save(thunderPost1);
        thunderPostRepository.save(thunderPost2);
        thunderPostRepository.save(thunderPost3);

        ThunderLikeEntity thunderLike = ThunderLikeEntity.builder()
                .user(user1)
                .thunderPost(thunderPost1)
                .build();

        ThunderLikeEntity thunderLike2 = ThunderLikeEntity.builder()
                .user(user2)
                .thunderPost(thunderPost1)
                .build();

        ThunderLikeEntity thunderLike3 = ThunderLikeEntity.builder()
                .user(user3)
                .thunderPost(thunderPost1)
                .build();

        ThunderLikeEntity thunderLike4 = ThunderLikeEntity.builder()
                .user(user1)
                .thunderPost(thunderPost2)
                .build();

        ThunderLikeEntity thunderLike5 = ThunderLikeEntity.builder()
                .user(user2)
                .thunderPost(thunderPost2)
                .build();

        thunderLikeRepository.save(thunderLike);
        thunderLikeRepository.save(thunderLike2);
        thunderLikeRepository.save(thunderLike3);
        thunderLikeRepository.save(thunderLike4);
        thunderLikeRepository.save(thunderLike5);
    }

    @Order(1)
    @DisplayName("전체 조회 성공")
    @Test
    void test1() {
        assertThat(thunderLikeRepository.findAll().size()).isEqualTo(5);
    }

    @Order(2)
    @DisplayName("조회 성공")
    @Test
    void test2() {

        UserEntity user = UserEntity.builder()
                .email("test4@gmail.com")
                .nickname("test4")
                .password("1234abCD!")
                .profileImg(".jpg")
                .build();

        userRepository.save(user);

        ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                .title("제목")
                .content("내용")
                .area("강원")
                .address("강원 양양 죽도해변")
                .goalMember(15)
                .currentMember(0)
                .thumbnailUrl(".jpeg")
                .meetDate("2022-10-12")
                .endDate("2022-10-16")
                .viewCount(5)
                .user(user)
                .build();

        thunderPostRepository.save(thunderPost);

        ThunderLikeEntity thunderLike = ThunderLikeEntity.builder()
                .user(user)
                .thunderPost(thunderPost)
                .build();

        thunderLikeRepository.save(thunderLike);
        Optional<ThunderLikeEntity> foundLike = thunderLikeRepository.findById(11L);
        assertThat(foundLike.get().getThunderLikeId()).isEqualTo(11L);
        assertThat(foundLike.get().getThunderPost().getTitle()).isEqualTo("제목");
        assertThat(foundLike.get().getThunderPost().getAddress()).isEqualTo("강원 양양 죽도해변");
        assertThat(foundLike.get().getUser().getUserId()).isEqualTo(28L);
        assertThat(foundLike.get().getUser().getNickname()).isEqualTo("test4");
    }

    @Order(3)
    @DisplayName("게시글 아이디, 유저 아이디 값으로 조회 성공")
    @Test
    void test3() {
        System.out.println(thunderLikeRepository.findAll().get(0).getThunderPost().getThunderPostId());
        Long id = thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(29L, 29L);
        Long id2 = thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(30L, 30L);
        assertThat(id).isEqualTo(12L);
        assertThat(id2).isEqualTo(16L);
    }
}
