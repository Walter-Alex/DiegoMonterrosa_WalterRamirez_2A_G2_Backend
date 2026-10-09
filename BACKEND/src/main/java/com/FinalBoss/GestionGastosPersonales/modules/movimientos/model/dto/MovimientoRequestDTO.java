package com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MovimientoRequestDTO(
        @NotNull(message = "Debe seleccionar una cuenta")
        Long id_cuenta,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotBlank(message = "El tipo de movimiento es obligatorio")
        @Pattern(regexp = "INGRESO|GASTO", message = "El tipo debe ser INGRESO o GASTO")
        String tipo,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 80, message = "La categoría no puede superar los 80 caracteres")
        String categoria,

        @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
        String descripcion,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        @Digits(integer = 10, fraction = 2, message = "El monto admite máximo 10 enteros y 2 decimales")
        BigDecimal monto) {
}
