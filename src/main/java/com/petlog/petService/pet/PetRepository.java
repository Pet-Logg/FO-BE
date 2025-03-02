package com.petlog.petService.pet;

import com.petlog.petService.domain.Pets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PetRepository {
    int createPetInfo(Pets pet);

    List<Pets> getPetsById(@Param("userId") int userid);

    Pets getPetDetail(@Param("userId") int userid, @Param("petId") int petId);
}
