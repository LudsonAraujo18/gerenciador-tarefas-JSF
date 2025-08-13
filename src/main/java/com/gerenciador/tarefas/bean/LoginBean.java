package com.gerenciador.tarefas.bean;

import com.gerenciador.tarefas.dao.UsuarioDAO;
import com.gerenciador.tarefas.model.Usuario;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private String login;
    private String senha;
    private Usuario usuarioLogado;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String logar() {
        System.out.println("Tentando login para: " + login);

        Usuario usuario = usuarioDAO.buscarPorLoginESenha(login, senha);

        if (usuario != null) {
            this.usuarioLogado = usuario;
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("usuarioLogado", usuario);
            System.out.println("Login bem-sucedido para: " + usuario.getLogin());
            return "index.xhtml?faces-redirect=true";
        } else {
            System.out.println("Falha no login para: " + login);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login ou senha inválidos!", null));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.usuarioLogado = null;
        System.out.println("Usuário deslogado.");
        return "login.xhtml?faces-redirect=true";
    }

    public boolean estaLogado() {
        return usuarioLogado != null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // Getters e setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
