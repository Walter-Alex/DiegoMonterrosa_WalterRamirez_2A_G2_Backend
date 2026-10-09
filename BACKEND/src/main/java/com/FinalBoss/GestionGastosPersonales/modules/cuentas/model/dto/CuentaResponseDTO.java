package com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto;

import java.math.BigDecimal;

public record CuentaResponseDTO(
        Long id_cuenta,
        Long id_usuario,
        String nombre_usuario,
        String nombre,
        String tipo,
        BigDecimal saldo_inicial,
        String activa,
        BigDecimal total_ingresos,
        BigDecimal total_gastos,
        BigDecimal saldo_actual) {
}
