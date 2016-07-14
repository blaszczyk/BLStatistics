package bn.blaszczyk.blstatistics.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.tools.BLException;

public class Game
{
	/*
	 * Entsprechend der Rückgabe von getResult()
	 */
	public static final int HOME = 1;
	public static final int DRAW = 0;
	public static final int AWAY = -1;
	
	public static final int WIN = HOME;
	public static final int LOSS = AWAY;
	
	/*
	 * Some useful Comparators
	 */
	public static final Comparator<Game> DATE_COMPARATOR = (g1,g2) -> g1.getDate().compareTo(g2.getDate());
	public static final Comparator<Game> DIFF_COMPARATOR = (g1,g2) -> Integer.compare(g2.getDiff(),g1.getDiff());
	public static final Comparator<Game> GOALS_COMPARATOR = (g1,g2) -> Integer.compare(g2.getGoals(),g1.getGoals());
	public static final Comparator<Game> DUEL_COMPARATOR = (g1,g2) -> {
		int result = 0;
		if( g1.containsTeam(g2.getTeam1()) && g1.containsTeam(g2.getTeam2()))
		{
			int teamSort1 = g1.getTeam1().compareTo(g1.getTeam2());
			int teamSort2 = g2.getTeam1().compareTo(g2.getTeam2());
			result += 4 * Integer.compare(g1.getDiff()*teamSort1,g2.getDiff()*teamSort2);
			result += 2 * GOALS_COMPARATOR.compare(g1, g2);
			result += DATE_COMPARATOR.compare(g1, g2);	
		}
		return Integer.signum(result);
	};
	
	/*
	 * Standard Date Format
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
	
	private int goals1;
	private int goals2;
	private String team1;
	private String team2;
	private Date date;
	
	/*
	 * Constructors
	 */
	public Game(int goals1, int goals2, String team1, String team2, Date date)
	{
		this.goals1 = goals1;
		this.goals2 = goals2;
		this.team1 = team1;
		this.team2 = team2;
		this.date = date;
	}
	
	//	String testGame = " 14.09.63: Hamburg - Frankfurt 3:0";
	public Game(String gameDetails) throws BLException
	{
		String[] split = gameDetails.trim().split(":");
		if(split.length != 3)
			throw new BLException("Wrong game Format in '" + gameDetails + "'");
		try
		{
			date = dateFormat.parse( split[0]);
		}
		catch (ParseException e)
		{
			throw new BLException("Wrong date Format in '" + gameDetails + "'",e);
		} 
		goals1 = Integer.parseInt( split[1].substring(split[1].lastIndexOf(' ')+1) );
		goals2 = Integer.parseInt(split[2]);
		String teams = split[1].substring(0, split[1].lastIndexOf(' '));
		split = teams.split("-");
		if(split.length != 2)
			throw new BLException("Wrong game Format in '" + gameDetails + "'");
		team1 = split[0].trim();
		team2 = split[1].trim();
	}
	
	/*
	 * only Getters
	 */
	public int getGoals1()
	{
		return goals1;
	}
	public int getGoals2()
	{
		return goals2;
	}

	public int getDiff()
	{
		return goals1 - goals2;
	}
	
	public int getGoals()
	{
		return goals1 + goals2;
	}
	
	public String getTeam1()
	{
		return team1;
	}
	public String getTeam2()
	{
		return team2;
	}
	
	public Date getDate()
	{
		return date;
	}

	public int getWinner()
	{
		switch(Integer.signum(goals1-goals2))
		{
		case 1:
			return HOME;
		case 0:
			return DRAW;
		case -1:
			return AWAY;
		}
		return 0;
		//Simpler but unsafe:		
//		return Integer.signum(goals1-goals2);
	}
	
	@Override
	public String toString()
	{
		return String.format(" %s: %15s - %15s %2d:%d" , dateFormat.format(date), team1, team2, goals1, goals2);
	}

	/*
	 * Specific useful Methods
	 */
	
	public boolean containsTeam(String team)
	{
		if(team != null)	
			return team.equals(team1) || team.equals(team2);
		return false;
	}
	
	public static List<Game> filterGameList (Iterable<Game> games, Filter<Game> gameFilter)
	{
		List<Game> filteredGames = new ArrayList<>();
		for(Game game : games)
			if(gameFilter.check(game))
				filteredGames.add(game);
		return filteredGames;
	}
}
