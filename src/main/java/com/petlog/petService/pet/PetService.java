package com.petlog.petService.pet;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.petlog.config.S3Config;
import com.petlog.petService.entity.Diary;
import com.petlog.petService.entity.DiaryImages;
import com.petlog.petService.entity.Pets;
import com.petlog.petService.dto.CreateDiaryRequestDto;
import com.petlog.petService.dto.CreatePetRequestDto;
import com.petlog.petService.dto.UpdatePetRequestDto;
import com.petlog.petService.dto.UpdatePetResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final S3Config s3Config;
    private final AmazonS3 amazonS3;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //펫 정보 등록
    public int createPetInfo(CreatePetRequestDto dto, int userId ) throws BadRequestException {

        String imgUrl = null;
        if( dto.getPetImg() != null ){
            imgUrl = uploadFile(dto.getPetImg());
        }

        Pets pet = new Pets();
        pet.setPetImg(imgUrl);
        pet.setAnimal(Pets.Animal.valueOf(dto.getAnimal().toUpperCase()));
        pet.setPetName(dto.getPetName());
        pet.setPetBreed(dto.getPetBreed());
        pet.setPetGender(Pets.Gender.valueOf(dto.getPetGender().toUpperCase()));
        pet.setPetWeight(dto.getPetWeight());
        pet.setUserId(userId);

        try { // // setBirth을 Date타입으로 바꾸기
            if (dto.getPetBirth() != null) {
                pet.setPetBirth(LocalDate.parse(dto.getPetBirth()));
            }
        } catch (Exception e) {
            throw new BadRequestException("잘못된 날짜 형식입니다: " + dto.getPetBirth());
        }


        petRepository.createPetInfo(pet);
        return pet.getPetId();
    }


    public List<Pets> getPetsById(int userId){
        List<Pets> pets = petRepository.getPetsById(userId);

        return pets;
    }

    public UpdatePetResponseDto getPetDetail(int userId, int petId){

        UpdatePetResponseDto dto = petRepository.getPetDetail(userId, petId);

        return dto;
    }

    public void deletePet(int userId, int petId) {
        petRepository.deletePet(userId, petId);
    }

    public void updatePet(int petId, UpdatePetRequestDto dto, int userId) throws BadRequestException {

        Pets pet = new Pets();

        String imgUrl;
        if( dto.getPetImg() != null ){
            imgUrl = uploadFile(dto.getPetImg());
            pet.setPetImg(imgUrl);
        }

        pet.setUserId(userId);
        pet.setAnimal(Pets.Animal.valueOf(dto.getAnimal().toUpperCase()));
        pet.setPetName(dto.getPetName());
        pet.setPetBreed(dto.getPetBreed());
        pet.setPetGender(Pets.Gender.valueOf(dto.getPetGender().toUpperCase()));
        pet.setPetWeight(dto.getPetWeight());
        pet.setIsNeutered(dto.getIsNeutered());


        try { // // setBirth을 Date타입으로 바꾸기
            if (dto.getPetBirth() != null) {
                pet.setPetBirth(LocalDate.parse(dto.getPetBirth()));
            }
        } catch (Exception e) {
            throw new BadRequestException("잘못된 날짜 형식입니다: " + dto.getPetBirth());
        }

        petRepository.updatePet(petId, pet);

        petRepository.deletePetDisease(petId);
        petRepository.deletePetAllergy(petId);

        if(!dto.getDisease().isEmpty()){
            petRepository.insertPetDisease(petId, dto.getDisease());
        }
        if(!dto.getAllergy().isEmpty()){
            petRepository.insertPetAllergy(petId, dto.getAllergy());
        }

    }

    // 다이어리 생성
    public void createDiary(int userId, CreateDiaryRequestDto dto) {

        Diary diary = new Diary();

        diary.setUserId(userId);
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());

        petRepository.createDiary(diary);
        int diaryId = diary.getDiaryId(); // 자동생성된 다이어리 Id 가져오기

        DiaryImages diaryImages = new DiaryImages();
        diaryImages.setDiaryId(diaryId);

        // 이미지 업로드 (서버 저장 후 DB에 URL 저장)
        List<MultipartFile> images = dto.getImages();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imgUrl = uploadFile(image); // 실제 이미지 저장 후 URL 반환
                diaryImages.setImgUrl(imgUrl);
                petRepository.insertDiaryImage(diaryImages);
            }
        }
    }

    public List<Diary> getDiaryById(int userId){

        return petRepository.getDiaryById(userId);
    }

    public Diary getDiaryDetailById(int userId, int diaryId){
        return petRepository.getDiaryDetailById(userId, diaryId);
    }

    public String uploadFile(MultipartFile file){
        String bucketName = s3Config.getS3().getBucket();
        String fileName = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucketName,fileName).toString(); // 업로드된 파일 URL 반환

        } catch (IOException e){
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }

}
