package com.noom.interview.fullstack.sleep.model;

import java.sql.Date;
import java.sql.Time;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AverageUserSleep {

    private Date date;

    private long avgMinutesInBed;

    private Time avgFallAsleepTime;

    private Time avgWakeTimeTime;

    private int goodCount;

    private int okCount;

    private int badCount;
}
