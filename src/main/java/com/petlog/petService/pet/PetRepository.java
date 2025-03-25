package com.petlog.petService.pet;

import com.petlog.petService.entity.Diary;
import com.petlog.petService.entity.DiaryImages;
import com.petlog.petService.entity.Pets;
import com.petlog.petService.dto.UpdatePetResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PetRepository {

    // 펫 추가하기
    void createPetInfo(Pets pet);

    // userId에 등록된 반려동물 모두 찾기
    List<Pets> getPetsById(@Param("userId") int userId);

    // 펫 상세정보 가져오기
    UpdatePetResponseDto getPetDetail(@Param("userId") int userId, @Param("petId") int petId);

    // 펫 삭제하기
    void deletePet(@Param("userId") int userId, @Param("petId") int petId);

    // 펫 수정하기
    void updatePet(@Param("petId") int petId, Pets pet);

    // 펫-염려질환 삭제
    void deletePetDisease(@Param("petId") int petId);

    // 펫-염려질환 추가
    void insertPetDisease(@Param("petId") int petId, List<String> diseases);

    // 펫-알러지 삭제
    void deletePetAllergy(@Param("petId") int petId);

    // 펫-알러지 추가
    void insertPetAllergy(@Param("petId") int petId, List<String> allergies);

    // 다이어리 등록
    int createDiary(Diary diary);

    // 다이어리 이미지 등록
    void insertDiaryImage(DiaryImages diaryImages);

    // 다이어리 목록 가져오기
    List<Diary> getDiaryById(int userId);

    // 다이어리 상세 가져오기
    Diary getDiaryDetailById(int userId, int diaryId);
}