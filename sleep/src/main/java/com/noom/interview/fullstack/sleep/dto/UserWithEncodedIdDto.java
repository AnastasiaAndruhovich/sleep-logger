package com.noom.interview.fullstack.sleep.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserWithEncodedIdDto extends UserDto {
    private String encodedUserId;
}
