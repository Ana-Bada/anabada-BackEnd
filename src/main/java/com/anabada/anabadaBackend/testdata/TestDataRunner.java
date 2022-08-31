package com.anabada.anabadaBackend.testdata;

import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws IOException {

        UserEntity testUser1 = new UserEntity("donggyu@gmail.com","안동규", passwordEncoder.encode("qla3456Q!"));
        UserEntity testUser2 = new UserEntity("donggyu1@gmail.com", "동규안", passwordEncoder.encode("qla3456Q!"));
        UserEntity testUser3 = new UserEntity("donggyu2@gmail.com", "동규", passwordEncoder.encode("qla3456Q!"));
        UserEntity testUser4 = new UserEntity("donggyu3@gmail.com", "Donggyu", passwordEncoder.encode("qla3456Q!"));
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        userRepository.save(testUser4);

    }
}
