package com.petlog.petService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disease {
    private int diseaseId;
    private String diseaseName;
}
