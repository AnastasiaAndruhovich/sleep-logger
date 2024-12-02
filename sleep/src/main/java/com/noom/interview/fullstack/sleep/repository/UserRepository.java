package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
