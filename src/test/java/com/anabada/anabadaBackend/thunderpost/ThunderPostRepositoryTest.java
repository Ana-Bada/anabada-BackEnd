package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.testconfig.TestConfig;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestConfig.class)
@DataJpaTest
public class ThunderPostRepositoryTest {

    @Autowired
    private ThunderPostRepository thunderPostRepository;

    @Autowired
    private ThunderPostRepositoryImpl thunderPostRepositoryImpl;

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

    }

    @Order(1)
    @DisplayName("전체 조회 성공")
    @Test
    void test1() {
        List<ThunderPostEntity> thunderPosts = thunderPostRepository.findAll();
        assertThat(thunderPosts.size()).isEqualTo(3);
    }

    @Order(2)
    @DisplayName("각 게시글 유저 닉네임 체크 성공")
    @Test
    void test2() {
        List<ThunderPostEntity> thunderPosts = thunderPostRepository.findAll();
        assertThat(thunderPosts.get(0).getUser().getNickname()).isEqualTo("test");
        assertThat(thunderPosts.get(1).getUser().getNickname()).isEqualTo("test1");
        assertThat(thunderPosts.get(2).getUser().getNickname()).isEqualTo("test2");
    }

    @Order(3)
    @DisplayName("지역별 조회 성공")
    @Test
    void test3() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByArea("제주", pageable);
        assertThat(responseDtos.getSize()).isEqualTo(10);
        assertThat(responseDtos.getNumberOfElements()).isEqualTo(1);
        assertThat(responseDtos.getContent().get(0).getArea()).isEqualTo("제주");
        assertThat(responseDtos.getContent().get(0).getAddress()).isEqualTo("제주 표선해비치");
    }

    @Order(4)
    @DisplayName("게시글 Id로 조회 성공")
    @Test
    void test4() {

        ThunderPostResponseDto responseDto = thunderPostRepositoryImpl.findByThunderPostId(10L);

        assertThat(responseDto.getThunderPostId()).isEqualTo(10L);
        assertThat(responseDto.getArea()).isEqualTo("강원");
        assertThat(responseDto.getNickname()).isEqualTo("test");
    }

    @Order(5)
    @DisplayName("검색어로 조회 성공")
    @Test
    void test5() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        String keyword = "제목";

        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByAreaAndKeyword("ALL", keyword, pageable);
        assertThat(responseDtos.getSize()).isEqualTo(10);
        assertThat(responseDtos.getNumberOfElements()).isEqualTo(3);
        assertThat(responseDtos.getContent().get(2).getArea()).isEqualTo("강원");
        assertThat(responseDtos.getContent().get(2).getTitle().contains(keyword)).isTrue();

        Slice<ThunderPostResponseDto> responseDtos1 = thunderPostRepositoryImpl.findAllByAreaAndKeyword("제주", keyword, pageable);
        assertThat(responseDtos1.getSize()).isEqualTo(10);
        assertThat(responseDtos1.getNumberOfElements()).isEqualTo(1);
        assertThat(responseDtos1.getContent().get(0).getArea()).isEqualTo("제주");
        assertThat(responseDtos1.getContent().get(0).getTitle().contains(keyword)).isTrue();
    }

    @Order(6)
    @DisplayName("인기 게시물 조회 성공")
    @Test
    void test6() {

        List<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findHotPost("ALL");
        assertThat(responseDtos.size()).isEqualTo(3);
        assertThat(responseDtos.get(0).getViewCount()).isEqualTo(8);

        List<ThunderPostResponseDto> responseDtos1 = thunderPostRepositoryImpl.findHotPost("강원");
        assertThat(responseDtos1.size()).isEqualTo(2);
        assertThat(responseDtos1.get(0).getViewCount()).isEqualTo(8);
        assertThat(responseDtos1.get(0).getArea()).isEqualTo("강원");
        assertThat(responseDtos1.get(1).getArea()).isEqualTo("강원");

    }

    @Order(7)
    @DisplayName("본인이 작성한 게시글 조회 성공")
    @Test
    void test7() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByFilter("myHostMeet", 19L, pageable);
        assertThat(responseDtos.getSize()).isEqualTo(10);
        assertThat(responseDtos.getNumberOfElements()).isEqualTo(1);
        assertThat(responseDtos.getContent().get(0).getNickname()).isEqualTo("test");

        Slice<ThunderPostResponseDto> responseDtos1 = thunderPostRepositoryImpl.findAllByFilter("myHostMeet", 21L, pageable);
        assertThat(responseDtos1.getSize()).isEqualTo(10);
        assertThat(responseDtos1.getNumberOfElements()).isEqualTo(1);
        assertThat(responseDtos1.getContent().get(0).getNickname()).isEqualTo("test2");

    }
}
