package com.petlog.petService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetInfoRequestDto {

    private String petImg;
    private String animal;
    private String petName;
    private Date petBirth;
    private String petGender;
    private double petWeight;

}