package com.petlog.userService.user;

import com.petlog.userService.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {
    Optional<Users> findByEmail(@Param("email") String email);
    int createUser(Users user);

    Optional<Users> findByUserId(@Param("userId") int userId);

    void savePassword (Users user);
}
