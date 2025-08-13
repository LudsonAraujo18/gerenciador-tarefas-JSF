package com.gerenciador.tarefas.bean;

import com.gerenciador.tarefas.dao.TarefaDAO;
import com.gerenciador.tarefas.model.Prioridade;
import com.gerenciador.tarefas.model.Tarefa;
import com.gerenciador.tarefas.model.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class TarefaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tarefa tarefa;
    private List<Tarefa> tarefas;
    private String filtroStatus;
    private Long filtroNumero;
    private String filtroTitulo;
    private String filtroResponsavel;

    private List<Prioridade> prioridades;
    private boolean editando = false;

    private TarefaDAO tarefaDAO = new TarefaDAO();

    @PostConstruct
    public void init() {
        prioridades = Arrays.asList(Prioridade.values());

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam != null) {
            try {
                Long id = Long.parseLong(idParam);
                tarefa = tarefaDAO.buscarPorId(id);
                if (tarefa != null) {
                    editando = true;
                } else {
                    tarefa = new Tarefa();
                }
            } catch (NumberFormatException e) {
                tarefa = new Tarefa();
            }
        } else {
            tarefa = new Tarefa();
        }

        carregarTarefas();
    }

    public void carregarTarefas() {
        Usuario usuarioLogado = getUsuarioLogado();
        tarefas = tarefaDAO.listarPorUsuario(usuarioLogado);
    }

    public List<Tarefa> getTarefasFiltradas() {
        return tarefas.stream()
                .filter(t -> filtroNumero == null || t.getId().equals(filtroNumero))
                .filter(t -> filtroTitulo == null || filtroTitulo.isEmpty() ||
                        t.getTitulo().toLowerCase().contains(filtroTitulo.toLowerCase()))
                .filter(t -> filtroResponsavel == null || filtroResponsavel.isEmpty() ||
                        t.getResponsavel().toLowerCase().contains(filtroResponsavel.toLowerCase()))
                .filter(t -> filtroStatus == null || filtroStatus.isEmpty() || filtroStatus.equals("Todos") ||
                        t.getStatus().equalsIgnoreCase(filtroStatus))
                .collect(Collectors.toList());
    }

    public String salvarOuAtualizar() {
        tarefa.setUsuario(getUsuarioLogado());
        if (!editando) {
            tarefa.setStatus("Em Andamento"); // status padrão
        }

        if (editando) {
            tarefaDAO.atualizar(tarefa);
        } else {
            tarefaDAO.salvar(tarefa);
        }

        tarefa = new Tarefa();
        editando = false;
        carregarTarefas();
        return "index.xhtml?faces-redirect=true";
    }

    public void excluir(Tarefa tarefa) {
        tarefaDAO.excluir(tarefa);
        carregarTarefas();
    }

    public void concluirTarefa(Tarefa tarefa) {
        tarefa.setStatus("Concluída");
        tarefaDAO.atualizar(tarefa);
        carregarTarefas();
    }

    public String editar(Tarefa tarefa) {
        return "nova_tarefa.xhtml?faces-redirect=true&id=" + tarefa.getId();
    }

    public void filtrar() {
        // Apenas força a atualização da lista filtrada na tela
    }

    public List<String> getStatusDisponiveis() {
        return Arrays.asList("Em Andamento", "Concluída");
    }

    private Usuario getUsuarioLogado() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        return (Usuario) sessionMap.get("usuarioLogado");
    }

    // Getters e Setters
    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }

    public List<Tarefa> getTarefas() { return tarefas; }
    public void setTarefas(List<Tarefa> tarefas) { this.tarefas = tarefas; }

    public String getFiltroStatus() { return filtroStatus; }
    public void setFiltroStatus(String filtroStatus) { this.filtroStatus = filtroStatus; }

    public Long getFiltroNumero() { return filtroNumero; }
    public void setFiltroNumero(Long filtroNumero) { this.filtroNumero = filtroNumero; }

    public String getFiltroTitulo() { return filtroTitulo; }
    public void setFiltroTitulo(String filtroTitulo) { this.filtroTitulo = filtroTitulo; }

    public String getFiltroResponsavel() { return filtroResponsavel; }
    public void setFiltroResponsavel(String filtroResponsavel) { this.filtroResponsavel = filtroResponsavel; }

    public List<Prioridade> getPrioridades() { return prioridades; }
    public void setPrioridades(List<Prioridade> prioridades) { this.prioridades = prioridades; }

    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
}
