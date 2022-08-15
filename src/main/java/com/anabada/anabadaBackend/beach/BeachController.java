package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BeachController {

    private final BeachService beachService;

    @GetMapping("/api/beaches")
    public ResponseEntity<?> getBeaches(){
        return beachService.getBeaches();
    }

    @GetMapping("/api/beaches/{beachId}")
    public ResponseEntity<?> getBeachWeather(@PathVariable Long beachId){
        return new ResponseEntity<>(beachService.getBeachWeather(beachId), HttpStatus.OK);
    }

    @GetMapping("/api/beach")
    public ResponseEntity<?> getBeach(){
        return beachService.getBeach();
    }
}
