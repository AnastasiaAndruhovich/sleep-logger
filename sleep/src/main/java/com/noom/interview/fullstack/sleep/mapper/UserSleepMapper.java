package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {Date.class, Time.class})
public interface UserSleepMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(Date.valueOf(sleepDto.getCreatedDate()))")
    @Mapping(target = "fallAsleepTime", expression = "java(Time.valueOf(sleepDto.getFallAsleepTime()))")
    @Mapping(target = "wakeUpTime", expression = "java(Time.valueOf(sleepDto.getWakeUpTime()))")
    @Mapping(target = "sleepingTimeInMinutes", source = "timeInBedInMinutes")
    @Mapping(target = "user", ignore = true)
    UserSleep mapSleepDtoToUserSleep(SleepDto sleepDto);

    @Mapping(target = "createdDate", expression = "java(userSleep.getCreatedDate().toLocalDate())")
    @Mapping(target = "timeInBedInMinutes", source = "sleepingTimeInMinutes")
    @Mapping(target = "fallAsleepTime", expression = "java(userSleep.getFallAsleepTime().toLocalTime())")
    @Mapping(target = "wakeUpTime", expression = "java(userSleep.getWakeUpTime().toLocalTime())")
    SleepDto mapUserSleepToSleepDto(UserSleep userSleep);

    List<SleepDto> mapUserSleepListToSleepDtoList(List<UserSleep> userSleepList);

    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "avgTimeInBedInMinutes", source = "avgSleepingTimeInMinutes")
    @Mapping(target = "feelingStatistic", source = ".", qualifiedByName = "mapFeelingStatistic")
    AverageSleepDto mapAverageUserSleepToAverageSleepDto(AverageUserSleep averageUserSleep);

    @Named("mapFeelingStatistic")
    default Map<Feeling, Integer> mapFeelingStatistic(AverageUserSleep averageUserSleep) {
        return Map.of(Feeling.GOOD, averageUserSleep.getGoodCount(),
                Feeling.OK, averageUserSleep.getOkCount(),
                Feeling.BAD, averageUserSleep.getBadCount());
    }
}
