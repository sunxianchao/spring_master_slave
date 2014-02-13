package me.sunxc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestCon {
	public static void main(String[] args) throws SQLException {
		Connection conn = null;

		try {
			String userName = "root";
			String password = "123123";

			String url = "jdbc:mysql://192.168.78.1:3306/test?characterEncoding=UTF-8";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			System.out.println("Database connection established");
		} catch (Exception e) {
			System.err.println("Cannot connect to database server");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
					System.out.println("Database Connection Terminated");
				} catch (Exception e) {
				}
			}
		}
	}
}
