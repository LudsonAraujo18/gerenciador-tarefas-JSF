package com.gerenciador.tarefas.dao;

import com.gerenciador.tarefas.model.Prioridade;
import com.gerenciador.tarefas.model.Tarefa;
import com.gerenciador.tarefas.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarefaDAOTest extends BaseDAOTest {

    @BeforeEach
    public void init() {
        limparBanco();
    }

    @Test
    void deveSalvarTarefa() {
        Usuario usuario = criarUsuario();

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Estudar JPA");
        tarefa.setDescricao("Ler sobre EntityManager");
        tarefa.setPrioridade(Prioridade.ALTA);
        tarefa.setDeadline(LocalDate.now().plusDays(5));
        tarefa.setStatus("PENDENTE");
        tarefa.setUsuario(usuario);

        em.persist(tarefa);
        em.flush();

        assertNotNull(tarefa.getId());
    }

    @Test
    void deveAtualizarTarefa() {
        Usuario usuario = criarUsuario();

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Fazer compras");
        tarefa.setDescricao("Lista de supermercado");
        tarefa.setPrioridade(Prioridade.MEDIA);
        tarefa.setDeadline(LocalDate.now().plusDays(2));
        tarefa.setStatus("PENDENTE");
        tarefa.setUsuario(usuario);

        em.persist(tarefa);

        tarefa.setStatus("CONCLUIDA");
        em.merge(tarefa);
        em.flush();

        Tarefa atualizada = em.find(Tarefa.class, tarefa.getId());
        assertEquals("CONCLUIDA", atualizada.getStatus());
    }

    @Test
    void deveExcluirTarefa() {
        Usuario usuario = criarUsuario();

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Lavar o carro");
        tarefa.setDescricao("Limpeza completa");
        tarefa.setPrioridade(Prioridade.BAIXA);
        tarefa.setDeadline(LocalDate.now().plusDays(1));
        tarefa.setStatus("PENDENTE");
        tarefa.setUsuario(usuario);

        em.persist(tarefa);
        em.flush();

        Long id = tarefa.getId();

        em.remove(tarefa);
        em.flush();

        assertNull(em.find(Tarefa.class, id));
    }

    @Test
    void deveBuscarTarefasPorStatus() {
        Usuario usuario = criarUsuario();

        Tarefa t1 = new Tarefa();
        t1.setTitulo("Tarefa 1");
        t1.setDescricao("Descrição 1");
        t1.setPrioridade(Prioridade.ALTA);
        t1.setDeadline(LocalDate.now());
        t1.setStatus("PENDENTE");
        t1.setUsuario(usuario);

        Tarefa t2 = new Tarefa();
        t2.setTitulo("Tarefa 2");
        t2.setDescricao("Descrição 2");
        t2.setPrioridade(Prioridade.BAIXA);
        t2.setDeadline(LocalDate.now());
        t2.setStatus("CONCLUIDA");
        t2.setUsuario(usuario);

        em.persist(t1);
        em.persist(t2);
        em.flush();

        List<Tarefa> pendentes = em.createQuery(
                        "SELECT t FROM Tarefa t WHERE t.status = :status", Tarefa.class)
                .setParameter("status", "PENDENTE")
                .getResultList();

        assertEquals(1, pendentes.size());
        assertEquals("Tarefa 1", pendentes.get(0).getTitulo());
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setLogin("teste");
        usuario.setSenha("123");
        em.persist(usuario);
        em.flush();
        return usuario;
    }
}
