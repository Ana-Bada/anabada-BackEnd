package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BeachDataRunner implements ApplicationRunner {

    private final BeachRepository beachRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {


    }

}
