package com.modfathers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
