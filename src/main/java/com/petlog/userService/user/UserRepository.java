package com.petlog.userService.user;

import com.petlog.userService.dto.RefreshResponseDto;
import com.petlog.userService.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Ref;
import java.util.Optional;

@Mapper
public interface UserRepository {

    // 이메일로 유저찾기
    Optional<Users> findByEmail(@Param("email") String email);

    // 유저 생성
    int createUser(Users user);

    // 유저아이디로 유저찾기
    Optional<Users> findByUserId(@Param("userId") int userId);

    // 비밀번호 변경
    void savePassword (Users user);

    // 리프레시토큰 저장
    void saveRefreshToken (@Param("userId") int userId, @Param("refreshToken") String refreshToken );

    // userId 리프레시 토큰 조회
    RefreshResponseDto findRefreshToken(String refreshToken);

    //로그아웃
    void deleteToken(int userId);
}
