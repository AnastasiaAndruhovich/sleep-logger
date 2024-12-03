package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noom.interview.fullstack.sleep.constant.Feeling;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Schema(description = "DTO representing the average sleep statistics for a user within a specific period.")
public class AverageSleepDto {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    @Schema(description = "The start date of the sleep period in MM/dd/yyyy format.", example = "12/01/2024")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    @Schema(description = "The end date of the sleep period in MM/dd/yyyy format.", example = "12/02/2024")
    private LocalDate endDate;

    @Min(0)
    @Schema(description = "The average time spent in bed (in minutes).", example = "480")
    private int avgTimeInBedInMinutes;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "hh:mm a")
    @Schema(description = "The average time the user falls asleep in hh:mm a format.", example = "10:30 PM")
    private LocalTime avgFallAsleepTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "hh:mm a")
    @Schema(description = "The average time the user wakes up in hh:mm a format.", example = "6:30 AM")
    private LocalTime avgWakeUpTime;

    @Schema(description = "The feeling statistics, mapping feelings to counts.", example = "{\"GOOD\": 10, \"OK\": 5}")
    private Map<Feeling, Integer> feelingStatistic;
}
