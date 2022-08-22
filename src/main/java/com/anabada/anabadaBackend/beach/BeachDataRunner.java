package com.anabada.anabadaBackend.beach;

import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class BeachDataRunner implements ApplicationRunner {
    private final BeachRepository beachRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        ClassPathResource resource = new ClassPathResource("data/beach.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            double x = Double.parseDouble(line.split(",")[1]);
            double y = Double.parseDouble(line.split(",")[2]);
            beachRepository.save(new BeachEntity(line.split(",")[0], x, y));
        }
        UserEntity testUser1 = new UserEntity("donggyu@gmail.com","안동규", passwordEncoder.encode("qla3456Q!"));
        userRepository.save(testUser1);
    }
}