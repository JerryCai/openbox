package com.googlecode.openbox.db;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0DbPoolImpl implements DbPool {

	private static final Logger logger = LogManager.getLogger();

	public static final String DB_PROPERTIES = "c3p0.properties";

	private ComboPooledDataSource ds;

	public static C3p0DbPoolImpl newInstance(DbInfo dbInfo) {
		return new C3p0DbPoolImpl(dbInfo);
	}

	public static C3p0DbPoolImpl newInstance(String dbUrl, String dbUsername,
			String dbPassword, String driverClass) {
		return new C3p0DbPoolImpl(dbUrl, dbUsername, dbPassword, driverClass);
	}

	public static C3p0DbPoolImpl newInstance() {
		return new C3p0DbPoolImpl();
	}

	public static C3p0DbPoolImpl newInstance(String dbPropertiesPath) {
		C3p0DbPoolImpl instance = null;
		try {
			instance = new C3p0DbPoolImpl(dbPropertiesPath);
		} catch (Exception e) {
			logger.error("init C3p0 DBPool error !", e);
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			logger.error("get db connection from db pool failed !!!", e);
		}
		return null;
	}

	public void printConnectionInfo() {
		if (logger.isInfoEnabled()) {
			logger.info("\n\r");
			logger.info(ds.toString());
			logger.info("\n\r");
		}
	}

	public void execute(String sql) throws SQLException {
		Connection dbconn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			dbconn = getConnection();
			cs = dbconn.prepareCall(sql);
			rs = cs.executeQuery();
			ResultSetMetaData rsmd = cs.getMetaData();
			String columInfo = "| ";
			int columnCount = rsmd.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				columInfo = columInfo + rsmd.getColumnName(column) + " |";
			}
			logger.info(columInfo);
			while (rs.next()) {
				String result = "| ";
				for (int column = 1; column <= columnCount; column++) {
					result = result + rs.getString(column) + " |";
				}
				logger.info(result);
			}
		} catch (SQLException e) {
			logger.error("execute SQL [" + sql + "] error !", e);
			throw e;
		} finally {
			close(dbconn, cs, rs);
		}
	}

	// private construct methods

	public String toString() {
		return ds.toString();
	}

	public C3p0DbPoolImpl() {
		Properties p = getProperies();
		ds = new ComboPooledDataSource();
		setDataSource(p);
	}

	public C3p0DbPoolImpl(String dbPropertiesPath) {
		Properties p = getProperies(dbPropertiesPath);
		ds = new ComboPooledDataSource();
		setDataSource(p);
	}

	public C3p0DbPoolImpl(String dbUrl, String dbUsername, String dbPassword,
			String driverClass) {
		Properties p = getProperies();
		ds = new ComboPooledDataSource();
		setDataSource(p);
		DbInfo dbInfo = new DbInfo();
		dbInfo.setUrl(dbUrl);
		dbInfo.setUsername(dbUsername);
		dbInfo.setPassword(dbPassword);
		dbInfo.setDriverClass(driverClass);
		setDbConnectionInfo(dbInfo);
	}

	public C3p0DbPoolImpl(DbInfo dbInfo) {
		Properties p = getProperies();
		ds = new ComboPooledDataSource();
		setDataSource(p);
		this.setDbConnectionInfo(dbInfo);
	}

	private Properties getProperies() {
		return getProperies(DB_PROPERTIES);
	}

	private Properties getProperies(String path) {
		try {
			Properties p = new Properties();
			p.load(getClass().getClassLoader().getResourceAsStream(path));
			return p;
		} catch (IOException e) {
			String msg = "load " + DB_PROPERTIES + " error";
			logger.error(msg);
			throw new RuntimeException(msg, e);
		}
	}

	private void setDbConnectionInfo(DbInfo dbInfo) {
		ds.setJdbcUrl(dbInfo.getUrl());
		ds.setUser(dbInfo.getUsername());
		ds.setPassword(dbInfo.getPassword());
		setDbDriverClass(dbInfo.getDriverClass());
	}

	private void setDbDriverClass(String driverClass) {
		if (null != driverClass) {
			try {
				ds.setDriverClass(driverClass);
			} catch (PropertyVetoException e) {
				String msg = "set the db driverClass=[" + driverClass
						+ "]failed !!!";
				logger.error(msg, e);
				throw new RuntimeException(msg, e);
			}
		}
	}

	private void setDataSource(Properties properties) {
		if (ds != null) {
			setDbDriverClass(properties.getProperty("driverClass"));
			ds.setJdbcUrl(properties.getProperty("jdbcUrl"));
			ds.setUser(properties.getProperty("user"));
			ds.setPassword(properties.getProperty("password"));
			ds.setInitialPoolSize(Integer.parseInt(properties
					.getProperty("initialPoolSize")));
			ds.setMaxPoolSize(Integer.parseInt(properties
					.getProperty("maxPoolSize")));
			ds.setMinPoolSize(Integer.parseInt(properties
					.getProperty("minPoolSize")));
			ds.setAcquireIncrement(Integer.parseInt(properties
					.getProperty("acquireIncrement")));
			ds.setMaxStatementsPerConnection(Integer.parseInt(properties
					.getProperty("maxStatementsPerConnection")));
			ds.setNumHelperThreads(Integer.parseInt(properties
					.getProperty("numHelperThreads")));
			ds.setDebugUnreturnedConnectionStackTraces(Boolean
					.parseBoolean("debugUnreturnedConnectionStackTraces"));
			ds.setTestConnectionOnCheckout(Boolean
					.parseBoolean("testConnectionOnCheckout"));
			ds.setIdleConnectionTestPeriod(Integer.parseInt(properties
					.getProperty("idleConnectionTestPeriod")));
			ds.setPreferredTestQuery(properties
					.getProperty("preferredTestQuery"));
			ds.setMaxIdleTime(Integer.parseInt(properties
					.getProperty("maxIdleTime")));
			ds.setUnreturnedConnectionTimeout(Integer.parseInt(properties
					.getProperty("unreturnedConnectionTimeout")));
			ds.setCheckoutTimeout(Integer.parseInt(properties
					.getProperty("checkoutTimeout")));
		}
	}

	public void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
		close(rs);
		close(conn, stmt);
	}

	public void close(Connection conn, PreparedStatement stmt) {
		close(conn);
		close(stmt);
	}

	public void close(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (SQLException e) {
			String strMsg = "failed to close ResultSet";
			logger.error(strMsg, e);
			throw new RuntimeException(strMsg, e);
		}
	}

	public void close(PreparedStatement stmt) {
		if (stmt == null)
			return;

		try {
			stmt.close();
		} catch (SQLException e) {
			String strMsg = "failed to close PreparedStatement";
			logger.error(strMsg, e);
			throw new RuntimeException(strMsg, e);
		}
	}

	public void close(Connection conn) {
		if (conn == null){
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			String strMsg = "failed to close Connection";
			logger.error(strMsg, e);
			throw new RuntimeException(strMsg, e);
		}

	}
}
