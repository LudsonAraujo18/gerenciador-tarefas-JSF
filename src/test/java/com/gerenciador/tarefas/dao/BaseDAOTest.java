package com.gerenciador.tarefas.dao;

import com.gerenciador.tarefas.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;

public abstract class BaseDAOTest {

    protected EntityManager em;

    @BeforeEach
    public void setUp() {
        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback(); // garante que não fique nada pendente
        }
        em.close();
    }

    protected void limparBanco() {
        em.createQuery("DELETE FROM Tarefa").executeUpdate();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
    }
}
