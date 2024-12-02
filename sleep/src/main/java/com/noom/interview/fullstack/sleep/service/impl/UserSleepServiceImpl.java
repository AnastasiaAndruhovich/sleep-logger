package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.constant.MessageKey;
import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.exception.NotFoundException;
import com.noom.interview.fullstack.sleep.mapper.UserSleepMapper;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.repository.UserSleepRepository;
import com.noom.interview.fullstack.sleep.service.UserSleepService;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSleepServiceImpl implements UserSleepService {

    private final UserRepository userRepository;

    private final UserSleepRepository userSleepRepository;

    private final UserSleepMapper userSleepMapper;

    @Override
    public long saveUserSleep(long userId, SleepDto sleepDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ERROR_NOT_FOUND_BY_ID.getName(), new Object[]{userId}));
        UserSleep userSleep = userSleepMapper.mapSleepDtoToUserSleep(sleepDto);
        userSleep.setUser(user);

        return userSleepRepository.save(userSleep).getId();
    }

    @Override
    public SleepDto findLastNightSleepByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ERROR_NOT_FOUND_BY_ID.getName(), new Object[]{userId}));

        Date createdDate = new Date(Instant.now().toEpochMilli());
        return userSleepRepository.findByUserIdAndCreatedDate(userId, createdDate)
                .map(userSleepMapper::mapUserSleepToSleepDto)
                .orElse(null);
    }

    @Override
    public AverageSleepDto calculateAverageSleepWithinLastMonthForUserById(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ERROR_NOT_FOUND_BY_ID.getName(), new Object[]{userId}));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        AverageUserSleep averageUserSleep = userSleepRepository.calculateUserAverageSleepWithinPeriod(userId, Date.valueOf(startDate), Date.valueOf(endDate));
        if (averageUserSleep.getAvgSleepingTimeInMinutes() == null) {
            return null;
        }
        AverageSleepDto averageSleepDto = userSleepMapper.mapAverageUserSleepToAverageSleepDto(averageUserSleep);
        averageSleepDto.setStartDate(startDate);
        averageSleepDto.setEndDate(endDate);

        return averageSleepDto;
    }
}