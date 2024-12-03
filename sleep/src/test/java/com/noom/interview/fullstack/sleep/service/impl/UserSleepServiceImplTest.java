package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.exception.ConflictRequestException;
import com.noom.interview.fullstack.sleep.exception.NotFoundException;
import com.noom.interview.fullstack.sleep.generator.UserGenerator;
import com.noom.interview.fullstack.sleep.generator.UserSleepGenerator;
import com.noom.interview.fullstack.sleep.mapper.UserSleepMapper;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.repository.UserSleepRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSleepServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSleepRepository userSleepRepository;

    private final UserSleepMapper userSleepMapper = Mappers.getMapper(UserSleepMapper.class);

    private UserSleepServiceImpl userSleepService;

    private static LocalDate currentDate;
    private static LocalDate oneMonthBeforeDate;

    @BeforeAll
    static void setUp() {
        currentDate = LocalDate.now();
        oneMonthBeforeDate = currentDate.minusMonths(1);
    }

    @BeforeEach
    void init() {
        userSleepService = new UserSleepServiceImpl(userRepository, userSleepRepository, userSleepMapper);
    }

    @Test
    @DisplayName("Should save user sleep successfully")
    void shouldSaveUserSleep() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        SleepDto sleepDto = UserSleepGenerator.generateSleepDto();

        UserSleep userSleep = userSleepMapper.mapSleepDtoToUserSleep(sleepDto);
        userSleep.setUser(testUser);

        UserSleep expectedUserSleep = userSleepMapper.mapSleepDtoToUserSleep(sleepDto);
        expectedUserSleep.setId(1L);
        expectedUserSleep.setUser(testUser);


        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.save(userSleep)).thenReturn(expectedUserSleep);

        long savedId = userSleepService.saveUserSleep(testUser.getId(), sleepDto);

        assertEquals(expectedUserSleep.getId(), savedId);
        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).save(userSleep);
    }

    @Test
    @DisplayName("Should throw ConflictRequestException when user sleep already exists")
    void shouldSaveUserSleep_whenUserSleepExists_throwConflictRequestException() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        Date createdDate = Date.valueOf(LocalDate.now());
        SleepDto sleepDto = UserSleepGenerator.generateSleepDto();
        sleepDto.setCreatedDate(LocalDate.now());

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UserSleep existingUserSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        existingUserSleep.setCreatedDate(createdDate);
        when(userSleepRepository.findByUserIdAndCreatedDate(testUser.getId(), createdDate))
                .thenReturn(Optional.of(existingUserSleep));

        assertThrows(ConflictRequestException.class, () -> userSleepService.saveUserSleep(testUser.getId(), sleepDto));
        verify(userSleepRepository, never()).save(any(UserSleep.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when user is not found while saving user sleep")
    void shouldSaveUserSleep_whenUserNotFound_throwNotFoundException() {
        long userId = -1L;
        SleepDto sleepDto = UserSleepGenerator.generateSleepDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSleepService.saveUserSleep(userId, sleepDto));

        verify(userRepository).findById(userId);
        verify(userSleepRepository, never()).save(any(UserSleep.class));
    }

    @Test
    @DisplayName("Should find the last night's sleep by user Id")
    void shouldFindLastNightSleepByUserId() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        UserSleep userSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        userSleep.setUser(testUser);
        SleepDto expectedSleepDto = userSleepMapper.mapUserSleepToSleepDto(userSleep); // Use real mapping

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDate(eq(testUser.getId()), any(Date.class))).thenReturn(Optional.of(userSleep));

        SleepDto actualSleepDto = userSleepService.findLastNightSleepByUserId(testUser.getId());

        assertNotNull(actualSleepDto);
        assertEquals(expectedSleepDto, actualSleepDto);

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).findByUserIdAndCreatedDate(eq(testUser.getId()), any(Date.class));
    }

    @Test
    @DisplayName("Should return null when no sleep logs are found for the last night")
    void shouldFindLastNightSleepByUserId_whenNoLogsFound_returnNull() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDate(eq(testUser.getId()), any(Date.class))).thenReturn(Optional.empty());

        SleepDto actualSleepDto = userSleepService.findLastNightSleepByUserId(testUser.getId());

        assertNull(actualSleepDto);
        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).findByUserIdAndCreatedDate(eq(testUser.getId()), any(Date.class));
    }

    @Test
    @DisplayName("Should find all user sleep records within the last month")
    void shouldFindLastMonthSleep() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        List<UserSleep> userSleepList = Arrays.asList(
                UserSleepGenerator.generateUserSleepWithoutIdAndUser(),
                UserSleepGenerator.generateUserSleepWithoutIdAndUser()
        );
        List<SleepDto> expectedSleepDtoList = userSleepMapper.mapUserSleepListToSleepDtoList(userSleepList); // Use real mapping

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)))).thenReturn(userSleepList);

        List<SleepDto> actualSleepDtoList = userSleepService.findLastMonthSleepByUserId(testUser.getId());

        assertNotNull(actualSleepDtoList);
        assertEquals(expectedSleepDtoList.size(), actualSleepDtoList.size());

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)));
    }

    @Test
    @DisplayName("Should return an empty list when no sleep records are found within the last month")
    void shouldFindLastMonthSleep_whenNoSleepRecordsFound_returnEmptyList() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)))).thenReturn(Collections.emptyList());

        List<SleepDto> actualSleepDtoList = userSleepService.findLastMonthSleepByUserId(testUser.getId());

        assertNotNull(actualSleepDtoList);
        assertTrue(actualSleepDtoList.isEmpty());

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)));
    }

    @Test
    @DisplayName("Should calculate the average sleep for the user within the last month")
    void shouldCalculateAverageSleepWithinLastMonthForUserById() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        AverageUserSleep averageUserSleep = UserSleepGenerator.generateAverageUserSleep();
        AverageSleepDto expectedAverageSleepDto = userSleepMapper.mapAverageUserSleepToAverageSleepDto(averageUserSleep);
        expectedAverageSleepDto.setStartDate(oneMonthBeforeDate);
        expectedAverageSleepDto.setEndDate(currentDate);

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)))).thenReturn(averageUserSleep);

        AverageSleepDto actualAverageSleepDto = userSleepService.calculateAverageSleepWithinLastMonthForUserById(testUser.getId());

        assertNotNull(actualAverageSleepDto);
        assertEquals(expectedAverageSleepDto.getStartDate(), actualAverageSleepDto.getStartDate());
        assertEquals(expectedAverageSleepDto.getEndDate(), actualAverageSleepDto.getEndDate());

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)));
    }

    @Test
    @DisplayName("Should return null when no average sleep data is found for the user within the last month")
    void shouldCalculateAverageSleepWithinLastMonthForUserById_whenNoAverageDataFound_returnNull() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)))).thenReturn(new AverageUserSleep());

        AverageSleepDto expectedAverageSleepDto = userSleepService.calculateAverageSleepWithinLastMonthForUserById(testUser.getId());

        assertNull(expectedAverageSleepDto);
        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)));
    }
}