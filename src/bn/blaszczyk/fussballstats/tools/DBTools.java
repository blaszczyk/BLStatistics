package bn.blaszczyk.fussballstats.tools;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Stack;


import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;

public class DBTools
{
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final String DB_NAME = "fussballspiele";
	private static final String SERVER = "localhost";


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
	

	
	public static void openMySQLDatabase() throws FussballException
	{
		String connectionString, classForName;
		classForName = "com.mysql.jdbc.Driver";		
		connectionString = "jdbc:mysql://" + SERVER + ":3306/" + DB_NAME;
		DBConnection.connectToDatabase(classForName, connectionString, "root", null);
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
				season.getLeague().getSQLName(), COL_PRIMARY_KEY, COL_SEASON, COL_MATCHDAY, COL_DATE, 
				COL_TEAM_H, COL_TEAM_A, COL_GOALS_H, COL_GOALS_A ));
		boolean first = true;
		for(Game game : season.getAllGames())
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
		sql += COL_PRIMARY_KEY + " int(7) NOT NULL, ";
		sql += COL_TIMESTAMP + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
		sql += COL_SEASON + " int(4) NOT NULL, ";
		sql += COL_MATCHDAY + " int(2) NOT NULL, ";
		sql += COL_DATE + " date NOT NULL, ";
		sql += COL_TEAM_H + " varchar(40) COLLATE latin1_general_ci NOT NULL, ";
		sql += COL_TEAM_A + " varchar(40) COLLATE latin1_general_ci NOT NULL, ";
		sql += COL_GOALS_H + " int(2) NOT NULL, ";
		sql += COL_GOALS_A + " int(2) NOT NULL ";
		sql += ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci";
		DBConnection.executeUpdate(sql);
		sql = String.format("ALTER TABLE %s ADD PRIMARY KEY (%s), ADD KEY " + INDEX_NAME + " (%s, %s, %s, %s)",
				league.getSQLName(), COL_PRIMARY_KEY, COL_SEASON, COL_MATCHDAY, COL_TEAM_H, COL_TEAM_A );
		DBConnection.executeUpdate(sql);
	}

	
	public static void loadSeason(Season season) throws FussballException
	{
		Stack<Game> games = new Stack<>();
		String sql = String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %d",
				COL_MATCHDAY,COL_DATE,COL_TEAM_H,COL_TEAM_A,COL_GOALS_H,COL_GOALS_A,season.getLeague().getSQLName(),COL_SEASON,season.getYear());
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
				games.push(game);
			}
			season.consumeGames(games);
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
	
	private static void removeSeason(Season season) throws FussballException
	{
		String sql = String.format("DELETE FROM %s WHERE %s = %4d", season.getLeague().getSQLName(), COL_SEASON, season.getYear() );
		DBConnection.executeUpdate(sql);
	}
	
	private static boolean tableExists(League league) throws FussballException
	{
		DatabaseMetaData meta;
		try
		{
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, league.getSQLName(),null);
			return res.next();
		}
		catch (SQLException e)
		{
			throw new FussballException("Fehler beim Zugriff auf Datenbank",e);
		}
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

}