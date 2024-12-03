package com.noom.interview.fullstack.sleep.generator;

import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import com.noom.interview.fullstack.sleep.entity.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.instancio.Instancio;
import static org.instancio.Select.field;

public class UserGenerator {

    private static final String EMAIL_PATTERN = "#a#a#a#a#a#a@example.com";

    private static final int MIN_STRING_LENGTH = 1;

    private static final int MAX_STRING_LENGTH = 40;


    public static User generateUserWithoutUserSleep() {
        return Instancio.of(User.class)
                .generate(field(User::getId), gen -> gen.longs().range(1L, 10L))
                .ignore(field(User::getUserSleeps))
                .generate(field(User::getName), gen -> gen.string().minLength(MIN_STRING_LENGTH).maxLength(MAX_STRING_LENGTH))
                .generate(field(User::getEmail), gen -> gen.text().pattern(EMAIL_PATTERN))
                .create();
    }

    public static User generateUserWithoutIdAndUserSleep() {
        return Instancio.of(User.class)
                .ignore(field(User::getId))
                .ignore(field(User::getUserSleeps))
                .generate(field(User::getName), gen -> gen.string().minLength(MIN_STRING_LENGTH).maxLength(MAX_STRING_LENGTH))
                .generate(field(User::getEmail), gen -> gen.text().pattern(EMAIL_PATTERN))
                .create();
    }

    public static UserWithEncodedIdDto generateUserWithEncodedIdDto(long userId) {
        return Instancio.of(UserWithEncodedIdDto.class)
                .set(field(UserWithEncodedIdDto::getId), userId)
                .generate(field(UserWithEncodedIdDto::getName), gen -> gen.string().minLength(MIN_STRING_LENGTH).maxLength(MAX_STRING_LENGTH))
                .generate(field(UserWithEncodedIdDto::getEmail), gen -> gen.text().pattern(EMAIL_PATTERN))
                .set(field(UserWithEncodedIdDto::getEncodedId), Base64.getEncoder().encodeToString(String.valueOf(userId).getBytes(StandardCharsets.UTF_8)))
                .create();

    }

    public static UserDto generateUserDto(long userId) {
        return Instancio.of(UserDto.class)
                .set(field(UserDto::getId), userId)
                .generate(field(UserDto::getName), gen -> gen.string().minLength(MIN_STRING_LENGTH).maxLength(MAX_STRING_LENGTH))
                .generate(field(UserDto::getEmail), gen -> gen.text().pattern(EMAIL_PATTERN))
                .create();
    }
}
