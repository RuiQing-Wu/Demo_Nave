package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.domain.Nave;
import com.example.demo.dto.NaveInputDTO;
import com.example.demo.dto.NaveOutputDTO;
import com.example.demo.exception.EntidadNoEncontradaException;
import com.example.demo.exception.RepositoryException;
import com.example.demo.repository.NaveRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NaveServiceTest {

    @Mock
    private NaveRepository naveRepository;

    @InjectMocks
    private NaveServiceImpl naveService;

    @Test
    public void testNaveServiceCreate() {
        // Arrange
        NaveInputDTO input = new NaveInputDTO("nombre");
        when(naveRepository.save(any())).thenReturn(new Nave());

        // Act
        NaveOutputDTO create = naveService.createNave(input);

        // Assert
        verify(naveRepository, times(1)).save(any());
        assertEquals(input.getNombre(), create.getNombre());
    }

    @Test
    public void testNaveServiceCreateRepositoryException() {
        // Arrange
        NaveInputDTO input = new NaveInputDTO("nombre");
        when(naveRepository.save(any())).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(RepositoryException.class, () -> {
            naveService.createNave(input);
        });

        verify(naveRepository, times(1)).save(any());
    }

    @Test
    public void testNaveServiceGetById() {

        Long id = 1L;
        Nave nave = new Nave();
        nave.setId(id);

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(true);
        when(naveRepository.findById((id))).thenReturn(Optional.of(nave));

        // Act
        NaveOutputDTO getNav = naveService.getNaveById(1);

        // Assert
        verify(naveRepository, times(1)).existsById(id);
        verify(naveRepository, times(1)).findById(id);

        assertEquals(getNav.getId(), id);
    }

    @Test
    public void testNaveServiceGetByIdNegativeId() {

        Long id = -1L;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            naveService.getNaveById(id);
        });
    }

    @Test
    public void testNaveServiceGetByIdNotFound() {

        Long id = 1L;

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(false);

        // Act & Assert
        assertThrows(EntidadNoEncontradaException.class, () -> {
            naveService.getNaveById(1);
        });
        
        verify(naveRepository, times(1)).existsById(id);
    }

    @Test
    public void testNaveServiceGetByIdRepositoryException() {

        Long id = 1L;

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(true);
        when(naveRepository.findById((id))).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(RepositoryException.class, () -> {
            naveService.getNaveById(1);
        });

        verify(naveRepository, times(1)).existsById(id);
        verify(naveRepository, times(1)).findById(id);
    }

    @Test
    public void testNaveServiceGetAll() {
        // Arrange
        when(naveRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());

        // Act
        List<NaveOutputDTO> allNave = naveService.getAllNave(0, 5, null);

        // Assert
        verify(naveRepository, times(1)).findAll(PageRequest.of(0, 5));

        assertEquals(0, allNave.size());
    }

    @Test
    public void testNaveServiceGetAllContainNombre() {
        // Arrange
        when(naveRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());

        // Act
        List<NaveOutputDTO> allNave = naveService.getAllNave(0, 5, "nombre");

        // Assert
        verify(naveRepository, times(1)).findAll(PageRequest.of(0, 5));

        assertEquals(0, allNave.size());
    }

    @Test
    public void testNaveServiceGetAllRepositoryException() {
        // Arrange
        when(naveRepository.findAll(PageRequest.of(0, 5))).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(RepositoryException.class, () -> {
            naveService.getAllNave(0, 5, null);
        });

        verify(naveRepository, times(1)).findAll(PageRequest.of(0, 5));
    }

    @Test
    public void testNaveServiceDelete() {

        Long id = 1L;
        Nave nave = new Nave();
        nave.setId(id);

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(true);

        // Act
        naveService.deleteNave(id);

        // Assert
        verify(naveRepository, times(1)).existsById(id);
        verify(naveRepository, times(1)).deleteById(id);
    }

    @Test
    public void testNaveServiceDeleteNotFound() {

        Long id = 1L;

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(false);

        // Act & Assert
        assertThrows(EntidadNoEncontradaException.class, () -> {
            naveService.deleteNave(id);
        });

        verify(naveRepository, times(1)).existsById(id);
    }

    @Test
    public void testNaveServiceDeleteRepositoryException() {

        Long id = 1L;

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(true);
        doThrow(new IllegalArgumentException()).when(naveRepository).deleteById(id);

        // Act & Assert
        assertThrows(RepositoryException.class, () -> {
            naveService.deleteNave(id);
        });

        verify(naveRepository, times(1)).existsById(id);
        verify(naveRepository, times(1)).deleteById(id);
    }

    @Test
    public void testNaveServiceUpdate() {

        Long id = 1L;
        Nave nave = new Nave();
        nave.setId(id);
        NaveInputDTO input = new NaveInputDTO("nombre");

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(true);
        when(naveRepository.findById((id))).thenReturn(Optional.of(nave));
        when(naveRepository.save(any())).thenReturn(nave);

        // Act
        NaveOutputDTO update = naveService.updateNave(id, input);

        // Assert
        verify(naveRepository, times(1)).existsById(id);
        verify(naveRepository, times(1)).findById(id);
        verify(naveRepository, times(1)).save(any());

        assertEquals(input.getNombre(), update.getNombre());
    }

    @Test
    public void testNaveServiceUpdateNotFound() {

        Long id = 1L;
        NaveInputDTO input = new NaveInputDTO("nombre");

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(false);

        // Act & Assert
        assertThrows(EntidadNoEncontradaException.class, () -> {
            naveService.updateNave(id, input);
        });

        verify(naveRepository, times(1)).existsById(id);
    }

    @Test
    public void testNaveServiceUpdateRepositoryException() {

        Long id = 1L;
        Nave nave = new Nave();
        nave.setId(id);
        NaveInputDTO input = new NaveInputDTO("nombre");

        // Arrange
        when(naveRepository.existsById((id))).thenReturn(true);
        when(naveRepository.findById((id))).thenReturn(Optional.of(nave));
        when(naveRepository.save(any())).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(RepositoryException.class, () -> {
            naveService.updateNave(id, input);
        });

        verify(naveRepository, times(1)).existsById(id);
        verify(naveRepository, times(1)).findById(id);
        verify(naveRepository, times(1)).save(any());
    }


}