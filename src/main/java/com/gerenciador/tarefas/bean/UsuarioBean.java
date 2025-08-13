package com.gerenciador.tarefas.bean;

import com.gerenciador.tarefas.dao.UsuarioDAO;
import com.gerenciador.tarefas.model.Usuario;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

    private String login;
    private String senha;
    private Usuario usuarioLogado;

    // Getters e Setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Usuario getUsuarioLogado() { return usuarioLogado; }

    // Cadastro
    public String cadastrar() {
        UsuarioDAO dao = new UsuarioDAO();

        if (dao.buscarPorLogin(login) != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login já existe!", null));
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(senha);

        dao.salvar(usuario);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário cadastrado com sucesso!", null));

        return "login.xhtml?faces-redirect=true";
    }

    // Login
    public String login() {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.buscarPorLoginESenha(login, senha);

        if (usuario != null) {
            this.usuarioLogado = usuario;
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("usuarioLogado", usuario);
            return "index.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou senha inválidos!", null));
            return null;
        }
    }

    // Logout
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuarioLogado = null;
        return "login.xhtml?faces-redirect=true";
    }
}
