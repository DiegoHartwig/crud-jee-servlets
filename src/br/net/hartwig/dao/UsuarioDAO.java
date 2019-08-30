/**
 * @author diegohartwig.it@gmail.com
 *
 * 20 de ago de 2016
 */
package br.net.hartwig.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import br.net.hartwig.connection.ConnectionFactory;
import br.net.hartwig.domain.Usuario;

public class UsuarioDAO {

	private Connection conn = null;

	public UsuarioDAO() {
		conn = ConnectionFactory.getConnection();
	}

	public boolean save(Usuario usuario) throws Exception {
		String sql = "INSERT INTO usuarios (nome, email, senha)" + "VALUES(?,?,?)";

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.executeUpdate();

			return true;
		} catch (SQLIntegrityConstraintViolationException ex) {
			System.out.println("Este e-mail ja esta cadastrado!");
			Throwable t = ex;
			boolean cont = true;
			while (t != null) {
				if (t.getMessage().startsWith("Duplicate entry")) {
					cont = false;
					throw new Exception("Duplicate entry");
				}
				t = t.getCause();
			}
			if (cont) {
				throw new Exception(ex.getMessage());
			}

			return false;

		} finally {
			ConnectionFactory.closeConnection(conn, stmt);
		}
	}

	public List<Usuario> getAll() {

		String sql = "SELECT id, nome, email FROM usuarios";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Usuario> usuarios = new ArrayList<>();

		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setEmail(rs.getString("email"));

				usuarios.add(usuario);
			}
		} catch (SQLException ex) {
			System.err.println("Ocorreu um erro: " + ex);
		} finally {
			ConnectionFactory.closeConnection(conn, stmt, rs);
		}
		return usuarios;
	}

	public boolean update(Usuario usuario) {

		String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.setInt(4, usuario.getId());
			stmt.executeUpdate();

			return true;
		} catch (SQLException ex) {
			System.err.println("Ocorreu um erro: " + ex);
			return false;
		} finally {
			ConnectionFactory.closeConnection(conn, stmt);
		}

	}

	public boolean delete(Usuario usuario) {

		String sql = "DELETE FROM usuarios WHERE id = ?";

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, usuario.getId());
			stmt.executeUpdate();

			return true;
		} catch (SQLException ex) {
			System.err.println("Ocorreu um erro: " + ex);
			return false;
		} finally {
			ConnectionFactory.closeConnection(conn, stmt);
		}

	}

	public Usuario getUsuarioById(Integer id) throws SQLException {

		String sql = "SELECT id, nome, email FROM usuarios WHERE id = ?";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		rs = stmt.executeQuery();

		Usuario usuario = new Usuario();

		while (rs.next()) {
			usuario.setId(rs.getInt("id"));
			usuario.setNome(rs.getString("nome"));
			usuario.setEmail(rs.getString("email"));
		}

		return usuario;
	}

	public Boolean login(Usuario usuario) throws SQLException {

		String sql = "SELECT id, nome, email FROM usuarios WHERE email = ? AND senha = ?";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		stmt = conn.prepareStatement(sql);
		stmt.setString(1, usuario.getEmail());
		stmt.setString(2, usuario.getSenha());
		rs = stmt.executeQuery();

		if (rs.next()) {
			return true;
		} else {
			return false;
		}

	}

}
