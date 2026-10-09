package com.FinalBoss.GestionGastosPersonales.modules.usuarios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinalBoss.GestionGastosPersonales.modules.usuarios.model.dto.UsuarioResponseDTO;
import com.FinalBoss.GestionGastosPersonales.modules.usuarios.service.UsuarioService;
import com.FinalBoss.GestionGastosPersonales.response.ApiResponse;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listar() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuarios obtenidos correctamente", service.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario obtenido correctamente", service.obtener(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario eliminado correctamente"));
    }
}
