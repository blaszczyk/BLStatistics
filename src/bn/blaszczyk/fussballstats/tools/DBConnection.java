package bn.blaszczyk.fussballstats.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class DBConnection
{
	private static Connection dbConn;
	private static String connectionString;

	public static void showErrorMsg( Exception e)
	{
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler beim Zugriff auf die Datenbank", JOptionPane.ERROR_MESSAGE );
	}
	
	public static boolean connectToDatabase(String classForName, String connectionString, String userID, String passWord)
	{
		try
		{
			Class.forName(classForName).newInstance();
			dbConn = DriverManager.getConnection(connectionString, userID, passWord);
			DBConnection.connectionString = connectionString;
			return true;
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e)
		{
			dbConn = null;
			DBConnection.connectionString = null;
			showErrorMsg(e);
		}
		return false;
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
		connectionString = null;
	}
	
	public static String getCatalog()
	{
		if (dbConn != null)
			try
			{
				return dbConn.getCatalog();
			}
			catch (Exception ex)
			{
			}
		return "";
	}


	public static Connection getConnection()
	{
		return dbConn;
	}


	public static String getConnectionString()
	{
		return connectionString;
	}
	
	
	public static int executeNonQuery(String sql)
	{
		Statement stmt;
		int retValue = 0;
		if (dbConn == null)
			return retValue;
		try
		{
			stmt = dbConn.createStatement();
			retValue = stmt.executeUpdate(sql);
			stmt.close();
		}
		catch (SQLException e)
		{
			showErrorMsg(e);
		}
		return retValue;
	}
	
	
	public static Object executeScalar(String sql)
	{
		Statement stmt;
		Object retValue = null;
		if (dbConn == null)
			return retValue;
		try
		{
			stmt = dbConn.createStatement();
			ResultSet rSet = stmt.executeQuery(sql);
			// Liest den ersten Datensatz aus dem ResultSet
			rSet.next();
			// Die erste Saplte der ersten Zeile zum Rückgabewert zuweisen.
			retValue = rSet.getObject(1);
			stmt.close();
		}
		catch (SQLException e)
		{
			showErrorMsg(e);
		}
		return retValue;
	}

	public static ResultSet executeQuery(String sql)
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
				showErrorMsg(e);
			}
		return null;
	}

	public static int executeUpdate(String sql)
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
				showErrorMsg(e);
			}
		return -1;
	}
	
	
	public static PreparedStatement prepareStatement(String sql)
	{
		if (dbConn == null)
			return null;
		try
		{
			return dbConn.prepareStatement(sql);	
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler beim Vorbereiten der SQL-Anweisung", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	
	public static void beginTransaction()
	{
		if (dbConn == null)
			return;
		try
		{	
			dbConn.setAutoCommit(false);
		}
		catch (SQLException e)
		{
			showErrorMsg(e);
		}
	}
	
	public static void commitTransaction()
	{
		if (dbConn == null)
			return;
		try
		{
			// Keine Transaktionsschleife geöffnet
			if (dbConn.getAutoCommit())
				return;
			
			dbConn.commit();
			dbConn.setAutoCommit(true);
			
		}
		catch (SQLException e)
		{
			showErrorMsg(e);
		}
	}
	
	public static void rollbackTransaction()
	{
		if (dbConn == null)
			return;
		try
		{
			// Keine Transaktionsschleife geöffnet
			if (dbConn.getAutoCommit())
				return;
			dbConn.rollback();
			dbConn.setAutoCommit(true);
		}
		catch (SQLException e)
		{
			showErrorMsg(e);
		}
	}

}
