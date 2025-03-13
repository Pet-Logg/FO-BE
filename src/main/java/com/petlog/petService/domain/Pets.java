package com.petlog.petService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petlog.userService.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pets extends CommonEntity {

    private int petId;
    private int userId; // 주인의 아이디
    private String petImg;
    private Animal animal;
    private String petName;
    private LocalDate petBirth;
    private String petBreed; // 견종, 묘종
    private Gender petGender;
    private double petWeight;
    private String isNeutered; // 중성화 여부

    public enum Animal {
        DOG, CAT
    }

    public enum Gender {
        MALE, FEMALE
    }
}
