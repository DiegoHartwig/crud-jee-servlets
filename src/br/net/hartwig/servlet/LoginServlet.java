/**
 * @author diegohartwig.it@gmail.com
 *
 * 20 de ago de 2016
 */
package br.net.hartwig.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.net.hartwig.dao.UsuarioDAO;
import br.net.hartwig.domain.Usuario;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		String dadosEmail = "";

		Cookie[] cookie = request.getCookies();

		if (cookie != null) {
			for (Cookie c : cookie) {
				if (c.getName().equals("dados_email")) {
					dadosEmail = c.getValue();
				}
			}

		}

		out.println("<html>");
		out.println("<head>");
		out.println("<title>CRUD com Servlets</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Login</h1>");
		out.println("<hr/>");
		out.println("<form method='POST'>");
		out.println("<h1>Dados do Usuario</h1>");
		out.println("<label>E-mail</label>");
		out.println("<input type='text' name='edtEmail' value='" + dadosEmail + "'>");
		out.println("<br/>");
		out.println("<label>Senha</label>");
		out.println("<input type='password' name='edtSenha'>");
		out.println("<br/>");
		out.println("<input type='submit' value='Entrar'>");
		out.println("<br/>");

		if (request.getParameter("msg") != null && request.getParameter("msg").equals("error")) {
			out.println("<span style='color: #FF4B4B'>Dados de Login incorretos</span>");
		}

		if (request.getParameter("msg") != null && request.getParameter("msg").equals("logoff")) {
			HttpSession sessao = request.getSession();
			sessao.invalidate();
			out.println("<span style='color: #FF4B4B'>Deslogado com sucesso</span>");
		}

		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		String email = request.getParameter("edtEmail").trim();
		String senha = request.getParameter("edtSenha").trim();

		Cookie cookie = new Cookie("dados_email", email);
		cookie.setMaxAge(86400);
		response.addCookie(cookie);

		if (!email.isEmpty() || !senha.isEmpty()) {

			Usuario usuario = new Usuario();
			UsuarioDAO usuarioDao = new UsuarioDAO();

			usuario.setEmail(email);
			usuario.setSenha(senha);

			try {
				if (usuarioDao.login(usuario) == true) {
					HttpSession sessao = request.getSession();
					sessao.setAttribute("email", email);
					sessao.setAttribute("ip", InetAddress.getLocalHost().getHostAddress());
					response.sendRedirect("ListarUsuarios");
				} else {
					response.sendRedirect("Login?msg=error");
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
