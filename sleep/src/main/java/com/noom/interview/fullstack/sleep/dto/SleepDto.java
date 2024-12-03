package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noom.interview.fullstack.sleep.constant.Feeling;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Schema(description = "DTO representing a user's sleep information for a particular date.")
public class SleepDto {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    @Schema(description = "The date when the user recorded their sleep in MM/dd/yyyy format.", example = "12/01/2024")
    private LocalDate createdDate;

    @Min(0)
    @Schema(description = "The total time the user spent in bed in minutes.", example = "480")
    private int timeInBedInMinutes;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "hh:mm a")
    @Schema(description = "The time the user fell asleep in hh:mm a format.", example = "11:30 PM")
    private LocalTime fallAsleepTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "hh:mm a")
    @Schema(description = "The time the user woke up in hh:mm a format.", example = "6:30 AM")
    private LocalTime wakeUpTime;

    @NotNull
    @Schema(description = "The user's feeling when they woke up, represented by a Feeling enum.", example = "GOOD")
    private Feeling feeling;
}
