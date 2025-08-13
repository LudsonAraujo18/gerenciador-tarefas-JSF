package com.gerenciador.tarefas.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("*.xhtml")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean usuarioLogado = (session != null && session.getAttribute("usuarioLogado") != null);
        boolean paginaLogin = req.getRequestURI().endsWith("login.xhtml");
        boolean paginaCadastro = req.getRequestURI().endsWith("cadastro.xhtml");
        boolean recursoPublico = req.getRequestURI().contains("javax.faces.resource");

        if (usuarioLogado || paginaLogin || paginaCadastro || recursoPublico) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }

    @Override
    public void destroy() {}
}
