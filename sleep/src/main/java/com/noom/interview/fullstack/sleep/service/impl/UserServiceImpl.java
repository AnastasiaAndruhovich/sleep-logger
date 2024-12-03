package com.noom.interview.fullstack.sleep.service.impl;

import com.noom.interview.fullstack.sleep.constant.MessageKey;
import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.exception.NotFoundException;
import com.noom.interview.fullstack.sleep.mapper.UserMapper;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserWithEncodedIdDto> findAllUsersWithEncodedIds() {
        List<User> userList = userRepository.findAll();
        return userMapper.mapUserListToUserWithEncodedIdDtoList(userList);
    }

    @Override
    public UserDto findUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageKey.ERROR_NOT_FOUND_BY_ID.getName(), new Object[]{id}));
        return userMapper.mapUserToUserDto(user);
    }
}
