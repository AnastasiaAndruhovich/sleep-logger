package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.generator.UserGenerator;
import com.noom.interview.fullstack.sleep.generator.UserSleepGenerator;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@RequiredArgsConstructor
public class UserSleepRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSleepRepository userSleepRepository;

    private static LocalDate currentDate;
    private static LocalDate oneMonthBeforeDate;

    @BeforeAll
    static void setUp() {
        currentDate = LocalDate.now();
        oneMonthBeforeDate = currentDate.minusMonths(1);
    }

    @Test
    @DisplayName("Should save UserSleep entity and verify all fields are persisted correctly")
    void shouldSave() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
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
    @DisplayName("Should throw DataIntegrityViolationException when saving duplicate user_id and created_date")
    void shouldSave_whenDuplicateUserIdAndCreatedDate_throwDataIntegrityViolationException() {
        User testUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        userRepository.save(testUser);

        UserSleep firstUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        firstUserSleep.setUser(testUser);
        firstUserSleep.setCreatedDate(Date.valueOf(LocalDate.now().minusYears(1)));
        userSleepRepository.save(firstUserSleep);

        UserSleep duplicateUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        duplicateUserSleep.setUser(testUser);
        duplicateUserSleep.setCreatedDate(firstUserSleep.getCreatedDate());

        assertThrows(DataIntegrityViolationException.class, () -> userSleepRepository.save(duplicateUserSleep));
    }

    @Test
    @DisplayName("Should find UserSleep entity by userId and createdDate")
    void shouldFindByUserIdAndCreatedDate() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
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
        UserSleep sampledUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        Optional<UserSleep> actualUserSleepOpt = userSleepRepository.findByUserIdAndCreatedDate(-1L, expectedUserSleep.getCreatedDate());

        assertTrue(actualUserSleepOpt.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when no UserSleep is found for createdDate")
    void shouldFindByUserIdAndCreatedDate_whenNotFoundByUserCreatedDate_returnsEmptyObject() {
        User expectedUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());
        UserSleep sampledUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        sampledUserSleep.setUser(expectedUser);
        UserSleep expectedUserSleep = userSleepRepository.save(sampledUserSleep);

        Optional<UserSleep> actualUserSleepOpt = userSleepRepository.findByUserIdAndCreatedDate(expectedUserSleep.getUser().getId(), Date.valueOf("3025-11-03"));

        assertTrue(actualUserSleepOpt.isEmpty());
    }

    @Test
    @DisplayName("Should find user sleep records within the specified date range")
    void shouldFindUserSleepRecordsWithinDateRange() {
        User testUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());

        UserSleep userSleep1 = UserSleepGenerator.generateUserSleepWithoutUser();
        userSleep1.setUser(testUser);
        userSleep1.setCreatedDate(Date.valueOf(currentDate.minusDays(3)));

        UserSleep userSleep2 = UserSleepGenerator.generateUserSleepWithoutUser();
        userSleep2.setUser(testUser);
        userSleep2.setCreatedDate(Date.valueOf(currentDate.minusDays(5)));

        userSleepRepository.save(userSleep1);
        userSleepRepository.save(userSleep2);

        List<UserSleep> actualUserSleepList = userSleepRepository.findByUserIdAndCreatedDateBetween(testUser.getId(), Date.valueOf(oneMonthBeforeDate), Date.valueOf(currentDate));

        assertNotNull(actualUserSleepList);
        assertEquals(2, actualUserSleepList.size());
    }

    @Test
    @DisplayName("Should return an empty list when no user sleep records are found within the date range")
    void shouldFindUserSleepRecordsWithinDateRange_whenNoRecordsFound_returnEmptyList() {
        Date startDateSql = Date.valueOf(currentDate.plusDays(1));
        Date endDateSql = Date.valueOf(currentDate.plusMonths(1));

        User testUser = userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());

        UserSleep userSleep1 = UserSleepGenerator.generateUserSleepWithoutUser();
        userSleep1.setUser(testUser);
        userSleep1.setCreatedDate(Date.valueOf(currentDate.minusDays(3)));

        UserSleep userSleep2 = UserSleepGenerator.generateUserSleepWithoutUser();
        userSleep2.setUser(testUser);
        userSleep2.setCreatedDate(Date.valueOf(currentDate.minusDays(5)));

        userSleepRepository.save(userSleep1);
        userSleepRepository.save(userSleep2);

        List<UserSleep> actualUserSleepList = userSleepRepository.findByUserIdAndCreatedDateBetween(testUser.getId(), startDateSql, endDateSql);

        assertNotNull(actualUserSleepList);
        assertTrue(actualUserSleepList.isEmpty());
    }

    @Test
    @DisplayName("should return an empty list when no user sleep records are found for the given user")
    void shouldReturnEmptyListWhenNoRecordsFoundForUser() {
        long userId = -1L;
        userRepository.save(UserGenerator.generateUserWithoutIdAndUserSleep());

        List<UserSleep> result = userSleepRepository.findByUserIdAndCreatedDateBetween(userId, Date.valueOf(oneMonthBeforeDate), Date.valueOf(currentDate));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
