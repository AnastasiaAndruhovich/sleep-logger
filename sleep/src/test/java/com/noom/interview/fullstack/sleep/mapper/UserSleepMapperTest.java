package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import com.noom.interview.fullstack.sleep.dto.AverageSleepDto;
import com.noom.interview.fullstack.sleep.dto.SleepDto;
import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.generator.UserSleepGenerator;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class UserSleepMapperTest {

    private final UserSleepMapper mapper = Mappers.getMapper(UserSleepMapper.class);

    @Test
    @DisplayName("Should map SleepDto to UserSleep")
    void shouldMapSleepDtoToUserSleep() {
        SleepDto sleepDto = UserSleepGenerator.generateSleepDto();

        UserSleep userSleep = mapper.mapSleepDtoToUserSleep(sleepDto);

        assertNotNull(userSleep);
        assertNull(userSleep.getId());
        assertEquals(Date.valueOf(sleepDto.getCreatedDate()), userSleep.getCreatedDate());
        assertEquals(Time.valueOf(sleepDto.getFallAsleepTime()), userSleep.getFallAsleepTime());
        assertEquals(Time.valueOf(sleepDto.getWakeUpTime()), userSleep.getWakeUpTime());
        assertEquals(sleepDto.getTimeInBedInMinutes(), userSleep.getSleepingTimeInMinutes());
        assertNull(userSleep.getUser());
    }

    @Test
    @DisplayName("Should map UserSleep to SleepDto")
    void shouldMapUserSleepToSleepDto() {
        UserSleep userSleep = UserSleepGenerator.generateUserSleepWithoutUser();

        SleepDto sleepDto = mapper.mapUserSleepToSleepDto(userSleep);

        assertNotNull(sleepDto);
        assertEquals(userSleep.getCreatedDate().toLocalDate(), sleepDto.getCreatedDate());
        assertEquals(userSleep.getSleepingTimeInMinutes(), sleepDto.getTimeInBedInMinutes());
        assertEquals(userSleep.getFallAsleepTime().toLocalTime(), sleepDto.getFallAsleepTime());
        assertEquals(userSleep.getWakeUpTime().toLocalTime(), sleepDto.getWakeUpTime());
    }

    @Test
    @DisplayName("Should map list of UserSleep to list of SleepDto")
    void shouldMapUserSleepListToSleepDtoList() {
        List<UserSleep> userSleepList = List.of(
                UserSleepGenerator.generateUserSleepWithoutUser(),
                UserSleepGenerator.generateUserSleepWithoutUser()
        );

        List<SleepDto> sleepDtoList = mapper.mapUserSleepListToSleepDtoList(userSleepList);

        assertNotNull(sleepDtoList);
        assertEquals(userSleepList.size(), sleepDtoList.size());
    }

    @Test
    @DisplayName("Should map AverageUserSleep to AverageSleepDto")
    void shouldMapAverageUserSleepToAverageSleepDto() {
        AverageUserSleep averageUserSleep = UserSleepGenerator.generateAverageUserSleep();

        AverageSleepDto averageSleepDto = mapper.mapAverageUserSleepToAverageSleepDto(averageUserSleep);

        assertNotNull(averageSleepDto);
        assertEquals(averageUserSleep.getAvgSleepingTimeInMinutes(), averageSleepDto.getAvgTimeInBedInMinutes());
        Map<Feeling, Integer> feelingStatistic = averageSleepDto.getFeelingStatistic();
        assertNotNull(feelingStatistic);
        assertEquals(averageUserSleep.getGoodCount(), feelingStatistic.get(Feeling.GOOD));
        assertEquals(averageUserSleep.getOkCount(), feelingStatistic.get(Feeling.OK));
        assertEquals(averageUserSleep.getBadCount(), feelingStatistic.get(Feeling.BAD));
    }

    @Test
    @DisplayName("Should return correct map for feeling statistics")
    void shouldMapFeelingStatistic() {
        AverageUserSleep averageUserSleep = UserSleepGenerator.generateAverageUserSleep();

        Map<Feeling, Integer> feelingStatistic = mapper.mapFeelingStatistic(averageUserSleep);

        assertNotNull(feelingStatistic);
        assertEquals(averageUserSleep.getGoodCount(), feelingStatistic.get(Feeling.GOOD));
        assertEquals(averageUserSleep.getOkCount(), feelingStatistic.get(Feeling.OK));
        assertEquals(averageUserSleep.getBadCount(), feelingStatistic.get(Feeling.BAD));
    }
}
