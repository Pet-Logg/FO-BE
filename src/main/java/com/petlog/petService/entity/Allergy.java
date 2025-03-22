package com.petlog.petService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Allergy {
    private int allergyId;
    private String allergyName;
}
