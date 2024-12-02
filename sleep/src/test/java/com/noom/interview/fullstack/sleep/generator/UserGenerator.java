package com.noom.interview.fullstack.sleep.generator;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import org.instancio.Instancio;
import static org.instancio.Select.field;

public class UserGenerator {

    public static User generateUserWithoutIdAndUserSleep() {
        return Instancio.of(User.class)
                .ignore(field(User::getId))
                .ignore(field(User::getUserSleeps))
                .generate(field(User::getName), gen -> gen.string().minLength(1).maxLength(40))
                .generate(field(User::getEmail), gen -> gen.text().pattern("#a#a#a#a#a#a@example.com"))
                .create();
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
}
