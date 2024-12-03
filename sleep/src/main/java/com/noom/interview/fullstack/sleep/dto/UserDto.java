package com.noom.interview.fullstack.sleep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO representing a user's information (without an encoded ID).")
public class UserDto {

    @Schema(description = "The unique identifier of the user.", example = "12345")
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 40)
    @Schema(description = "The user's email address.", example = "john.doe@example.com")
    private String email;

    @NotEmpty
    @Size(min = 1, max = 40)
    @Schema(description = "The user's full name.", example = "John Doe")
    private String name;
}
