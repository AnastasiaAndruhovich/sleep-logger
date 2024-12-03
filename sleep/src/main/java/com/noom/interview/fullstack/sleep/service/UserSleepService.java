package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import java.util.List;

public interface UserSleepService {

    long saveUserSleep(long userId, SleepDto sleepDto);

    SleepDto findLastNightSleepByUserId(long userId);

    List<SleepDto> findLastMonthSleepByUserId(long userId);


    AverageSleepDto calculateAverageSleepWithinLastMonthForUserById(long userId);
}
