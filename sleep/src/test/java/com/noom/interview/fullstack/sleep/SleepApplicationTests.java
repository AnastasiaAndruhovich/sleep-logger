package com.noom.interview.fullstack.sleep;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("unit-test")
public class SleepApplicationTests {

    @Test
    public void contextLoads() {
        assertThat(true).isTrue();
    }

}
