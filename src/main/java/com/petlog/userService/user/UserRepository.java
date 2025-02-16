package com.petlog.userService.user;

import com.petlog.userService.domain.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {
    Optional<Users> findByEmail(@Param("email") String email);
    int createUser(Users user);
}
