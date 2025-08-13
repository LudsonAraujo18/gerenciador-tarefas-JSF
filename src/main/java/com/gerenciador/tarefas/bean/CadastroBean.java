package com.gerenciador.tarefas.bean;

import com.gerenciador.tarefas.dao.UsuarioDAO;
import com.gerenciador.tarefas.model.Usuario;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@RequestScoped
public class CadastroBean implements Serializable {

    private Usuario usuario = new Usuario();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String salvar() {
        Usuario existente = usuarioDAO.buscarPorLogin(usuario.getLogin());
        if (existente != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um usuário com esse login."));
            return null;
        }

        usuarioDAO.salvar(usuario);

        FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário cadastrado com sucesso!"));

        return "login.xhtml?faces-redirect=true";
    }


    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
