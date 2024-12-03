package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.service.UserSleepService;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/sleep")
@RequiredArgsConstructor
public class UserSleepController {

    private final UserSleepService userSleepService;

    @GetMapping("/last-night")
    public SleepDto findLastNightSleepByUserId(@NotNull @PathVariable("userId") Integer userId) {
        return userSleepService.findLastNightSleepByUserId(userId);
    }

    @GetMapping("/last-month-average")
    public AverageSleepDto calculateLastMonthAverageSleepByUserId(@NotNull @PathVariable("userId") Integer userId) {
        return userSleepService.calculateAverageSleepWithinLastMonthForUserById(userId);
    }
}
