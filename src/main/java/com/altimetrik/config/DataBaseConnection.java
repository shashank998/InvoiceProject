package com.altimetrik.config;

import java.sql.*;
import java.util.Properties;

public class DataBaseConnection {

	public static Connection dbConnection() throws SQLException {
		String userName = "root";
		String password = "root";
		String url1 = "jdbc:mysql://localhost:3306/invoicedata";
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		Connection conn = DriverManager.getConnection(url1, userName, password);
		return conn;
	}
}
