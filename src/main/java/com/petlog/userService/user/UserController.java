package com.petlog.userService.user;

import com.petlog.userService.dto.ResponseMessage;
import com.petlog.userService.dto.UserCommonDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createUser (@RequestBody UserCommonDto userCommonDto) throws BadRequestException {
        int createdUser = userService.createUser(userCommonDto);

        ResponseMessage response = ResponseMessage.builder()
                .data(createdUser)
                .statusCode(201)
                .resultMessage("User created successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserCommonDto userCommonDto, HttpServletResponse response) throws BadRequestException {
        String token = userService.login(userCommonDto);

        // 쿠키 생성
//        String base64Token = Base64.getEncoder().encodeToString(("Bearer " + token).getBytes());
//        Cookie cookie = new Cookie("Authorization", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(false); // HTTPS를 사용하는 경우에만 설정
//        cookie.setPath("/");
//        cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (1시간)

        // ✅ SameSite 설정 추가
//        cookie.setAttribute("SameSite", "None");

//         응답에 쿠키 추가
//        response.addCookie(cookie);

        ResponseMessage responseMessage = ResponseMessage.builder()
                .data(token) // 토큰 다시 준거
                .statusCode(200)
                .resultMessage("Login successful")
                .build();

        return ResponseEntity.ok(responseMessage);
    }

}
