package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import com.noom.interview.fullstack.sleep.service.UserService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserWithEncodedIdDto> findAllUsersWithEncodedIds() {
        return userService.findAllUsersWithEncodedIds();
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@Valid @NotNull @PathVariable("userId") Integer userId) {
        return userService.findUserById(userId);
    }

}
