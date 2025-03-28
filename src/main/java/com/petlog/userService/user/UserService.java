package com.petlog.userService.user;

import com.petlog.userService.entity.Users;
import com.petlog.userService.dto.ChangePasswordRequestDto;
import com.petlog.userService.dto.UserCommonDto;
import com.petlog.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            System.out.println("아이디 중복체크 완");
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
    public String login(UserCommonDto userCommonDto) throws BadRequestException {
        Optional<Users> user = idCheck(userCommonDto.getEmail());

        if (user.isEmpty()) {
            throw new BadRequestException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String token = null;

        // 비밀번호 검증
        if (passwordEncoder.matches(userCommonDto.getPassword(), user.get().getPassword())) {
            token = jwtUtil.createToken(user.get().getId(), user.get().getRole().toString());

            return token;
        }

        throw new BadRequestException("아이디 또는 비밀번호가 잘못되었습니다.");
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

            System.out.println("user : " + user);
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


