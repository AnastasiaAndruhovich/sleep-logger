package com.noom.interview.fullstack.sleep.model;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AverageUserSleep {

    private Integer avgSleepingTimeInMinutes;

    private LocalTime avgFallAsleepTime;

    private LocalTime avgWakeUpTime;

    private int goodCount;

    private int okCount;

    private int badCount;

    public AverageUserSleep(
            Integer avgSleepingTimeInMinutes,
            Date avgFallAsleepTime,
            Date avgWakeUpTime,
            int goodCount,
            int okCount,
            int badCount
    ) {
        this.avgSleepingTimeInMinutes = avgSleepingTimeInMinutes;
        this.avgFallAsleepTime = LocalTime.ofInstant(avgFallAsleepTime.toInstant(), ZoneId.systemDefault());
        this.avgWakeUpTime = LocalTime.ofInstant(avgWakeUpTime.toInstant(), ZoneId.systemDefault());
        this.goodCount = goodCount;
        this.okCount = okCount;
        this.badCount = badCount;
    }
}
