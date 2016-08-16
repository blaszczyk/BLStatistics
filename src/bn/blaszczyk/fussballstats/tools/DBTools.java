package bn.blaszczyk.fussballstats.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Stack;
import java.sql.DatabaseMetaData;
import java.sql.Date;


import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;

public class DBTools
{
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
	

	
	public static boolean openMySQLDatabase()
	{
		String connectionString, classForName;
		classForName = "com.mysql.jdbc.Driver";		
		connectionString = "jdbc:mysql://" + SERVER + ":3306/" + DB_NAME;
		return DBConnection.connectToDatabase(classForName, connectionString, "root", null);
			
	}
	
	public static void updateGames(Season season)
	{
		League league = season.getLeague();
		
		PreparedStatement prepStatInsert = prepareInsertGame(league);
		PreparedStatement prepStatUpdate = prepareUpdateGame(league);
		PreparedStatement prepStatPrimKey = prepareGetPrimaryKey(league);
		long nextPimaryKey = getNextKey(league);
		for(Game game : season.getAllGames())
		{
			long primaryKey = getPrimaryKeyPrepared(prepStatPrimKey, season, game);
			if(primaryKey < 0)
				insertGamePrepared(prepStatInsert, nextPimaryKey++, season, game);
			else
				updateGamePrepared(prepStatUpdate, primaryKey, season,game);
		}
	}

	public static boolean loadGames(Season season)
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
			return true;
		}
		catch (SQLException e)
		{
			DBConnection.showErrorMsg(e);
		}
		return false;
	}
	
	public static void createTable(League league)
	{
		String sql = "CREATE TABLE " + league.getSQLName() + " ( ";
		sql += quote(COL_PRIMARY_KEY) + " int(7) NOT NULL, ";
		sql += quote(COL_TIMESTAMP) + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
		sql += quote(COL_SEASON) + " int(4) NOT NULL, ";
		sql += quote(COL_MATCHDAY) + " int(2) NOT NULL, ";
		sql += quote(COL_DATE) + " date NOT NULL, ";
		sql += quote(COL_TEAM_H) + " varchar(40) COLLATE latin1_general_ci NOT NULL, ";
		sql += quote(COL_TEAM_A) + " varchar(40) COLLATE latin1_general_ci NOT NULL, ";
		sql += quote(COL_GOALS_H) + " int(2) NOT NULL, ";
		sql += quote(COL_GOALS_A) + " int(2) NOT NULL ";
		sql += ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci";
		System.out.println(sql);
		DBConnection.executeUpdate(sql);
		sql = String.format("ALTER TABLE %s ADD PRIMARY KEY (%s), ADD KEY MYKEY (%s, %s, %s, %s)",
				league.getSQLName(), quote(COL_PRIMARY_KEY), quote(COL_SEASON), quote(COL_MATCHDAY), quote(COL_TEAM_H), quote(COL_TEAM_A) );
		DBConnection.executeUpdate(sql);
	}

	public static String quote(String value)
	{
		return value;//"'" + value.replaceAll("'", "''") + "'";
	}
	
	private static PreparedStatement prepareUpdateGame(League league)
	{
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?  WHERE %s = ?",
				league.getSQLName(), COL_DATE, COL_GOALS_H, COL_GOALS_A, COL_PRIMARY_KEY );
		return DBConnection.prepareStatement(sql);
	}

	private static boolean updateGamePrepared(PreparedStatement prepStatement, long primaryKey, Season season, Game game)
	{
		try
		{
			prepStatement.setDate(1, new Date( game.getDate().getTime()));
			prepStatement.setInt(2, game.getGoalsH());
			prepStatement.setInt(3, game.getGoalsA());
			prepStatement.setLong(4, primaryKey);
			prepStatement.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			DBConnection.showErrorMsg(e);
		}
		return false;
	}
	
	private static PreparedStatement prepareGetPrimaryKey(League league)
	{
		String sql = String.format("SELECT %s FROM %s WHERE %s = ? AND %s = ? AND %s = ? AND %s = ?", 
				COL_PRIMARY_KEY, league.getSQLName(), COL_SEASON, COL_DATE, COL_TEAM_H, COL_TEAM_A );
		return DBConnection.prepareStatement(sql);
	}
	
	private static long getPrimaryKeyPrepared(PreparedStatement prepStatement, Season season, Game game)
	{
		try
		{
			prepStatement.setInt(1, season.getYear());
			prepStatement.setInt(2, game.getMatchDay());
			prepStatement.setString(3, game.getTeamH());
			prepStatement.setString(4, game.getTeamA());
			ResultSet rSet = prepStatement.executeQuery();
			if(rSet.next())
			{
				long primaryKey = rSet.getLong(1);
				rSet.close();
				return primaryKey;
			}
		}
		catch( SQLException e)
		{
			DBConnection.showErrorMsg(e);
		}
		return -1;
	}

	private static long getNextKey(League league)
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
			DBConnection.showErrorMsg(e);
		}
		return 1;
	}
	
	private static PreparedStatement prepareInsertGame(League league)
	{
		String sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
				league.getSQLName(), COL_PRIMARY_KEY, COL_SEASON, COL_MATCHDAY, COL_DATE, COL_TEAM_H, COL_TEAM_A, COL_GOALS_H, COL_GOALS_A );
		return DBConnection.prepareStatement(sql);
	}

	private static boolean insertGamePrepared(PreparedStatement prepStatement, long primaryKey, Season season, Game game)
	{
		try
		{
			prepStatement.setLong(1, primaryKey);
			prepStatement.setInt(2, season.getYear());
			prepStatement.setInt(3, game.getMatchDay());
			prepStatement.setDate(4, new Date(game.getDate().getTime()));
			prepStatement.setString(5, game.getTeamH());
			prepStatement.setString(6, game.getTeamA());
			prepStatement.setInt(7, game.getGoalsH());
			prepStatement.setInt(8, game.getGoalsA());
			prepStatement.executeUpdate();
			return true;
		}
		catch( SQLException e)
		{
			DBConnection.showErrorMsg(e);
		}
		return false;
	}
	
	private static boolean tableExists(League league)
	{
		DatabaseMetaData meta;
		try
		{
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(DBConnection.getCatalog(), null, league.getSQLName(),null);
			return res.next();
		}
		catch (SQLException e)
		{
			DBConnection.showErrorMsg(e);
		}
		return false;
	}
	
	public static void main(String[] args)
	{
		List<League> leagues = FileIO.initLeagues();
		openMySQLDatabase();
		for(League league : leagues)
		{
			if(!tableExists(league))
				createTable(league);
			for(Season season : league)
				updateGames(season);
		}
	}

}
