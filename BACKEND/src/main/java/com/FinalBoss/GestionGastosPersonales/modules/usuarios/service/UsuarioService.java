package com.FinalBoss.GestionGastosPersonales.modules.usuarios.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FinalBoss.GestionGastosPersonales.modules.cuentas.repository.CuentaRepository;
import com.FinalBoss.GestionGastosPersonales.modules.cuentas.service.CuentaService;
import com.FinalBoss.GestionGastosPersonales.modules.usuarios.model.dto.UsuarioResponseDTO;
import com.FinalBoss.GestionGastosPersonales.modules.usuarios.model.entity.Usuario;
import com.FinalBoss.GestionGastosPersonales.modules.usuarios.repository.UsuarioRepository;
import com.FinalBoss.GestionGastosPersonales.response.RecursoNoEncontradoException;
import com.FinalBoss.GestionGastosPersonales.response.ReglaNegocioException;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final CuentaRepository cuentaRepository;
    private final CuentaService cuentaService;

    public UsuarioService(UsuarioRepository repository, CuentaRepository cuentaRepository,
            CuentaService cuentaService) {
        this.repository = repository;
        this.cuentaRepository = cuentaRepository;
        this.cuentaService = cuentaService;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return repository.findAll().stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getFechaRegistro(),
                        null))
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtener(Long id) {
        Usuario usuario = buscar(id);
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getFechaRegistro(),
                cuentaService.listarPorUsuario(id));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscar(id);
        if (cuentaRepository.existsByUsuarioIdUsuario(id)) {
            throw new ReglaNegocioException("No se puede eliminar el usuario porque posee cuentas registradas");
        }
        repository.delete(usuario);
        System.out.printf("Usuario eliminado con id %d%n", id);
    }

    private Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con id " + id));
    }
}
