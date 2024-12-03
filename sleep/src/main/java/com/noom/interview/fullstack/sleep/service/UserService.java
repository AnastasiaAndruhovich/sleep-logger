package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import java.util.List;

public interface UserService {

    List<UserWithEncodedIdDto> findAllUsersWithEncodedIds();

    UserDto findUserById(long id);
}
