package com.google.test.openbox.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbPool {

	Connection getConnection() throws SQLException;

	void close(Connection conn, PreparedStatement stmt, ResultSet rs);

	void close(Connection conn, PreparedStatement stmt);

	void close(ResultSet rs);

	void close(PreparedStatement stmt);

	void close(Connection conn);
	
	void execute(String sql) throws SQLException;

	void printConnectionInfo();
}
