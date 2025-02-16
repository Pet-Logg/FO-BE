package com.petlog.userService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 지정하는 모든 필드에 대해 생성자 만들어줌
@NoArgsConstructor // 매개변수 없는 생성자 만들어줌
public class Users extends CommonEntity {

    private int userId;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String profileImg;

    private Role role;

    public enum Role {
        USER, ADMIN
    }

}
