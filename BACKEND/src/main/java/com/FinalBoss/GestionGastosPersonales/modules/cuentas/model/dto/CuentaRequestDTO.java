package com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CuentaRequestDTO(
        @NotNull(message = "Debe seleccionar un usuario")
        Long id_usuario,

        @NotBlank(message = "El nombre de la cuenta es obligatorio")
        @Size(max = 100, message = "El nombre de la cuenta no puede superar los 100 caracteres")
        String nombre,

        @NotBlank(message = "El tipo de cuenta es obligatorio")
        @Pattern(regexp = "EFECTIVO|BANCO|TARJETA", message = "El tipo debe ser EFECTIVO, BANCO o TARJETA")
        String tipo,

        @NotNull(message = "El saldo inicial es obligatorio")
        @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El saldo inicial admite máximo 10 enteros y 2 decimales")
        BigDecimal saldo_inicial,

        @Pattern(regexp = "S|N", message = "El estado debe ser S o N")
        String activa) {
}
