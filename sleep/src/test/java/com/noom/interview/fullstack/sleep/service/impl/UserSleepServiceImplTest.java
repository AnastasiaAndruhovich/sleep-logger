package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSleepServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSleepRepository userSleepRepository;

    @Mock
    private UserSleepMapper userSleepMapper;

    @InjectMocks
    private UserSleepServiceImpl userSleepService;

    private static LocalDate currentDate;
    private static LocalDate oneMonthBeforeDate;

    @BeforeAll
    static void setUp() {
        currentDate = LocalDate.now();
        oneMonthBeforeDate = currentDate.minusMonths(1);
    }

    @Test
    @DisplayName("Should save user sleep successfully")
    void shouldSaveUserSleep() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        UserSleep userSleep = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        UserSleep expectedUserSleep = UserSleepGenerator.generateUserSleepWithoutUser();
        SleepDto sleepDto = UserSleepGenerator.generateSleepDto();
        userSleep.setUser(testUser);

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepMapper.mapSleepDtoToUserSleep(sleepDto)).thenReturn(userSleep);
        when(userSleepRepository.save(userSleep)).thenReturn(expectedUserSleep);

        long savedId = userSleepService.saveUserSleep(testUser.getId(), sleepDto);

        assertEquals(expectedUserSleep.getId(), savedId);
        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).save(userSleep);
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
        SleepDto expectedSleepDto = UserSleepGenerator.generateSleepDto();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDate(eq(testUser.getId()), any(Date.class))).thenReturn(Optional.of(userSleep));
        when(userSleepMapper.mapUserSleepToSleepDto(userSleep)).thenReturn(expectedSleepDto);

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
        verify(userSleepMapper, never()).mapUserSleepToSleepDto(any(UserSleep.class));

    }

    @Test
    @DisplayName("Should find all user sleep records within the last month")
    void shouldFindLastMonthSleep() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        UserSleep userSleep1 = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        userSleep1.setUser(testUser);
        UserSleep userSleep2 = UserSleepGenerator.generateUserSleepWithoutIdAndUser();
        userSleep2.setUser(testUser);
        List<UserSleep> userSleepList = Arrays.asList(userSleep1, userSleep2);

        SleepDto sleepDto1 = UserSleepGenerator.generateSleepDto();
        SleepDto sleepDto2 = UserSleepGenerator.generateSleepDto();
        List<SleepDto> expectedSleepDtoList = Arrays.asList(sleepDto1, sleepDto2);


        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)))).thenReturn(userSleepList);
        when(userSleepMapper.mapUserSleepListToSleepDtoList(userSleepList)).thenReturn(expectedSleepDtoList);

        List<SleepDto> actualSleepDtoList = userSleepService.findLastMonthSleepByUserId(testUser.getId());

        assertNotNull(actualSleepDtoList);
        assertEquals(expectedSleepDtoList.size(), actualSleepDtoList.size());

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)));
        verify(userSleepMapper).mapUserSleepListToSleepDtoList(userSleepList);
    }

    @Test
    @DisplayName("Should return an empty list when no sleep records are found within the last month")
    void shouldFindLastMonthSleep_whenNoSleepRecordsFound_returnEmptyList() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(startDate)), eq(Date.valueOf(endDate)))).thenReturn(Collections.emptyList());

        List<SleepDto> actualSleepDtoList = userSleepService.findLastMonthSleepByUserId(testUser.getId());

        assertNotNull(actualSleepDtoList);
        assertTrue(actualSleepDtoList.isEmpty());

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).findByUserIdAndCreatedDateBetween(eq(testUser.getId()), eq(Date.valueOf(startDate)), eq(Date.valueOf(endDate)));
    }

    @Test
    @DisplayName("Should throw NotFoundException when user is not found while fetching last month's sleep records")
    void shouldFindLastMonthSleep_whenUserNotFound_throwNotFoundException() {
        long userId = -1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSleepService.findLastMonthSleepByUserId(userId));
    }

    @Test
    @DisplayName("Should calculate the average sleep for the user within the last month")
    void shouldCalculateAverageSleepWithinLastMonthForUserById() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        AverageUserSleep averageUserSleep = UserSleepGenerator.generateAverageUserSleep();
        AverageSleepDto averageSleepDto = UserSleepGenerator.generateAverageSleepDtoWithinOneMonth();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), eq(Date.valueOf(oneMonthBeforeDate)), eq(Date.valueOf(currentDate)))).thenReturn(averageUserSleep);
        when(userSleepMapper.mapAverageUserSleepToAverageSleepDto(averageUserSleep)).thenReturn(averageSleepDto);

        AverageSleepDto actualAverageSleepDto = userSleepService.calculateAverageSleepWithinLastMonthForUserById(testUser.getId());

        assertNotNull(actualAverageSleepDto);
        assertEquals(averageSleepDto.getStartDate(), actualAverageSleepDto.getStartDate());
        assertEquals(averageSleepDto.getEndDate(), actualAverageSleepDto.getEndDate());

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
        verify(userSleepMapper, never()).mapAverageUserSleepToAverageSleepDto(any(AverageUserSleep.class));
    }
}