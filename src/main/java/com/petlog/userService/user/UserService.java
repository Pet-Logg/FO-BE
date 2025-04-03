package com.petlog.userService.user;

import com.petlog.userService.dto.RefreshResponseDto;
import com.petlog.userService.entity.Users;
import com.petlog.userService.dto.ChangePasswordRequestDto;
import com.petlog.userService.dto.UserCommonDto;
import com.petlog.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    public int createUser(UserCommonDto userCommonDto) throws BadRequestException {

        // 아이디 중복체크
        if (idCheck(userCommonDto.getEmail()).isPresent()) {
            throw new BadRequestException("이미 사용중인 이메일 입니다.");
        }

        // 비밀번호 유효성 검사
        String password = userCommonDto.getPassword();
        if (!isPasswordValid(password)) {
            throw new BadRequestException("비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.");
        }

        Users user = new Users();
        user.setEmail(userCommonDto.getEmail());
        user.setRole(Users.Role.USER);
        user.setPassword(passwordEncoder.encode(userCommonDto.getPassword())); // 비밀번호 암호화
        userRepository.createUser(user);
        return user.getId();
    }

    // 로그인
    public ResponseCookie login(UserCommonDto userCommonDto, HttpServletResponse response) throws BadRequestException {
        Optional<Users> user = idCheck(userCommonDto.getEmail());

        if (user.isEmpty()) {
            throw new BadRequestException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String token = null;

        // 비밀번호 검증
        if (passwordEncoder.matches(userCommonDto.getPassword(), user.get().getPassword())) {
            String accessToken = jwtUtil.createAccessToken(user.get().getId(), user.get().getRole().toString());
            String refreshToken = jwtUtil.createRefreshToken(user.get().getId());

            userRepository.saveRefreshToken(user.get().getId(), refreshToken);

            Map<String,String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            ResponseCookie cookie = ResponseCookie.from("Authorization", tokens.get("accessToken"))
                    .domain(".petlog.store")
                    .httpOnly(false)
                    .secure(true)
                    .path("/")
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                    .domain(".petlog.store")
                    .httpOnly(false)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", refreshCookie.toString());

            return cookie;
        }

        throw new BadRequestException("아이디 또는 비밀번호가 잘못되었습니다.");
    }

    public Cookie refreshAccessToken(String refreshToken, HttpServletResponse response) {

        // 1. Refresh Token 유무 검증
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token missing");
        }

        // 2. 리프레시 토큰 유효성 검사
        jwtUtil.getUserInfoFromToken(refreshToken);

        // 3. DB에서 Refresh Token 조회
        RefreshResponseDto storedToken = userRepository.findRefreshToken(refreshToken);

        if (storedToken == null) {
            throw new IllegalArgumentException("Refresh token not found");
        }

        // 3. 토큰 일치 여부 확인
        if (!refreshToken.equals(storedToken.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        // 4. 새 Access Token 생성
        String newAccessToken = jwtUtil.createAccessToken(storedToken.getUserId(), storedToken.getRole());

        // 5. 새 Access Token 쿠키 설정
        Cookie newAccessTokenCookie = new Cookie("Authorization", newAccessToken);
        newAccessTokenCookie.setHttpOnly(false);
        newAccessTokenCookie.setSecure(false);
        newAccessTokenCookie.setPath("/");
        response.addCookie(newAccessTokenCookie);


        return newAccessTokenCookie;
    }

    public void deleteToken(int userId){
        userRepository.deleteToken(userId);
    }

    // 회원 비밀번호 수정
    public void changePassword(int userId, ChangePasswordRequestDto dto) throws BadRequestException {
        Optional<Users> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                // 비밀번호 검증
                String password = dto.getPassword();
                if (!isPasswordValid(password)) {
                    throw new BadRequestException("Password does not meet the security requirements");
                }

                user.setPassword(passwordEncoder.encode(dto.getPassword()));  // 비밀번호 암호화
            }

            userRepository.savePassword(user);

        } else {
            throw new RuntimeException("User not found with id " + userId);
        }
    }

    // 이메일 존재 확인
    public Optional<Users> idCheck(String email) {
        return userRepository.findByEmail(email);
    }

    // 비밀번호 유효성 검사
    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*?_])[A-Za-z\\d!@#$%^&*?_]{8,16}$";
        return password.matches(passwordPattern);
    }

}


