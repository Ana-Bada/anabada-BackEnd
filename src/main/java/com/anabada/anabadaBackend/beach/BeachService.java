package com.anabada.anabadaBackend.beach;

import com.anabada.anabadaBackend.beach.dto.BeachResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BeachService {
    private final BeachRepository beachRepository;
    
    public ResponseEntity<?> getBeaches(){
        List<BeachEntity> beachList = beachRepository.findAll();
        List<BeachResponseDto> beachResponseDtoList = new ArrayList<>();
        for(BeachEntity beach : beachList) {
            BeachResponseDto beachResponseDto = new BeachResponseDto(beach);
            beachResponseDtoList.add(beachResponseDto);
        }
        return new ResponseEntity<>(beachResponseDtoList, HttpStatus.OK);
    }

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
