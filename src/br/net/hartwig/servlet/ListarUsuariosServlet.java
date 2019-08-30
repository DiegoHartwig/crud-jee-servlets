/**
 * @author diegohartwig.it@gmail.com
 *
 * 20 de ago de 2016
 */
package br.net.hartwig.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.net.hartwig.dao.UsuarioDAO;
import br.net.hartwig.domain.Usuario;

public class ListarUsuariosServlet extends HttpServlet {
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
			out.println("<h1>Usuarios</h1>");
			
			out.println("Seu ip: "+ sessao.getAttribute("ip"));
			
			out.println("<hr/>");
			out.println("<a href='NovoUsuario'>Novo Usuario</a>");
			out.println("<br/>");
			out.println("<a href='ListarUsuarios'>Listar Usuarios</a>");
			out.println("<br/>");
			out.println("<a href='Login?msg=logoff'>Sair</a>");
			out.println("<br/><br/>");
			out.println("<hr/>");

			out.println("<table width='100%' border='1'>");
			out.println("<tr>");
			out.println("<td>Id</td>");
			out.println("<td>Nome</td>");
			out.println("<td>E-mail</td");
			out.println("<td></td");
			out.println("</tr>");

			UsuarioDAO usuarioDao = new UsuarioDAO();

			List<Usuario> usuarios = usuarioDao.getAll();

			for (Usuario usuario : usuarios) {
				out.println("<tr>");
				out.println("<td>");
				out.println(usuario.getId());
				out.println("</td>");
				out.println("<td>");
				out.println(usuario.getNome());
				out.println("</td>");
				out.println("<td>");
				out.println(usuario.getEmail());
				out.println("</td>");
				out.println("<td>");
				out.println("<a href='EditarUsuario?id=" + usuario.getId() + "'>Editar</a>");
				out.println("</td>");
				out.println("<td>");
				out.println("<a href='ListarUsuarios?id=" + usuario.getId() + "'>Excluir</a>");
				out.println("</td>");
				out.println("</tr>");
			}

			out.println("</table");
			out.println("</body>");
			out.println("</html>");

			if (request.getParameter("id") != null) {
				int id = Integer.parseInt(request.getParameter("id"));

				UsuarioDAO dao = new UsuarioDAO();
				Usuario usuario = new Usuario();
				usuario.setId(id);
				if (dao.delete(usuario)) {
					out.println("Usuário deletado com sucesso!");
					response.sendRedirect("ListarUsuarios");
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	}

}
