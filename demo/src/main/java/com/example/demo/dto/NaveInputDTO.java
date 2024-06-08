package com.example.demo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "NaveInputDTO", description = "DTO para la creación de una nave")
public class NaveInputDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Nombre de la nave", example = "Falcon", required = true)
    @JsonProperty("nombre")
    @NotBlank(message = "El nombre no puede ser nulo")
    private final String nombre;

    @JsonCreator
    public NaveInputDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
