package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.service.UserSleepService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/sleep")
@RequiredArgsConstructor
public class UserSleepController {

    private final UserSleepService userSleepService;

    @PostMapping
    public long saveUserSleep(@NotNull @PathVariable("userId") Integer userId,
                              @NotNull @RequestBody SleepDto sleepDto) {
        return userSleepService.saveUserSleep(userId, sleepDto);
    }

    @GetMapping("/last-night")
    public SleepDto findLastNightSleepByUserId(@NotNull @PathVariable("userId") Integer userId) {
        return userSleepService.findLastNightSleepByUserId(userId);
    }

    @GetMapping("/last-month")
    public List<SleepDto> findLastMonthSleepByUserId(@NotNull @PathVariable("userId") Integer userId) {
        return userSleepService.findLastMonthSleepByUserId(userId);
    }

    @GetMapping("/last-month-average")
    public AverageSleepDto calculateLastMonthAverageSleepByUserId(@NotNull @PathVariable("userId") Integer userId) {
        return userSleepService.calculateAverageSleepWithinLastMonthForUserById(userId);
    }
}
