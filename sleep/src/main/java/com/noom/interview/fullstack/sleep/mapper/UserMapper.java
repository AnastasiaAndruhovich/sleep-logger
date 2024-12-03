package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.UserDto;
import com.noom.interview.fullstack.sleep.dto.UserWithEncodedIdDto;
import com.noom.interview.fullstack.sleep.entity.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto mapUserToUserDto(User user);

    @Mapping(target = "encodedId", source = "id", qualifiedByName = "encodeId")
    UserWithEncodedIdDto mapUserToUserWithEncodedIdDto(User user);

    List<UserWithEncodedIdDto> mapUserListToUserWithEncodedIdDtoList(List<User> users);

    @Named("encodeId")
    default String encodeId(Integer userId) {
        return userId == null ? null : Base64.getEncoder().encodeToString(String.valueOf(userId).getBytes(StandardCharsets.UTF_8));
    }

}
