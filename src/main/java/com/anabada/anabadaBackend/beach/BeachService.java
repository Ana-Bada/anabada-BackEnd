package com.anabada.anabadaBackend.beach;

import com.anabada.anabadaBackend.beach.dto.BeachResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BeachService {
    private final BeachRepository beachRepository;
    private final BeachWeatherData beachWeatherData;
    
    public ResponseEntity<?> getBeaches() throws IOException, ParseException {
        List<BeachEntity> beachList = beachRepository.findAll();
        List<BeachResponseDto> beachResponseDtoList = new ArrayList<>();
        for(BeachEntity beach : beachList) {
            HashMap<String, String> hm = beachWeatherData.getBeachesWeather(beach);
            BeachResponseDto beachResponseDto = new BeachResponseDto(beach, hm.get("TMP"), hm.get("WAV"));
            beachResponseDtoList.add(beachResponseDto);
        }
        return new ResponseEntity<>(beachResponseDtoList, HttpStatus.OK);
    }

    public BeachResponseDto getBeachWeather(Long beachId) throws IOException, ParseException {
        BeachEntity beach = beachRepository.findById(beachId)
                .orElseThrow( ()-> new IllegalArgumentException("해변이 존재하지 않습니다."));
        return new BeachResponseDto(beach, beachWeatherData.getBeachWeather(beach.getX(), beach.getY()));
    }
}
