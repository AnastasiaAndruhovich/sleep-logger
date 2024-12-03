package com.noom.interview.fullstack.sleep.generator;

import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.time.LocalDate;
import org.instancio.Instancio;
import static org.instancio.Select.field;

public class UserSleepGenerator {

    public static UserSleep generateUserSleepWithoutUser() {
        UserSleep userSleep = Instancio.of(UserSleep.class)
                .generate(field(UserSleep::getId), gen -> gen.longs().range(1L, 10L))
                .ignore(field(UserSleep::getSleepingTimeInMinutes))
                .ignore(field(UserSleep::getUser))
                .create();
        userSleep.setSleepingTimeInMinutes((int) (Math.abs(userSleep.getWakeUpTime().getTime() - userSleep.getFallAsleepTime().getTime()) / 60000));
        return userSleep;
    }

    public static UserSleep generateUserSleepWithoutIdAndUser() {
        UserSleep userSleep = Instancio.of(UserSleep.class)
                .ignore(field(UserSleep::getId))
                .ignore(field(UserSleep::getSleepingTimeInMinutes))
                .ignore(field(UserSleep::getUser))
                .create();
        userSleep.setSleepingTimeInMinutes((int) (Math.abs(userSleep.getWakeUpTime().getTime() - userSleep.getFallAsleepTime().getTime()) / 60000));
        return userSleep;
    }

    public static SleepDto generateSleepDto() {
        return Instancio.of(SleepDto.class)
                .set(field(SleepDto::getCreatedDate), LocalDate.now())
                .generate(field(SleepDto::getTimeInBedInMinutes), gen -> gen.ints().range(0, 800))
                .create();
    }

    public static AverageUserSleep generateAverageUserSleep() {
        return Instancio.of(AverageUserSleep.class)
                .generate(field(AverageUserSleep::getAvgSleepingTimeInMinutes), gen -> gen.ints().range(0, 800))
                .generate(field(AverageUserSleep::getGoodCount), gen -> gen.ints().range(0, 10))
                .generate(field(AverageUserSleep::getOkCount), gen -> gen.ints().range(0, 10))
                .generate(field(AverageUserSleep::getBadCount), gen -> gen.ints().range(0, 10))
                .create();
    }

    public static AverageSleepDto generateAverageSleepDtoWithinOneMonth() {
        return Instancio.of(AverageSleepDto.class)
                .set(field(AverageSleepDto::getStartDate), LocalDate.now().minusMonths(1))
                .set(field(AverageSleepDto::getEndDate), LocalDate.now())
                .generate(field(AverageSleepDto::getAvgTimeInBedInMinutes), gen -> gen.ints().range(0, 800))
                .create();
    }
}
