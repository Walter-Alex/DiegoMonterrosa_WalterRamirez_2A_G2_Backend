package com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimientoResponseDTO(
        Long id_movimiento,
        Long id_cuenta,
        String nombre_cuenta,
        LocalDate fecha,
        String tipo,
        String categoria,
        String descripcion,
        BigDecimal monto) {
}
