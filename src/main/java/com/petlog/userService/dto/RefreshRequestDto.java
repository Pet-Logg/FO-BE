package com.petlog.userService.dto;

import lombok.Data;

@Data
public class RefreshRequestDto {
    private String role;
    private int userId;
}
