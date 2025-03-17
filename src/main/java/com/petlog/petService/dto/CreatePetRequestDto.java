package com.petlog.petService.dto;

import io.micrometer.common.lang.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreatePetRequestDto {

    private String petName;
    @Nullable
    private MultipartFile petImg;
    private String animal;
    private String petBirth;
    private String petBreed;
    private String petGender;
    private double petWeight;

}
