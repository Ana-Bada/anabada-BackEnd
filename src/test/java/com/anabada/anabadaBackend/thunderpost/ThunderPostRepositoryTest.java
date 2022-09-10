package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.testconfig.TestConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestConfig.class)
@DataJpaTest
public class ThunderPostRepositoryTest {
}
