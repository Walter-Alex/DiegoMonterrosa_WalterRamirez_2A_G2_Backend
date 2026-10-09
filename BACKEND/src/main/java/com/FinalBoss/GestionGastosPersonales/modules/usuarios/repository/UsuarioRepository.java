package com.FinalBoss.GestionGastosPersonales.modules.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalBoss.GestionGastosPersonales.modules.usuarios.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
