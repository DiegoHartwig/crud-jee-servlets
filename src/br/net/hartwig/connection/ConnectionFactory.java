/**
 * @author diegohartwig.it@gmail.com
 *
 * 20 de ago de 2016
 */
package br.net.hartwig.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	public static Connection getConnection() {
		try {
			Properties props = new Properties();
			try (FileInputStream config = new FileInputStream("/home/hartwig/Projetos/crud-jee-servlets/src/config.properties")) {
				props.load(config);
			}

			String URL = props.getProperty("URL");
			String USER = props.getProperty("USER");
			String PASS = props.getProperty("PASS");
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {				
				e.printStackTrace();
			}

			return DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException | IOException ex) {
			throw new RuntimeException("Ocorreu um erro na conexão!", ex);
		}
	}

	public static void closeConnection(Connection conn) {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				System.err.println("Ocorreu um erro" + ex);
			}
		}
	}

	public static void closeConnection(Connection conn, PreparedStatement stmt) {

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				System.err.println("Ocorreu um erro" + ex);
			}
		}
		closeConnection(conn);
	}

	public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				System.err.println("Ocorreu um erro" + ex);
			}
		}
		closeConnection(conn, stmt);
	}
}
