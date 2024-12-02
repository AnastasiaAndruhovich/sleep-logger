package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noom.interview.fullstack.sleep.constant.Feeling;
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
public class AverageSleepDto {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Min(0)
    private int avgTimeInBedInMinutes;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime avgFallAsleepTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime avgWakeUpTime;

    private Map<Feeling, Integer> feelingStatistic;
}