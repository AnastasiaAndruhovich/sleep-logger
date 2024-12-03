package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.generator.UserGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Should correctly map User to UserDto")
    void shouldMapUserToUserDto() {
        User user = UserGenerator.generateUserWithoutUserSleep();

        UserDto userDto = userMapper.mapUserToUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    @DisplayName("Should correctly map User to UserWithEncodedIdDto and encode the ID")
    void shouldMapUserToUserWithEncodedIdDto() {
        User user = UserGenerator.generateUserWithoutUserSleep();
        String expectedEncodedId = Base64.getEncoder().encodeToString(String.valueOf(user.getId()).getBytes(StandardCharsets.UTF_8));

        UserWithEncodedIdDto userWithEncodedIdDto = userMapper.mapUserToUserWithEncodedIdDto(user);

        assertNotNull(userWithEncodedIdDto);
        assertEquals(user.getId(), userWithEncodedIdDto.getId());
        assertEquals(user.getName(), userWithEncodedIdDto.getName());
        assertEquals(user.getEmail(), userWithEncodedIdDto.getEmail());
        assertEquals(expectedEncodedId, userWithEncodedIdDto.getEncodedId());
    }

    @Test
    @DisplayName("Should correctly map a list of Users to a list of UserWithEncodedIdDto")
    void shouldMapUserListToUserWithEncodedIdDtoList() {
        List<User> userList = List.of(
                UserGenerator.generateUserWithoutUserSleep(),
                UserGenerator.generateUserWithoutUserSleep()
        );

        List<UserWithEncodedIdDto> userWithEncodedIdDtoList = userMapper.mapUserListToUserWithEncodedIdDtoList(userList);

        assertNotNull(userWithEncodedIdDtoList);
        assertEquals(userList.size(), userWithEncodedIdDtoList.size());
    }

    @Test
    @DisplayName("Should return encoded ID when encodeId is called with a valid ID")
    void shouldEncodeId() {
        long userId = 123L;
        String expectedEncodedId = Base64.getEncoder().encodeToString(String.valueOf(userId).getBytes(StandardCharsets.UTF_8));

        String actualEncodedId = userMapper.encodeId(userId);

        assertNotNull(actualEncodedId);
        assertEquals(expectedEncodedId, actualEncodedId);
    }

    @Test
    @DisplayName("Should return null when encodeId is called with a null ID")
    void shouldEncodeId_whenIdIsNull_returnNull() {
        String actualEncodedId = userMapper.encodeId(null);

        assertNull(actualEncodedId);
    }
}

