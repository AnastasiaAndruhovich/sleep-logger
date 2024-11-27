package com.noom.interview.fullstack.sleep;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SleepApplication {

    private final String timezone;

    public SleepApplication(@Value("${spring.application.timezone}") String timezone) {
        this.timezone = timezone;
    }

    public static void main(String[] args) {
        SpringApplication.run(SleepApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
    }

}
