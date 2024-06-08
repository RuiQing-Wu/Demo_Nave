package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.NaveInputDTO;
import com.example.demo.dto.NaveOutputDTO;

public interface NaveService {
    NaveOutputDTO createNave(NaveInputDTO naveDTO);

    NaveOutputDTO getNaveById(long id);

    List<NaveOutputDTO> getAllNave(Integer pageNo, Integer pageSize, String containNombre);

    NaveOutputDTO updateNave(long id, NaveInputDTO nave);

    void deleteNave(long id);
}
