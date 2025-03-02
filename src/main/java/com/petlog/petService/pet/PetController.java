package com.petlog.petService.pet;

import com.petlog.petService.domain.Pets;
import com.petlog.petService.dto.CreatePetInfoRequestDto;
import com.petlog.userService.dto.ResponseMessage;
import com.petlog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final JwtUtil jwtUtil;

    // 펫 정보 등록
    @PostMapping("/createPetInfo")
    public ResponseEntity<ResponseMessage> createPetInfo (
            @RequestPart(value = "petImage", required = false) MultipartFile petImage,
            @RequestPart("petName") String petName,
            @RequestPart("petAnimal") String petAnimal,
            @RequestPart("petBirth") String petBirth,
            @RequestPart("petBreed") String petBreed,
            @RequestPart("petGender") String petGender,
            @RequestPart("petWeight") String petWeight,
            HttpServletRequest request) throws BadRequestException {

        int userId = extractUserIdFromToken(request);

        int createdPet = petService.createPetInfo(petImage, petName, petAnimal, petBirth, petBreed, petGender, petWeight, userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(createdPet)
                .statusCode(201)
                .resultMessage("User created successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // userId에 등록된 반려동물 가져오기
    @GetMapping("/getPetsById")
    public ResponseEntity<ResponseMessage> getPetsById (HttpServletRequest request){

        int userId = extractUserIdFromToken(request);

        List<Pets> pets= petService.getPetsById(userId);

        System.out.println("컨트롤러의 pets : " + pets);
        ResponseMessage response = ResponseMessage.builder()
                .data(pets)
                .statusCode(200)
                .resultMessage("반려동물 가져오기 성공!")
                .build();

        return ResponseEntity.status(200).body(response);
    }

    // 펫 상세정보 가져오기
    @GetMapping("/getPetDetail/{petId}")
    public ResponseEntity<ResponseMessage> getPetDetail (@PathVariable("petId") int petId, HttpServletRequest request){

        int userId = extractUserIdFromToken(request);

        Pets pet = petService.getPetDetail(userId, petId);

        ResponseMessage response = ResponseMessage.builder()
                .data(pet)
                .statusCode(200)
                .resultMessage("반려동물 가져오기 성공!")
                .build();

        return ResponseEntity.status(200).body(response);
    }

    // 토큰에서 유저 아이디 반환
    private int extractUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        token = token.replace("Bearer ", ""); // "Bearer " 제거
        Claims claims = jwtUtil.getUserInfoFromToken(token); // JWT에서 클레임 가져오기
        System.out.println("claims = " + claims);

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


}
