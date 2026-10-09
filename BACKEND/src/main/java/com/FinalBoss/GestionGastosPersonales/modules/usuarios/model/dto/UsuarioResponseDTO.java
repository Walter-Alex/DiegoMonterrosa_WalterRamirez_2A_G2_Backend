package com.FinalBoss.GestionGastosPersonales.modules.usuarios.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto.CuentaResponseDTO;

public record UsuarioResponseDTO(
        Long id_usuario,
        String nombre,
        String correo,
        LocalDate fecha_registro,
        List<CuentaResponseDTO> cuentas) {
}
