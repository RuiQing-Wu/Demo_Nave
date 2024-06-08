package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NaveInputDTO;
import com.example.demo.dto.NaveOutputDTO;
import com.example.demo.service.NaveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Nave", description = "API para la gestión de naves")
@RestController
@Validated
@RequestMapping("/api/nave")
public class NaveController {

    private final NaveService naveService;

    @Autowired
    private RedisTemplate<String, NaveOutputDTO> redisTemplate;

    public NaveController(NaveService naveService) {
        this.naveService = naveService;
    }

    @Operation(summary = "Crear una nave nueva", description = "Crea una nueva nave en la base de datos", tags = "Nave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nave creada exitosamente", content = {
                    @Content(schema = @Schema(implementation = NaveOutputDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Petición inválida", content = @Content(schema = @Schema())),
    })
    @PostMapping("")
    public ResponseEntity<NaveOutputDTO> createNave(@Valid @RequestBody NaveInputDTO naveInputDTO) {
        NaveOutputDTO naveOutputDTO = naveService.createNave(naveInputDTO);
        String key = String.valueOf(naveOutputDTO.getId());

        // Save to Redis
        redisTemplate.opsForValue().set(key, naveOutputDTO);
        return new ResponseEntity<>(naveOutputDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener una nave por ID", description = "Obtiene una nave por su ID", tags = "Nave")
    @Parameters({
            @Parameter(name="id", description = "ID de la nave a obtener", required = true, content = @Content(schema = @Schema(type = "integer"))) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nave encontrada", content = {
                    @Content(schema = @Schema(implementation = NaveOutputDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Nave no encontrada", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NaveOutputDTO> getNaveById(@PathVariable Long id) {
        NaveOutputDTO redisNave = redisTemplate.opsForValue().get(String.valueOf(id));
        System.out.println(redisNave);
        if (redisNave != null) {
            return ResponseEntity.ok(redisNave);
        }

        return ResponseEntity.ok(naveService.getNaveById(id));
    }

    @Operation(summary = "Obtener todas las naves", description = "Obtiene todas las naves en la base de datos", tags = "Nave")
    @Parameters({
            @Parameter(description = "Número de página", name = "pageNo", content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
            @Parameter(description = "Tamaño de la página", name = "pageSize", content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),
            @Parameter(description = "Nombre de la nave a buscar", name = "containNombre", content = @Content(schema = @Schema(type = "string"))),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Naves encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NaveOutputDTO.class))),
            @ApiResponse(responseCode = "404", description = "Naves no encontradas", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = { @Content(schema = @Schema())})
    })
    @GetMapping("")
    public ResponseEntity<List<NaveOutputDTO>> getAllNave(
            @RequestParam(defaultValue = "0", required = false) Integer pageNo,
            @RequestParam(defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(required = false) String containNombre) {

        return new ResponseEntity<>(naveService.getAllNave(pageNo, pageSize, containNombre), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar una nave", description = "Actualiza una nave por su ID", tags = "Nave")
    @Parameters({
            @Parameter(name="id", description = "ID de la nave a actualizar", required = true, content = @Content(schema = @Schema(type = "integer"))) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nave actualizada exitosamente", content = { @Content(schema = @Schema(implementation = NaveOutputDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Petición inválida", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Nave no encontrada", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = { @Content(schema = @Schema())})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<NaveOutputDTO> updateNave(@PathVariable Long id,
            @Valid @RequestBody NaveInputDTO naveInputDTO) {
        return new ResponseEntity<>(naveService.updateNave(id, naveInputDTO), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una nave", description = "Elimina una nave por su ID", tags = "Nave")
    @Parameters({
            @Parameter(name = "id", description = "ID de la nave a eliminar", required = true, content = @Content(schema = @Schema(type = "integer"))) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nave eliminada exitosamente", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Nave no encontrada", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = { @Content(schema = @Schema())})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNave(@PathVariable Long id) {
        naveService.deleteNave(id);
        return ResponseEntity.ok("Nave eliminada con id: " + id);
    }
}
