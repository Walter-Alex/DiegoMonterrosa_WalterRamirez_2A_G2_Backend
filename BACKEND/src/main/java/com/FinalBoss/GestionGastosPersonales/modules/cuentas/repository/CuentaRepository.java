package com.FinalBoss.GestionGastosPersonales.modules.cuentas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    List<Cuenta> findByUsuarioIdUsuarioOrderByIdCuentaAsc(Long idUsuario);

    boolean existsByUsuarioIdUsuario(Long idUsuario);
}
