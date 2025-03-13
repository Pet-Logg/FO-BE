package com.petlog.petService.dto;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class UpdatePetRequestDto {

    @Nullable
    private MultipartFile petImg;
    private String petName;
    private String animal;
    private String petBirth;
    private String petBreed;
    private String petGender;
    private String petWeight;
    @Nullable
    private String isNeutered;
    @Nullable
    private List<String> disease;
    @Nullable
    private List<String> allergy ;

}
