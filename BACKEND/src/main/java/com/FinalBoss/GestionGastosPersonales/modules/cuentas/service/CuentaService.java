package com.FinalBoss.GestionGastosPersonales.modules.cuentas.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto.CuentaRequestDTO;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.dto.CuentaResponseDTO;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.model.entity.Cuenta;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.repository.CuentaRepository;
import com.FinalBoss.GestionGastosPersonales.modules.movimientos.repository.MovimientoRepository;
import com.FinalBoss.GestionGastosPersonales.modules.usuarios.model.entity.Usuario;
import com.FinalBoss.GestionGastosPersonales.modules.usuarios.repository.UsuarioRepository;
import com.FinalBoss.GestionGastosPersonales.response.RecursoNoEncontradoException;
import com.FinalBoss.GestionGastosPersonales.response.ReglaNegocioException;

@Service
public class CuentaService {

    private static final String ACTIVA = "S";
    private static final String INACTIVA = "N";
    private static final String INGRESO = "INGRESO";
    private static final String GASTO = "GASTO";

    private final CuentaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoRepository movimientoRepository;

    public CuentaService(CuentaRepository repository, UsuarioRepository usuarioRepository,
            MovimientoRepository movimientoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> listarPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RecursoNoEncontradoException("No existe un usuario con id " + idUsuario);
        }
        return repository.findByUsuarioIdUsuarioOrderByIdCuentaAsc(idUsuario).stream()
                .map(this::aRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponseDTO obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public CuentaResponseDTO crear(CuentaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.id_usuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario seleccionado no existe"));

        String activa = dto.activa() != null ? dto.activa() : ACTIVA;
        if (INACTIVA.equals(activa) && dto.saldo_inicial().compareTo(BigDecimal.ZERO) != 0) {
            throw new ReglaNegocioException("Una cuenta solo puede registrarse inactiva si su saldo es $0.00");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setNombre(dto.nombre().trim());
        cuenta.setTipo(dto.tipo());
        cuenta.setSaldoInicial(dto.saldo_inicial());
        cuenta.setActiva(activa);

        Cuenta guardada = repository.save(cuenta);
        System.out.printf("Cuenta registrada con id %d para el usuario %d%n", guardada.getIdCuenta(), usuario.getIdUsuario());
        return aRespuesta(guardada);
    }

    @Transactional
    public CuentaResponseDTO actualizar(Long id, CuentaRequestDTO dto) {
        Cuenta cuenta = buscar(id);

        if (!cuenta.getUsuario().getIdUsuario().equals(dto.id_usuario())) {
            throw new ReglaNegocioException("No se puede cambiar el usuario al que pertenece la cuenta");
        }

        String activa = dto.activa() != null ? dto.activa() : cuenta.getActiva();
        BigDecimal saldoResultante = dto.saldo_inicial()
                .add(sumar(id, INGRESO))
                .subtract(sumar(id, GASTO));

        if (INACTIVA.equals(activa) && saldoResultante.compareTo(BigDecimal.ZERO) != 0) {
            throw new ReglaNegocioException("No se puede desactivar la cuenta porque su saldo actual es $"
                    + saldoResultante + " y debe ser $0.00");
        }

        cuenta.setNombre(dto.nombre().trim());
        cuenta.setTipo(dto.tipo());
        cuenta.setSaldoInicial(dto.saldo_inicial());
        cuenta.setActiva(activa);

        Cuenta guardada = repository.save(cuenta);
        System.out.printf("Cuenta actualizada con id %d, estado %s%n", guardada.getIdCuenta(), guardada.getActiva());
        return aRespuesta(guardada);
    }

    @Transactional
    public CuentaResponseDTO desactivar(Long id) {
        Cuenta cuenta = buscar(id);

        if (INACTIVA.equals(cuenta.getActiva())) {
            throw new ReglaNegocioException("La cuenta ya se encuentra desactivada");
        }

        BigDecimal saldoActual = calcularSaldoActual(cuenta);
        if (saldoActual.compareTo(BigDecimal.ZERO) != 0) {
            throw new ReglaNegocioException("No se puede desactivar la cuenta porque su saldo actual es $"
                    + saldoActual + " y debe ser $0.00");
        }

        cuenta.setActiva(INACTIVA);
        Cuenta guardada = repository.save(cuenta);
        System.out.printf("Cuenta desactivada con id %d%n", id);
        return aRespuesta(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        Cuenta cuenta = buscar(id);
        if (movimientoRepository.existsByCuentaIdCuenta(id)) {
            throw new ReglaNegocioException("No se puede eliminar la cuenta porque tiene movimientos asociados");
        }
        repository.delete(cuenta);
        System.out.printf("Cuenta eliminada con id %d%n", id);
    }

    private Cuenta buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cuenta con id " + id));
    }

    private BigDecimal sumar(Long idCuenta, String tipo) {
        BigDecimal total = movimientoRepository.sumarPorCuentaYTipo(idCuenta, tipo);
        return total != null ? total : BigDecimal.ZERO;
    }

    private BigDecimal calcularSaldoActual(Cuenta cuenta) {
        return cuenta.getSaldoInicial()
                .add(sumar(cuenta.getIdCuenta(), INGRESO))
                .subtract(sumar(cuenta.getIdCuenta(), GASTO));
    }

    private CuentaResponseDTO aRespuesta(Cuenta cuenta) {
        Usuario usuario = cuenta.getUsuario();
        BigDecimal ingresos = sumar(cuenta.getIdCuenta(), INGRESO);
        BigDecimal gastos = sumar(cuenta.getIdCuenta(), GASTO);
        return new CuentaResponseDTO(
                cuenta.getIdCuenta(),
                usuario.getIdUsuario(),
                usuario.getNombre(),
                cuenta.getNombre(),
                cuenta.getTipo(),
                cuenta.getSaldoInicial(),
                cuenta.getActiva(),
                ingresos,
                gastos,
                cuenta.getSaldoInicial().add(ingresos).subtract(gastos));
    }
}
