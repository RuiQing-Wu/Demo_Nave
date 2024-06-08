package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Nave;
import com.example.demo.dto.NaveInputDTO;
import com.example.demo.dto.NaveOutputDTO;
import com.example.demo.exception.EntidadNoEncontradaException;
import com.example.demo.exception.RepositoryException;
import com.example.demo.repository.NaveRepository;
import com.example.demo.utils.LogExecution;


@Service
public class NaveServiceImpl implements NaveService {

    private final NaveRepository naveRepository;

    public NaveServiceImpl(NaveRepository naveRepository) {
        this.naveRepository = naveRepository;
    }

    @Override
    public NaveOutputDTO createNave(NaveInputDTO naveDTO) {
        try {
            Nave nave = new Nave(naveDTO.getNombre());
            naveRepository.save(nave);
            return nave.asOutputDto();
        } catch (Exception e) {
            throw new RepositoryException("Error al crear la nave");
        }
    }

    @LogExecution(additionalMessage = "This is a aop log message")
    @Override
    public NaveOutputDTO getNaveById(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id no puede ser negativo");
        }

        if (!naveRepository.existsById(id)) {
            throw new EntidadNoEncontradaException("Nave no encontrada con id: " + id);
        }

        try {
            Optional<Nave> nave = naveRepository.findById(id);

            return nave.get().asOutputDto();

        } catch (Exception e) {
            throw new RepositoryException("Error al obtener la nave con id: " + id);
        }
    }

    @Override
    public List<NaveOutputDTO> getAllNave(Integer pageNo, Integer pageSize, String containNombre) {
        try {
            if (containNombre != null) {
                return naveRepository.findAll(PageRequest.of(pageNo, pageSize)).stream()
                        .filter(nave -> nave.getNombre().contains(containNombre))
                        .map(Nave::asOutputDto).toList();
            }

            return naveRepository.findAll(PageRequest.of(pageNo, pageSize)).stream().map(Nave::asOutputDto).toList();
        } catch (Exception e) {
            throw new RepositoryException("Error al obtener todas las naves");
        }
    }

    @Override
    public NaveOutputDTO updateNave(long id, NaveInputDTO nave) {
        if (!naveRepository.existsById(id)) {
            throw new EntidadNoEncontradaException("Nave no encontrada con id: " + id);
        }

        try {
            Optional<Nave> naveOptional = naveRepository.findById(id);

            Nave naveToUpdate = naveOptional.get();
            naveToUpdate.setNombre(nave.getNombre());
            naveRepository.save(naveToUpdate);
            return naveToUpdate.asOutputDto();

        } catch (Exception e) {

            throw new RepositoryException("Error al actualizar la nave con id: " + id);
        }
    }

    @Override
    public void deleteNave(long id) {
        if (!naveRepository.existsById(id)) {
            throw new EntidadNoEncontradaException("Nave no encontrada con id: " + id);
        }

        try {
            naveRepository.deleteById(id);
        } catch (Exception e) {
            throw new RepositoryException("Error al eliminar la nave con id: " + id);
        }
    }

}
