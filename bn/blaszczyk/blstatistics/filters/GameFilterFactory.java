package bn.blaszczyk.blstatistics.filters;

import java.util.ArrayList;
import java.util.List;

import bn.blaszczyk.blstatistics.core.Game;

public class GameFilterFactory
{
	/*
	 * Goal Filters
	 */
	public static Filter<Game> getMinGoalFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoals() >= minGoals;
		return f;
	}
	public static Filter<Game> getMaxGoalFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoals() <= maxGoals;
		return f;
	}
	
	/*
	 * Result Filters
	 */
	public static Filter<Game> getWinnerFilter(int winner) 
	{
		Filter<Game> f = g -> g.getWinner() == winner;
		return f;
	}

	/*
	 * Basic Team Filters
	 */
	public static Filter<Game> getTeamFilter(String team) 
	{
		Filter<Game> f = g -> g.containsTeam(team);
		return f;
	}
	
	public static Filter<Game> getHomeTeamFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeam1().equals(team);
		return f;
	}
	
	public static Filter<Game> getAwayTeamFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeam2().equals(team);
		return f;
	}
	
	public static Filter<Game> getDuelFilter(String team1, String team2)
	{
		Filter<Game> f = g -> g.containsTeam(team1) && g.containsTeam(team2);
		return f;
	}

	
	/*
	 * Composite Filters
	 */
	public static Filter<Game> getTeamFilter(String...teams) 
	{
		List<Filter<Game>> filters = new ArrayList<>();
		for(String team : teams)
			filters.add( getTeamFilter(team) );
		return LogicalFilterFactory.getORFilter(filters);
	}
	
	public static Filter<Game> getTeamResultFilter(String team, int result) 
	{
		Filter<Game> isHome = getHomeTeamFilter(team);
		Filter<Game> isAway = getAwayTeamFilter(team);
		Filter<Game> homeWinner = getWinnerFilter(result);
		Filter<Game> awayWinner = getWinnerFilter(-result);
		Filter<Game> falseFilter = LogicalFilterFactory.getFALSEFilter();
		Filter<Game> isHomeWinner = LogicalFilterFactory.getIF_THEN_ELSEFilter(isHome, homeWinner, falseFilter);
		Filter<Game> isAwayWinner = LogicalFilterFactory.getIF_THEN_ELSEFilter(isAway, awayWinner, falseFilter);
		return LogicalFilterFactory.getORFilter(isHomeWinner,isAwayWinner);
	}

}
