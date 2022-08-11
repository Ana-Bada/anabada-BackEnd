package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class BeachController {

    private final BeachService beachService;

    @GetMapping("/api/beaches")
    public ResponseEntity<?> getBeaches() throws IOException, ParseException {
        return beachService.getBeaches();
    }

    @GetMapping("/api/beaches/{beachId}")
    public ResponseEntity<?> getBeachWeather(@PathVariable Long beachId) throws IOException, ParseException {
        return new ResponseEntity<>(beachService.getBeachWeather(beachId), HttpStatus.OK);
    }
}
