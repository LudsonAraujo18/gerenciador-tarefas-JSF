package com.gerenciador.tarefas.dao;

import com.gerenciador.tarefas.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest extends BaseDAOTest {

    @BeforeEach
    public void init() {
        limparBanco();
    }

    @Test
    void deveSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setLogin("maria");
        usuario.setSenha("123");

        em.persist(usuario);
        em.flush();

        assertNotNull(usuario.getId());
    }

    @Test
    void naoDeveSalvarUsuarioComLoginDuplicado() {
        Usuario u1 = new Usuario();
        u1.setNome("João");
        u1.setLogin("joao");
        u1.setSenha("123");
        em.persist(u1);

        Usuario u2 = new Usuario();
        u2.setNome("Outro João");
        u2.setLogin("joao"); // mesmo login
        u2.setSenha("456");

        assertThrows(Exception.class, () -> {
            em.persist(u2);
            em.flush();
        });
    }

    @Test
    void deveBuscarUsuarioPorLogin() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setLogin("ana");
        usuario.setSenha("123");
        em.persist(usuario);
        em.flush();

        Usuario encontrado = em.createQuery(
                        "SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class)
                .setParameter("login", "ana")
                .getSingleResult();

        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getNome());
    }

    @Test
    void deveRetornarVazioQuandoUsuarioNaoEncontrado() {
        assertThrows(NoResultException.class, () -> {
            em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class)
                    .setParameter("login", "inexistente")
                    .getSingleResult();
        });
    }
}
