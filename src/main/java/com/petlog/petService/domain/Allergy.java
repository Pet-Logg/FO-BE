package com.petlog.petService.domain;

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
