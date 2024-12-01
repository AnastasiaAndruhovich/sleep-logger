package com.noom.interview.fullstack.sleep.model;

import java.sql.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AverageUserSleep {

    private long avgSleepingTimeInMinutes;

    private Time avgFallAsleepTime;

    private Time avgWakeTimeTime;

    private int goodCount;

    private int okCount;

    private int badCount;
}
