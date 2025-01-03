package com.noom.interview.fullstack.sleep.entity;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_sleep")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@SqlResultSetMapping(
        name = "averageUserSleepMapping",
        classes = {
                @ConstructorResult(
                        targetClass = AverageUserSleep.class,
                        columns = {
                                @ColumnResult(name = "avg_sleeping_time_in_minutes", type = Integer.class),
                                @ColumnResult(name = "avg_fall_asleep_time", type = java.util.Date.class),
                                @ColumnResult(name = "avg_wake_time", type = java.util.Date.class),
                                @ColumnResult(name = "good_count", type = Integer.class),
                                @ColumnResult(name = "ok_count", type = Integer.class),
                                @ColumnResult(name = "bad_count", type = Integer.class)
                        }
                )
        }
)
@NamedNativeQuery(
        name = "UserSleep.calculateUserAverageSleepWithinPeriod",
        query = "SELECT CAST(AVG(sleeping_time_in_minutes) as integer) AS avg_sleeping_time_in_minutes,\n" +
                "       AVG(fall_asleep_time)                          AS avg_fall_asleep_time,\n" +
                "       AVG(wake_up_time)                              AS avg_wake_time,\n" +
                "       COUNT(CASE WHEN feeling = 'GOOD' THEN 1 END)   AS good_count,\n" +
                "       COUNT(CASE WHEN feeling = 'OK' THEN 1 END)     AS ok_count,\n" +
                "       COUNT(CASE WHEN feeling = 'BAD' THEN 1 END)    AS bad_count\n" +
                "FROM user_sleep\n" +
                "WHERE user_id = :userId\n" +
                "  AND created_date BETWEEN :startDate AND :endDate",
        resultSetMapping = "averageUserSleepMapping")
public class UserSleep {

    @Id
    @Column(name = "id")
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

    @Column(name = "feeling")
    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    private Feeling feeling;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSleep userSleep = (UserSleep) o;
        return sleepingTimeInMinutes == userSleep.sleepingTimeInMinutes && Objects.equals(id, userSleep.id) && Objects.equals(createdDate, userSleep.createdDate) && Objects.equals(fallAsleepTime, userSleep.fallAsleepTime) && Objects.equals(wakeUpTime, userSleep.wakeUpTime) && feeling == userSleep.feeling;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, fallAsleepTime, wakeUpTime, sleepingTimeInMinutes, feeling);
    }

    @Override
    public String toString() {
        return "UserSleep{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", fallAsleepTime=" + fallAsleepTime +
                ", wakeUpTime=" + wakeUpTime +
                ", sleepingTimeInMinutes=" + sleepingTimeInMinutes +
                ", feeling=" + feeling +
                '}';
    }
}
