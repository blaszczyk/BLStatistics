package bn.blaszczyk.fussballstats.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection
{
	private static Connection dbConn;

	public static void connectToDatabase(String classForName, String connectionString, String userID, String passWord) throws FussballException
	{
		try
		{
			Class.forName(classForName).newInstance();
			dbConn = DriverManager.getConnection(connectionString, userID, passWord);
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e)
		{
			dbConn = null;
			throw new FussballException("Fehler beim Zugriff auf die Datenbank", e);
		}
	}
	
	
	public static void closeConnection()
	{
		if (dbConn == null)
			return;
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{}
		dbConn = null;
	}

	public static Connection getConnection()
	{
		return dbConn;
	}

	public static ResultSet executeQuery(String sql) throws FussballException
	{
		Statement stmt;
		if (dbConn != null)
			try
			{
				stmt = dbConn.createStatement();
				return stmt.executeQuery(sql);
			}
			catch (SQLException e)
			{
				throw new FussballException("Fehler beim Zugriff auf die Datenbank", e);
			}
		return null;
	}

	public static int executeUpdate(String sql) throws FussballException
	{
		Statement stmt;
		if (dbConn != null)
			try
			{
				stmt = dbConn.createStatement();
				return stmt.executeUpdate(sql);
			}
			catch (SQLException e)
			{
				throw new FussballException("Fehler beim Zugriff auf die Datenbank", e);
			}
		return -1;
	}
	
}
