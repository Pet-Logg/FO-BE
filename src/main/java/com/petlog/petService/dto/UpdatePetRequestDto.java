package com.petlog.petService.dto;

import io.micrometer.common.lang.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdatePetRequestDto {

    private String petName;
    @Nullable
    private MultipartFile petImg;
    private String animal;
    private String petBirth;
    private String petBreed;
    private String petGender;
    private double petWeight;

    @Nullable
    private String isNeutered;
    @Nullable
    private List<String> disease;
    @Nullable
    private List<String> allergy ;

}
