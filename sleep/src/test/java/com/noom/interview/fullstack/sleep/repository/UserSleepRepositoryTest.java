package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.generator.UserGenerator;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@RequiredArgsConstructor
public class UserSleepRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSleepRepository userSleepRepository;

    @Test
    @DisplayName("Should save UserSleep entity and verify all fields are persisted correctly")
    void shouldSave() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserGenerator.generateUserSleepWithoutIdAndUser();
        sampledUserSleep.setUser(expectedUser);

        UserSleep actualUserSleep = userSleepRepository.save(sampledUserSleep);

        assertNotNull(actualUserSleep.getId());
        assertEquals(sampledUserSleep.getUser(), actualUserSleep.getUser());
        assertEquals(sampledUserSleep.getCreatedDate(), actualUserSleep.getCreatedDate());
        assertEquals(sampledUserSleep.getFallAsleepTime(), actualUserSleep.getFallAsleepTime());
        assertEquals(sampledUserSleep.getWakeUpTime(), actualUserSleep.getWakeUpTime());
        assertEquals(sampledUserSleep.getSleepingTimeInMinutes(), actualUserSleep.getSleepingTimeInMinutes());
        assertEquals(sampledUserSleep.getFeeling(), actualUserSleep.getFeeling());
    }

    @Test
    @DisplayName("Should find UserSleep entity by userId and createdDate")
    void shouldFindByUserIdAndCreatedDate() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserGenerator.generateUserSleepWithoutIdAndUser();
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        UserSleep actualUserSleep = userSleepRepository.findByUserIdAndCreatedDate(expectedUserSleep.getUser().getId(), expectedUserSleep.getCreatedDate()).orElse(null);

        assertNotNull(actualUserSleep);
        assertEquals(expectedUserSleep, actualUserSleep);
    }

    @Test
    @DisplayName("Should return empty when no UserSleep is found for userId")
    void shouldFindByUserIdAndCreatedDate_whenNotFoundByUserId_returnsEmptyObject() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserGenerator.generateUserSleepWithoutIdAndUser();
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        Optional<UserSleep> actualUserSleepOpt = userSleepRepository.findByUserIdAndCreatedDate((long) 4, expectedUserSleep.getCreatedDate());

        assertTrue(actualUserSleepOpt.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when no UserSleep is found for createdDate")
    void shouldFindByUserIdAndCreatedDate_whenNotFoundByUserCreatedDate_returnsEmptyObject() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserGenerator.generateUserSleepWithoutIdAndUser();
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        Optional<UserSleep> actualUserSleepOpt = userSleepRepository.findByUserIdAndCreatedDate(expectedUserSleep.getUser().getId(), Date.valueOf("3025-11-03"));

        assertTrue(actualUserSleepOpt.isEmpty());
    }

    /*@ParameterizedTest
    @MethodSource("provideArgumentsForUserAverageSleep")
    @DisplayName("")
    void shouldCalculateUserAverageSleepWithinPeriod(long userId, Date startDate, Date endDate, AverageUserSleep expectedAverageUSerSleep) {
        *//*AverageUserSleep actualAverageUserSleep = userSleepRepository.calculateUserAverageSleepWithinPeriod(userId, startDate, endDate);

        assertEquals(expectedAverageUSerSleep, actualAverageUserSleep);*//*
    }

    private static Stream<Arguments> provideArgumentsForUserAverageSleep() {
        return Stream.of(Arguments.of((long) 1, Date.valueOf("2024-11-01"), Date.valueOf("2024-11-30"),
                AverageUserSleep.builder().avgSleepingTimeInMinutes(363).avgFallAsleepTime(Time.valueOf("22:13:45")).avgWakeTimeTime(Time.valueOf("06:20:00")).goodCount(2).okCount(0).badCount(2).build()));
    }*/

}
