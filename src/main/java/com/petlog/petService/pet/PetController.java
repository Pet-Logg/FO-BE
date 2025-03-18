package com.petlog.petService.pet;

import com.petlog.petService.domain.Diary;
import com.petlog.petService.domain.Pets;
import com.petlog.petService.dto.CreateDiaryRequestDto;
import com.petlog.petService.dto.CreatePetRequestDto;
import com.petlog.petService.dto.UpdatePetRequestDto;
import com.petlog.petService.dto.UpdatePetResponseDto;
import com.petlog.userService.dto.ResponseMessage;
import com.petlog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            CreatePetRequestDto dto,
            HttpServletRequest request) throws BadRequestException {

        int userId = extractUserIdFromToken(request);

        int createdPet = petService.createPetInfo(dto, userId);
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

        UpdatePetResponseDto dto = petService.getPetDetail(userId, petId);

        ResponseMessage response = ResponseMessage.builder()
                .data(dto)
                .statusCode(200)
                .resultMessage("반려동물 가져오기 성공!")
                .build();

        return ResponseEntity.status(200).body(response);
    }

    // 펫 삭제
    @DeleteMapping("/{petId}")
    public ResponseEntity<ResponseMessage> deletePet (@PathVariable("petId") int petId, HttpServletRequest request){

        int userId = extractUserIdFromToken(request);

        petService.deletePet(userId, petId);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("반려동물 가져오기 성공!")
                .build();

        return ResponseEntity.status(200).body(response);
    }

    // 펫 수정
    @PostMapping(value = "/updatePet/{petId}")
    public ResponseEntity<ResponseMessage> updatePet (
            @PathVariable("petId") int petId,
            UpdatePetRequestDto dto,
            HttpServletRequest request) throws BadRequestException {

        int userId = extractUserIdFromToken(request);

        petService.updatePet(petId, dto, userId);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(201)
                .resultMessage("Pet update successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // 펫 다이어리 등록
    @PostMapping("/createDiary")
    private ResponseEntity<ResponseMessage> createDiary (CreateDiaryRequestDto dto, HttpServletRequest request) {

        int userId = extractUserIdFromToken(request);

        petService.createDiary(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(201)
                .resultMessage("Diary create successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // userId에 등록된 다이어리 가져오기
    @GetMapping("/getDiaryById")
    private ResponseEntity<ResponseMessage> getDiaryById (HttpServletRequest request) {

        int userId = extractUserIdFromToken(request);

        List<Diary> diary = petService.getDiaryById(userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(diary)
                .statusCode(201)
                .resultMessage("Diary create successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/getDiaryDetailById/{diaryId}")
    private ResponseEntity<ResponseMessage> getDiaryDetailById (@PathVariable("diaryId") int diaryId, HttpServletRequest request) {

        int userId = extractUserIdFromToken(request);

        List<Diary> diary = petService.getDiaryDetailById(userId, diaryId);

        ResponseMessage response = ResponseMessage.builder()
                .data(diary)
                .statusCode(201)
                .resultMessage("Diary create successfully")
                .build();

        return ResponseEntity.status(201).body(response);

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


}
