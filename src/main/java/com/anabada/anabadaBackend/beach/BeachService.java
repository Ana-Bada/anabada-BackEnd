package com.anabada.anabadaBackend.beach;

import com.anabada.anabadaBackend.beach.dto.BeachResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BeachService {
    private final BeachRepository beachRepository;

    public BeachResponseDto getBeachWeather(Long beachId){
        BeachEntity beach = beachRepository.findById(beachId)
                .orElseThrow( ()-> new IllegalArgumentException("해변이 존재하지 않습니다."));
        return new BeachResponseDto(beach);
    }

    public ResponseEntity<?> getBeach() {
        List<BeachEntity> beachList = beachRepository.findAll();
        return new ResponseEntity<>(beachList, HttpStatus.OK);
    }
}
