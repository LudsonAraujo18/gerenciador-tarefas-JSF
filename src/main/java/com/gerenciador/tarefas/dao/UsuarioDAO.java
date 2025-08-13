package com.gerenciador.tarefas.dao;

import com.gerenciador.tarefas.model.Usuario;
import com.gerenciador.tarefas.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Usuario> lista = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class)
                    .setParameter("login", login)
                    .getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorLoginESenha(String login, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Usuario> lista = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class)
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }
}
