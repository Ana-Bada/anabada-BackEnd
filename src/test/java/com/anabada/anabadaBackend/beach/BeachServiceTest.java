package com.anabada.anabadaBackend.beach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeachServiceTest {

    @InjectMocks
    private BeachService beachService;

    @Mock
    private BeachRepository beachRepository;

    @Nested
    @DisplayName("해수욕장 날씨정보 불러오기")
    class GetWeather {

        @Test
        @Order(1)
        @DisplayName("실패(해수욕장이 존재하지 않음)")
        public void test1() {

            when(beachRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                beachService.getBeachWeather(1L);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                beachService.getBeachWeather(1L);
            }).hasMessage("404 NOT_FOUND \"해변이 존재하지 않습니다.\"");
        }

        @Test
        @Order(2)
        @DisplayName("성공")
        public void test2() {

            BeachEntity beach = new BeachEntity();

            when(beachRepository.findById(1L))
                    .thenReturn(Optional.of(beach));

            ResponseEntity<?> response = beachService.getBeachWeather(1L);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);

        }
    }

    @Nested
    @DisplayName("해수욕장 불러오기")
    class GetBeach {

        @Test
        @Order(1)
        @DisplayName("성공")
        public void test1() {
            BeachEntity beach = new BeachEntity();
            BeachEntity beach2 = new BeachEntity();

            List<BeachEntity> beachList = new ArrayList<>();

            beachList.add(beach);
            beachList.add(beach2);

            when(beachRepository.findAll())
                    .thenReturn(beachList);

            ResponseEntity<?> response = beachService.getBeach();
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }
    }
}
