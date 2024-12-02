package com.noom.interview.fullstack.sleep.model;

import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AverageUserSleep {

    private long avgSleepingTimeInMinutes;

    private Time avgFallAsleepTime;

    private Time avgWakeTimeTime;

    private int goodCount;

    private int okCount;

    private int badCount;
}
