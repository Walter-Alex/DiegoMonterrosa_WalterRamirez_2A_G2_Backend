package com.FinalBoss.GestionGastosPersonales.modules.movimientos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.dto.MovimientoRequestDTO;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.dto.MovimientoResponseDTO;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.service.MovimientoService;
import com.FinalBoss.GestionGastosPersonales.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {

    private final MovimientoService service;

    public MovimientoController(MovimientoService service) {
        this.service = service;
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> listarPorCuenta(
            @PathVariable Long idCuenta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimientos obtenidos correctamente",
                service.listarPorCuenta(idCuenta, tipo, categoria, desde, hasta)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimiento obtenido correctamente", service.obtener(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> crear(@Valid @RequestBody MovimientoRequestDTO dto) {
        MovimientoResponseDTO creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Movimiento registrado correctamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> actualizar(@PathVariable Long id,
            @Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimiento actualizado correctamente", service.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Movimiento eliminado correctamente"));
    }
}
