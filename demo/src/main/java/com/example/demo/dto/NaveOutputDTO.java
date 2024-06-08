package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "NaveOutputDTO", description = "DTO para la salida de una nave")
public class NaveOutputDTO extends NaveInputDTO {

    @Schema(description = "Identificador de la nave", example = "1", required = true)
    @JsonProperty("id")
    private final long id;

    @JsonCreator
    public NaveOutputDTO(final long id, final String nombre) {
        super(nombre);
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
