package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BeachDataRunner implements ApplicationRunner {
    private final BeachRepository beachRepository;

    @Override
    public void run(ApplicationArguments args) throws IOException {
//        beachRepository.deleteAll();
//
//        ClassPathResource resource = new ClassPathResource("data/beach.txt");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            double y = Double.parseDouble(line.split(",")[2]);
//            double x = Double.parseDouble(line.split(",")[3]);
//            beachRepository.save(new BeachEntity(line.split(",")[0], line.split(",")[1], x, y));
//        }
    }
}