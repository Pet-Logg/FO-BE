package com.petlog.userService.dto;

import com.petlog.userService.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RefreshResponseDto {

    private String refreshToken;
    private String role;
    private  int userId;

}
