package com.noom.interview.fullstack.sleep.entity;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_sleep")
@SqlResultSetMapping(
        name = "averageUserSleepMapping",
        classes = {
                @ConstructorResult(
                        targetClass = AverageUserSleep.class,
                        columns = {
                                @ColumnResult(name = "date"),
                                @ColumnResult(name = "avg_sleeping_time_in_minutes"),
                                @ColumnResult(name = "avg_fall_asleep_time"),
                                @ColumnResult(name = "avg_wake_time"),
                                @ColumnResult(name = "good_count"),
                                @ColumnResult(name = "ok_count"),
                                @ColumnResult(name = "bad_count")
                        }
                )
        }
)
@NamedNativeQuery(
        name = "UserSleep.calculateUserAverageSleepWithinPeriod",
        query = "SELECT AVG(sleeping_time_in_minutes)                AS avg_sleeping_time_in_minutes,\n" +
                "       AVG(fall_asleep_time)::time                  AS avg_fall_asleep_time,\n" +
                "       AVG(wake_up_time)::time                      AS avg_wake_time,\n" +
                "       COUNT(CASE WHEN feeling = 'GOOD' THEN 1 END) AS good_count,\n" +
                "       COUNT(CASE WHEN feeling = 'OK' THEN 1 END)   AS ok_count,\n" +
                "       COUNT(CASE WHEN feeling = 'BAD' THEN 1 END)  AS bad_count\n" +
                "FROM user_sleep\n" +
                "WHERE user_id = :user_id\n" +
                "  AND created_date BETWEEN :startDate AND :endDate",
        resultSetMapping = "averageUserSleepMapping")
public class UserSleep {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "fall_asleep_time", nullable = false)
    private Time fallAsleepTime;

    @Column(name = "wake_up_time", nullable = false)
    private Time wakeUpTime;

    @Column(name = "sleeping_time_in_minutes", nullable = false)
    private int sleepingTimeInMinutes;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
