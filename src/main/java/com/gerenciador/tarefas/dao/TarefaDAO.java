package com.gerenciador.tarefas.dao;

import com.gerenciador.tarefas.model.Tarefa;
import com.gerenciador.tarefas.model.Usuario;
import com.gerenciador.tarefas.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class TarefaDAO {

    public void salvar(Tarefa tarefa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (tarefa.getId() == null) {
                em.persist(tarefa);
            } else {
                em.merge(tarefa);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void atualizar(Tarefa tarefa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tarefa);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void excluir(Tarefa tarefa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa tarefaGerenciada = em.find(Tarefa.class, tarefa.getId());
            if (tarefaGerenciada != null) {
                em.remove(tarefaGerenciada);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT t FROM Tarefa t WHERE t.usuario = :usuario");
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Tarefa> buscarPorStatusEUsuario(String status, Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createQuery(
                    "SELECT t FROM Tarefa t WHERE t.status = :status AND t.usuario = :usuario"
            );
            query.setParameter("status", status);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Tarefa buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } finally {
            em.close();
        }
    }
}
