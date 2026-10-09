package com.FinalBoss.GestionGastosPersonales.modules.cuentas.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto.CuentaRequestDTO;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto.CuentaResponseDTO;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.service.CuentaService;
import com.FinalBoss.GestionGastosPersonales.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {

    private final CuentaService service;

    public CuentaController(CuentaService service) {
        this.service = service;
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<CuentaResponseDTO>>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuentas del usuario obtenidas correctamente",
                service.listarPorUsuario(idUsuario)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta obtenida correctamente", service.obtener(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> crear(@Valid @RequestBody CuentaRequestDTO dto) {
        CuentaResponseDTO creada = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Cuenta registrada correctamente", creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> actualizar(@PathVariable Long id,
            @Valid @RequestBody CuentaRequestDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta actualizada correctamente", service.actualizar(id, dto)));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta desactivada correctamente", service.desactivar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta eliminada correctamente"));
    }
}
