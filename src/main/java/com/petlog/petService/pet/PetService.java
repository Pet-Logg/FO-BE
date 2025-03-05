package com.petlog.petService.pet;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.petlog.config.S3Config;
import com.petlog.petService.domain.Pets;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final S3Config s3Config;
    private final AmazonS3 amazonS3;

    //펫 정보 등록
    public int createPetInfo(MultipartFile petImage, String petName, String petAnimal,
                             String petBirth, String petBreed, String petGender, String petWeight, int userId ) throws BadRequestException {

        String imgUrl = null;
        if( petImage != null ){
            imgUrl = uploadFile(petImage);
        }

        Pets pet = new Pets();
        pet.setPetImg(imgUrl);
        pet.setAnimal(Pets.Animal.valueOf(petAnimal.toUpperCase()));
        pet.setPetName(petName);
        pet.setPetBreed(petBreed);
        pet.setPetGender(Pets.Gender.valueOf(petGender.toUpperCase()));
        pet.setPetWeight(Double.parseDouble(petWeight));
        pet.setUserId(userId);

        try { // setBirth을 Date타입으로 바꾸기
            LocalDate localDate = LocalDate.parse(petBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            pet.setPetBirth(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } catch (Exception e) {
            throw new BadRequestException("잘못된 날짜 형식입니다: " + petBirth);
        }


        petRepository.createPetInfo(pet);
        return pet.getPetId();
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

    public List<Pets> getPetsById(int userId){
        List<Pets> pets = petRepository.getPetsById(userId);

        return pets;
    }

    public Pets getPetDetail(int userId, int petId){
        Pets pet = petRepository.getPetDetail(userId, petId);

        if (pet == null) {
            throw new RuntimeException("반려동물 정보를 찾을 수 없습니다.");
        }

        return pet;
    }

    public void deletePet(int userId, int petId) {
        petRepository.deletePet(userId, petId);
    }
}
