package com.noom.interview.fullstack.sleep.entity;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Date;
import java.sql.Timestamp;
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
                                @ColumnResult(name = "avg_minutes_in_bed"),
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
        name = "UserSleep.calculateUserAverageSleep",
        query = "SELECT DATE_TRUNC('day', fall_asleep_tms)                            AS date," +
                "       AVG(EXTRACT(EPOCH FROM (wake_up_tms - fall_asleep_tms))) / 60 AS avg_minutes_in_bed," +
                "       AVG(fall_asleep_tms::time)                                    AS avg_fall_asleep_time," +
                "       AVG(wake_up_tms::time)                                        AS avg_wake_time," +
                "       COUNT(CASE WHEN feeling = 'GOOD' THEN 1 END)                  AS good_count," +
                "       COUNT(CASE WHEN feeling = 'OK' THEN 1 END)                    AS ok_count," +
                "       COUNT(CASE WHEN feeling = 'BAD' THEN 1 END)                   AS bad_count " +
                "FROM user_sleep " +
                "WHERE user_id = :user_id" +
                "  AND fall_asleep_tms::time >= :startSleepWindow" +
                "  AND fall_asleep_tms::time <= :endSleepWindow" +
                "  AND fall_asleep_tms >= :startDateTms" +
                "  AND fall_asleep_tms <= :endDateTms " +
                "GROUP BY date",
        resultSetMapping = "averageUserSleepMapping")
public class UserSleep {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fall_asleep_tms")
    private Timestamp fallAsleepTms;

    @Column(name = "wake_up_tms")
    private Timestamp wakeUpTms;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
