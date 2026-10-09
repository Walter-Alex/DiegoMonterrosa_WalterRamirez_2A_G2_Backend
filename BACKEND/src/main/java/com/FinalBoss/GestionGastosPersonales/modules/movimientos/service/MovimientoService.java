package com.FinalBoss.GestionGastosPersonales.modules.movimientos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.entity.Cuenta;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.repository.CuentaRepository;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.dto.MovimientoRequestDTO;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.dto.MovimientoResponseDTO;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.model.entity.Movimiento;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.repository.MovimientoRepository;
import com.FinalBoss.GestionGastosPersonales.response.RecursoNoEncontradoException;
import com.FinalBoss.GestionGastosPersonales.response.ReglaNegocioException;

@Service
public class MovimientoService {

    private static final String ACTIVA = "S";

    private final MovimientoRepository repository;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository repository, CuentaRepository cuentaRepository) {
        this.repository = repository;
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarPorCuenta(Long idCuenta, String tipo, String categoria,
            LocalDate desde, LocalDate hasta) {
        buscarCuenta(idCuenta);

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha inicial no puede ser mayor que la fecha final");
        }

        String tipoFiltro = tipo != null && !tipo.isBlank() ? tipo.trim().toUpperCase() : null;
        String categoriaFiltro = categoria != null && !categoria.isBlank() ? categoria.trim().toLowerCase() : null;

        return repository.findByCuentaIdCuentaOrderByFechaDescIdMovimientoDesc(idCuenta).stream()
                .filter(m -> tipoFiltro == null || m.getTipo().equals(tipoFiltro))
                .filter(m -> categoriaFiltro == null || m.getCategoria().toLowerCase().contains(categoriaFiltro))
                .filter(m -> desde == null || !m.getFecha().isBefore(desde))
                .filter(m -> hasta == null || !m.getFecha().isAfter(hasta))
                .map(this::aRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDTO obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public MovimientoResponseDTO crear(MovimientoRequestDTO dto) {
        Movimiento movimiento = new Movimiento();
        cargar(movimiento, dto);
        Movimiento guardado = repository.save(movimiento);
        System.out.printf("Movimiento registrado con id %d (%s de %s)%n",
                guardado.getIdMovimiento(), guardado.getTipo(), guardado.getMonto());
        return aRespuesta(guardado);
    }

    @Transactional
    public MovimientoResponseDTO actualizar(Long id, MovimientoRequestDTO dto) {
        Movimiento movimiento = buscar(id);
        cargar(movimiento, dto);
        Movimiento guardado = repository.save(movimiento);
        System.out.printf("Movimiento actualizado con id %d%n", guardado.getIdMovimiento());
        return aRespuesta(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Movimiento movimiento = buscar(id);
        repository.delete(movimiento);
        System.out.printf("Movimiento eliminado con id %d%n", id);
    }

    private Movimiento buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un movimiento con id " + id));
    }

    private Cuenta buscarCuenta(Long idCuenta) {
        return cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cuenta con id " + idCuenta));
    }

    private void cargar(Movimiento movimiento, MovimientoRequestDTO dto) {
        Cuenta cuenta = buscarCuenta(dto.id_cuenta());
        if (!ACTIVA.equals(cuenta.getActiva())) {
            throw new ReglaNegocioException("Solo se pueden registrar o modificar movimientos en cuentas activas");
        }

        boolean sinDescripcion = dto.descripcion() == null || dto.descripcion().isBlank();

        movimiento.setCuenta(cuenta);
        movimiento.setFecha(dto.fecha());
        movimiento.setTipo(dto.tipo());
        movimiento.setCategoria(dto.categoria().trim());
        movimiento.setDescripcion(sinDescripcion ? null : dto.descripcion().trim());
        movimiento.setMonto(dto.monto());
    }

    private MovimientoResponseDTO aRespuesta(Movimiento movimiento) {
        Cuenta cuenta = movimiento.getCuenta();
        return new MovimientoResponseDTO(
                movimiento.getIdMovimiento(),
                cuenta.getIdCuenta(),
                cuenta.getNombre(),
                movimiento.getFecha(),
                movimiento.getTipo(),
                movimiento.getCategoria(),
                movimiento.getDescripcion(),
                movimiento.getMonto());
    }
}
