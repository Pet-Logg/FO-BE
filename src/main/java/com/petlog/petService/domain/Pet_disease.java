package com.petlog.petService.domain;

import com.petlog.userService.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet_disease {
    private int petId;
    private int diseaseId;
}
