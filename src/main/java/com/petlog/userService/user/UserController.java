package com.petlog.userService.user;

import com.petlog.userService.dto.ChangePasswordRequestDto;
import com.petlog.userService.dto.ResponseMessage;
import com.petlog.userService.dto.UserCommonDto;
import com.petlog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "OK";
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody UserCommonDto userCommonDto) throws BadRequestException {
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
    public ResponseEntity<ResponseMessage> login(
            @RequestBody UserCommonDto userCommonDto,
            HttpServletResponse response) throws BadRequestException {

        Cookie accessTokenCookie = userService.login(userCommonDto, response);

        ResponseMessage responseMessage = ResponseMessage.builder()
                .data(accessTokenCookie) // 토큰 다시 준거
                .statusCode(200)
                .resultMessage("Login successful")
                .build();

        return ResponseEntity.ok(responseMessage);
    }

    // Refresh Token으로 Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<ResponseMessage> refreshAccessToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        // 토큰 유효성 검사 및 새 Access Token 발급
        Cookie newAccessTokenCookie = userService.refreshAccessToken(refreshToken, response);

        return ResponseEntity.ok(ResponseMessage.builder()
                .data(newAccessTokenCookie)
                .statusCode(200)
                .resultMessage("Token refreshed")
                .build());
    }

    @PostMapping("logout")
    public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {

        int userId = extractUserIdFromToken(request);

        userService.deleteToken(userId);

        //  Token 쿠키 삭제
        deleteCookie(response, "Authorization");
        deleteCookie(response, "refreshToken");

        return ResponseEntity.ok(ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Logiout successful")
                .build());
    }

    // 회원 비밀번호 수정
    @PostMapping("/changePassword")
    public ResponseEntity<ResponseMessage> changePassword(
            @RequestBody ChangePasswordRequestDto dto, HttpServletRequest request) throws BadRequestException {

        int userId = extractUserIdFromToken(request);
        userService.changePassword(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("User updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // 토큰에서 유저 아이디 반환
    private int extractUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        token = token.replace("Bearer ", ""); // "Bearer " 제거
        Claims claims = jwtUtil.getUserInfoFromToken(token); // JWT에서 클레임 가져오기

        Object userIdObject = claims.get("userId");
        if (userIdObject == null) {
            throw new RuntimeException("User ID not found in token claims");
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdObject.toString()); // 숫자로 변환
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid user ID format in token");
        }

        return userId;
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
