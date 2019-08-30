/**
 * @author diegohartwig.it@gmail.com
 *
 * 20 de ago de 2016
 */
package br.net.hartwig.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
			out.println("<h1>Gerenciamento de Usuarios</h1>");
			
			out.println("Seu ip: "+ sessao.getAttribute("ip"));
			
			out.println("<hr/>");
			out.println("<a href='NovoUsuario'>Novo Usuario</a>");
			out.println("<br/>");
			out.println("<a href='ListarUsuarios'>Listar Usuarios</a>");
			out.println("<br/>");
			out.println("<a href='Login?msg=logoff'>Sair</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {

	}

}
