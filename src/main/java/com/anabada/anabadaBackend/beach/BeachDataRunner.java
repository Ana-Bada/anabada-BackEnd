package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BeachDataRunner implements ApplicationRunner {
    private final BeachRepository beachRepository;

    @Override
    public void run(ApplicationArguments args) {
        ClassPathResource resource = new ClassPathResource("data/beach.txt");
        try {
            Path path = Paths.get(resource.getURI());
            List<String> content = Files.readAllLines(path);
//            System.out.println(content.get(0).split(",")[0]);
//            content.forEach(System.out::println);
            for (int i = 0; i < content.size(); i++) {
                double x = Double.parseDouble(content.get(i).split(",")[1]);
                double y = Double.parseDouble(content.get(i).split(",")[2]);
                beachRepository.save(new BeachEntity(content.get(i).split(",")[0], x, y));
            }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
}