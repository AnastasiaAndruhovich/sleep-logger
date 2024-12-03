package com.noom.interview.fullstack.sleep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO representing a user with an encoded ID.")
public class UserWithEncodedIdDto extends UserDto {

    @Schema(description = "An encoded ID associated with the user.", example = "MTY=")
    private String encodedId;
}
