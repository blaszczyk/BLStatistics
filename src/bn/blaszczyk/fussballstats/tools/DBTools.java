package bn.blaszczyk.fussballstats.tools;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;

public class DBTools
{
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private static String server = "localhost";
	private static String port = "3306";
	private static String dbName = "fussballspiele";
	private static String user = "root";
	private static String password = null;

	private static final String COL_PRIMARY_KEY = "PRIMARY_KEY";
	private static final String COL_TIMESTAMP = "TIMESTAMP";
	private static final String COL_SEASON = "SAISON";
	private static final String COL_MATCHDAY = "SPEILTAG";
	private static final String COL_DATE = "DATUM";
	private static final String COL_TEAM_H = "TEAM_H";
	private static final String COL_TEAM_A = "TEAM_A";
	private static final String COL_GOALS_H = "TORE_H";
	private static final String COL_GOALS_A = "TORE_A";
	
	private static final String INDEX_NAME = "MY_INDEX";
	

	public static String getServer()
	{
		return server;
	}

	public static String getPort()
	{
		return port;
	}

	public static String getDbName()
	{
		return dbName;
	}

	public static String getUser()
	{
		return user;
	}

	public static String getPassword()
	{
		return password;
	}

	public static void setAccessData(String server, String port, String dbName, String user, String password)
	{
		DBTools.server = server;
		DBTools.port = port;
		DBTools.dbName = dbName;
		DBTools.user = user;
		DBTools.password = password;
	}
	
	public static void openMySQLDatabase() throws FussballException
	{
		String connectionString, classForName;
		classForName = "com.mysql.jdbc.Driver";		
		connectionString = String.format("jdbc:mysql://%s:%s/%s", server, port, dbName);
		DBConnection.connectToDatabase(classForName, connectionString, user, password);
	}

	public static void insertSeason(Season season) throws FussballException
	{
		if(season.getGameCount() == 0)
			return;
		removeSeason(season);
		League league = season.getLeague();
		long nextPimaryKey = getNextKey(league);
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s) VALUES ",
				league.getSQLName(), COL_PRIMARY_KEY, COL_SEASON, COL_MATCHDAY, COL_DATE, 
				COL_TEAM_H, COL_TEAM_A, COL_GOALS_H, COL_GOALS_A ));
		boolean first = true;
		for(Game game : season)
		{
			if(first)
				first = false;
			else
				sql.append(", ");
			sql.append(String.format("(%d, %4d, %2d, %s, %s, %s, %2d, %2d)",
					nextPimaryKey++, season.getYear(), game.getMatchDay(), quote(DATE_FORMAT.format(game.getDate())), 
					quote(game.getTeamH()), quote(game.getTeamA()), game.getGoalsH(), game.getGoalsA() ) );	
		}
		DBConnection.executeUpdate(sql.toString());
	}

	public static void createTable(League league) throws FussballException
	{
		if(tableExists(league) || league.getSeasonCount() == 0)
			return;
		String sql = "CREATE TABLE " + league.getSQLName() + " ( ";
		sql += COL_PRIMARY_KEY + " INT(7) NOT NULL, ";
		sql += COL_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
		sql += COL_SEASON + " INT(4) NOT NULL, ";
		sql += COL_MATCHDAY + " int(2) NOT NULL, ";
		sql += COL_DATE + " DATE NOT NULL, ";
		sql += COL_TEAM_H + " VARCHAR(40) COLLATE latin1_general_ci NOT NULL, ";
		sql += COL_TEAM_A + " VARCHAR(40) COLLATE latin1_general_ci NOT NULL, ";
		sql += COL_GOALS_H + " INT(2) NOT NULL, ";
		sql += COL_GOALS_A + " INT(2) NOT NULL ";
		sql += ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci";
		DBConnection.executeUpdate(sql);
		sql = String.format("ALTER TABLE %s ADD PRIMARY KEY (%s), ADD KEY " + INDEX_NAME + " (%s, %s, %s, %s)",
				league.getSQLName(), COL_PRIMARY_KEY, COL_SEASON, COL_MATCHDAY, COL_TEAM_H, COL_TEAM_A );
		DBConnection.executeUpdate(sql);
	}

	
	public static void loadSeason(Season season) throws FussballException
	{
		List<Game> games = new ArrayList<>();
		String sql = String.format( "SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %4d",
				COL_MATCHDAY, COL_DATE, COL_TEAM_H, COL_TEAM_A, COL_GOALS_H, COL_GOALS_A,
				season.getLeague().getSQLName(), COL_SEASON, season.getYear() );
		ResultSet rSet = DBConnection.executeQuery(sql);
		try
		{
			while(rSet.next())
			{
				int matchDay = rSet.getInt(COL_MATCHDAY);
				java.util.Date date = new Date( rSet.getDate(COL_DATE).getTime() );
				String teamH = rSet.getString(COL_TEAM_H);
				String teamA = rSet.getString(COL_TEAM_A);
				int goalsH = rSet.getInt(COL_GOALS_H);
				int goalsA = rSet.getInt(COL_GOALS_A);
				Game game = new Game(matchDay, date, teamH, teamA, goalsH, goalsA);
				games.add(game);
			}
			season.setGames(games);
		}
		catch (SQLException e)
		{
			throw new FussballException("Fehler beim Zugriff auf Datenbank",e);
		}
	}

	public static void dropTable(League league) throws FussballException
	{
		String sql = "DROP TABLE " + league.getSQLName();
		DBConnection.executeUpdate(sql);
	}
	
	public static boolean tableExists(League league) throws FussballException
	{
		try
		{
			DatabaseMetaData meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, league.getSQLName(), null);
			return res.next();
		}
		catch (SQLException e)
		{
			throw new FussballException("Fehler beim Zugriff auf Datenbank",e);
		}
	}
	
	public static void removeSeason(Season season) throws FussballException
	{
		String sql = String.format("DELETE FROM %s WHERE %s = %4d", season.getLeague().getSQLName(), COL_SEASON, season.getYear() );
		DBConnection.executeUpdate(sql);
	}
	
	private static String quote(String value)
	{
		return "'" + value.replaceAll("'", "''") + "'";
	}

	private static long getNextKey(League league) throws FussballException
	{
		String sql = String.format("SELECT MAX(%s) FROM %s", COL_PRIMARY_KEY, league.getSQLName());
		ResultSet rSet = DBConnection.executeQuery(sql);
		try
		{
			if(rSet.next())
				return rSet.getLong(1) + 1;
		}
		catch (SQLException e)
		{
			throw new FussballException("Fehler beim Zugriff auf Datenbank",e);
		}
		return 1;
	}
	
	public static void connectToSQLServer() throws FussballException
	{
		String connectionString, classForName;
		classForName = "com.mysql.jdbc.Driver";		
		connectionString = "jdbc:mysql://" + server + ":3306";
		DBConnection.connectToDatabase(classForName, connectionString, user, password);
	}
	
	public static boolean databaseExists() throws FussballException
	{
		ResultSet rs;
		try
		{
			rs = DBConnection.getConnection().getMetaData().getCatalogs();
			while(rs.next())
				if(rs.getString(1).equals(dbName))
					return true;
		}
		catch (SQLException e)
		{
			throw new FussballException("Fehler beim Zugriff auf SQL Server",e);
		}
		return false;
	}
	
	public static void createDatabase() throws FussballException
	{
		String sql = "CREATE DATABASE " + dbName;
		DBConnection.executeUpdate(sql);
	}
	
	public static void main(String[] args) throws Exception
	{
		String connectionString, classForName;
		classForName = "com.mysql.jdbc.Driver";		
		connectionString = "jdbc:mysql://" + server + ":3306/" + dbName;
		DBConnection.connectToDatabase(classForName, connectionString, user, password);
		
		ResultSet rs = DBConnection.getConnection().getMetaData().getCatalogs();
		while(rs.next())
			System.out.println(rs.getString(1) );
		DBConnection.closeConnection();
	}

}
