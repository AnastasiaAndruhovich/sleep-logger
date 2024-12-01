package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@RequiredArgsConstructor
public class UserSleepRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSleepRepository userSleepRepository;

    private final User sampledUser = User.builder()
            .name("Harry Potter")
            .email("harry.potter@test.com").build();

    private final UserSleep sampledUserSleep = UserSleep.builder()
            .createdDate(Date.valueOf("2024-11-01"))
            .fallAsleepTime(Time.valueOf("22:30:00"))
            .wakeUpTime(Time.valueOf("06:30:00"))
            .sleepingTimeInMinutes(323)
            .feeling(Feeling.GOOD)
            .build();

    @Test
    @DisplayName("Should save UserSleep entity and verify all fields are persisted correctly")
    void shouldSave() {
        User expectedUser = userRepository.save(sampledUser);
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
        User expectedUser = userRepository.save(sampledUser);
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        UserSleep actualUserSleep = userSleepRepository.findByUserIdAndCreatedDate(expectedUserSleep.getUser().getId(), expectedUserSleep.getCreatedDate()).orElse(null);

        assertNotNull(actualUserSleep);
        assertEquals(expectedUserSleep, actualUserSleep);
    }

    @Test
    @DisplayName("Should return empty when no UserSleep is found for userId")
    void shouldFindByUserIdAndCreatedDate_whenNotFoundByUserId_returnsEmptyObject() {
        User expectedUser = userRepository.save(sampledUser);
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        Optional<UserSleep> actualUserSleepOpt = userSleepRepository.findByUserIdAndCreatedDate((long) 4, expectedUserSleep.getCreatedDate());

        assertTrue(actualUserSleepOpt.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when no UserSleep is found for createdDate")
    void shouldFindByUserIdAndCreatedDate_whenNotFoundByUserCreatedDate_returnsEmptyObject() {
        User expectedUser = userRepository.save(sampledUser);
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        Optional<UserSleep> actualUserSleepOpt = userSleepRepository.findByUserIdAndCreatedDate(expectedUserSleep.getUser().getId(), Date.valueOf("3025-11-03"));

        assertTrue(actualUserSleepOpt.isEmpty());
    }

    @Test
    @DisplayName("")
    @Sql(value = {"/db/init_users.sql", "/db/init_user_sleep.sql"})
    void shouldCalculateUserAverageSleepWithinPeriod() {

    }

}
