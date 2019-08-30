/**
 * @author diegohartwig.it@gmail.com
 *
 * 20 de ago de 2016
 */
package br.net.hartwig.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.net.hartwig.dao.UsuarioDAO;
import br.net.hartwig.domain.Usuario;

public class NovoUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession sessao = request.getSession();

		if (sessao.getAttribute("email") == null) {
			response.sendRedirect("Login");
		}

		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>CRUD com Servlets</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Novo Usuario</h1>");
			
			out.println("Seu ip: "+ sessao.getAttribute("ip"));
			
			out.println("<hr/>");
			out.println("<a href='NovoUsuario'>Novo Usuario</a>");
			out.println("<br/>");
			out.println("<a href='ListarUsuarios'>Listar Usuarios</a>");
			out.println("<br/>");
			out.println("<a href='Login?msg=logoff'>Sair</a>");
			out.println("<hr/>");
			out.println("<form method='POST'>");
			out.println("<h1>Dados do Usuario</h1>");
			out.println("<label>Nome</label>");
			out.println("<input type='text' name='edtNome'>");
			out.println("<br/>");
			out.println("<label>E-mail</label>");
			out.println("<input type='text' name='edtEmail'>");
			out.println("<br/>");
			out.println("<label>Senha</label>");
			out.println("<input type='password' name='edtSenha'>");
			out.println("<br/>");
			out.println("<input type='submit' value='Salvar'>");
			out.println("<br/>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		String nome = request.getParameter("edtNome").trim();
		String email = request.getParameter("edtEmail").trim();
		String senha = request.getParameter("edtSenha").trim();

		if (!nome.isEmpty() || !email.isEmpty() || !senha.isEmpty()) {

			Usuario usuario = new Usuario();
			UsuarioDAO usuarioDao = new UsuarioDAO();

			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setSenha(senha);

			try {
				if (usuarioDao.save(usuario)) {
					out.println("Usuario salvo com sucesso!");
					response.sendRedirect("ListarUsuarios");
				}
			} catch (Exception ex) {
				if (ex.getMessage().trim().startsWith("Duplicate entry")) {
					out.println("Este e-mail ja esta cadastrado!");
				} else {
					out.println("Ocorreu um erro ao salvar");
					ex.printStackTrace();
				}
			}

		} else {
			out.println("Preencha os dados corretamente");
		}

	}

}
