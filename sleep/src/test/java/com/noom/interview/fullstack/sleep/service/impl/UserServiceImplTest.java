package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.exception.NotFoundException;
import com.noom.interview.fullstack.sleep.generator.UserGenerator;
import com.noom.interview.fullstack.sleep.mapper.UserMapper;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void shouldFindAllUsersWithEncodedIds() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        List<User> userList = Collections.singletonList(testUser);
        List<UserWithEncodedIdDto> expectedDtoList = userMapper.mapUserListToUserWithEncodedIdDtoList(userList);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserWithEncodedIdDto> actualDtoList = userService.findAllUsersWithEncodedIds();

        assertNotNull(actualDtoList);
        assertEquals(expectedDtoList.size(), actualDtoList.size());
        assertEquals(expectedDtoList.get(0).getEncodedId(), actualDtoList.get(0).getEncodedId());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldFindUserById_whenUserExists() {
        User testUser = UserGenerator.generateUserWithoutUserSleep();
        UserDto expectedDto = userMapper.mapUserToUserDto(testUser);

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UserDto result = userService.findUserById(testUser.getId());

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getEmail(), result.getEmail());

        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void shouldFindUserById_whenUserDoesNotExist_throwNotFoundException() {
        long userId = -1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }
}