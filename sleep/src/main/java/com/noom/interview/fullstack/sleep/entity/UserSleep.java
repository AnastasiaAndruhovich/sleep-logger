package com.noom.interview.fullstack.sleep.entity;

import com.noom.interview.fullstack.sleep.constant.Feeling;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_sleep")
public class UserSleep {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_tms")
    private Timestamp createdTms;

    @Column(name = "fall_asleep_tms")
    private Timestamp fallAsleepTms;

    @Column(name = "wake_up_tms")
    private Timestamp wakeUpTms;

    @Enumerated(EnumType.STRING)
    private Feeling feeling;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
