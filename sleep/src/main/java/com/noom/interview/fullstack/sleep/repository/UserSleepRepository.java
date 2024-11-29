package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSleepRepository extends JpaRepository<UserSleep, Long> {

    @Query("select us from UserSleep us where us.user.id = :userId and us.fallAsleepTms >= :startSleepWindow and us.fallAsleepTms <= :endSleepWindow")
    Optional<UserSleep> findByUserIdAndSleepWindow(@Param("userId") Long userId, @Param("startSleepWindow") Timestamp startSleepWindow, @Param("endSleepWindow") Timestamp endSleepWindow);

    AverageUserSleep calculateUserAverageSleep(@Param("userId") Long userId, @Param("startDateTms") Timestamp startDateTms, @Param("endDateTms") Timestamp endDateTms, @Param("startSleepWindow") Time startSleepWindow, @Param("endSleepWindow") Time endSleepWindow);

}
