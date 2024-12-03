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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
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
    void shouldSaveUserSleep_whenUserNotFound_throwNotFoundException() {
        long userId = -1L;
        SleepDto sleepDto = UserSleepGenerator.generateSleepDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSleepService.saveUserSleep(userId, sleepDto));

        verify(userRepository).findById(userId);
        verify(userSleepRepository, never()).save(any(UserSleep.class));
    }

    @Test
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
    void shouldCalculateAverageSleepWithinLastMonthForUserById() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        AverageUserSleep averageUserSleep = UserSleepGenerator.generateAverageUserSleep();
        AverageSleepDto averageSleepDto = UserSleepGenerator.generateAverageSleepDtoWithinOneMonth();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), any(Date.class), any(Date.class))).thenReturn(averageUserSleep);
        when(userSleepMapper.mapAverageUserSleepToAverageSleepDto(averageUserSleep)).thenReturn(averageSleepDto);

        AverageSleepDto actualAverageSleepDto = userSleepService.calculateAverageSleepWithinLastMonthForUserById(testUser.getId());

        assertNotNull(actualAverageSleepDto);
        assertEquals(averageSleepDto.getStartDate(), actualAverageSleepDto.getStartDate());
        assertEquals(averageSleepDto.getEndDate(), actualAverageSleepDto.getEndDate());

        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), any(Date.class), any(Date.class));
    }

    @Test
    void shouldCalculateAverageSleepWithinLastMonthForUserById_whenNoAverageDataFound_returnNull() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userSleepRepository.calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), any(Date.class), any(Date.class))).thenReturn(new AverageUserSleep());

        AverageSleepDto expectedAverageSleepDto = userSleepService.calculateAverageSleepWithinLastMonthForUserById(testUser.getId());

        assertNull(expectedAverageSleepDto);
        verify(userRepository).findById(testUser.getId());
        verify(userSleepRepository).calculateUserAverageSleepWithinPeriod(eq(testUser.getId()), any(Date.class), any(Date.class));
        verify(userSleepMapper, never()).mapAverageUserSleepToAverageSleepDto(any(AverageUserSleep.class));
    }
}