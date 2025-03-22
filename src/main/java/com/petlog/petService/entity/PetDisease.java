package com.petlog.petService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDisease {
    private int petId;
    private int diseaseId;
}
