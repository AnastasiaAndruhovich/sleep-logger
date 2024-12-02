package com.noom.interview.fullstack.sleep.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer userId;

    @NotEmpty
    @Size(min = 1, max = 40)
    private String email;

    @NotEmpty
    @Size(min = 1, max = 40)
    private String name;
}
