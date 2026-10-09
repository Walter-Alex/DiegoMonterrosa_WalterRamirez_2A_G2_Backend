package com.FinalBoss.GestionGastosPersonales.modules.movimientos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdCuentaOrderByFechaDescIdMovimientoDesc(Long idCuenta);

    boolean existsByCuentaIdCuenta(Long idCuenta);

    @Query("SELECT SUM(m.monto) FROM Movimiento m WHERE m.cuenta.idCuenta = :idCuenta AND m.tipo = :tipo")
    BigDecimal sumarPorCuentaYTipo(@Param("idCuenta") Long idCuenta, @Param("tipo") String tipo);
}
