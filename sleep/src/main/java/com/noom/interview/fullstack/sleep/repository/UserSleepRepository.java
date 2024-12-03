package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.UserSleep;
import com.noom.interview.fullstack.sleep.model.AverageUserSleep;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSleepRepository extends JpaRepository<UserSleep, Long> {

    Optional<UserSleep> findByUserIdAndCreatedDate(@Param("userId") Long userId, @Param("createdDate") Date createdDate);

    List<UserSleep> findByUserIdAndCreatedDateBetween(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(nativeQuery = true)
    AverageUserSleep calculateUserAverageSleepWithinPeriod(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
