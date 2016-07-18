package bn.blaszczyk.blstatistics.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import bn.blaszczyk.blstatistics.core.Game;

public abstract class GameFilter implements Filter<Game>
{
	/*
	 * MatchDay filters
	 */
	public static Filter<Game> getMatchDayFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchDay() == matchDay;
		return f;
	}
	public static Filter<Game> getMatchDayMinFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchDay() >= matchDay;
		return f;
	}
	public static Filter<Game> getMatchDayMaxFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchDay() <= matchDay;
		return f;
	}
	
	/*
	 * Goal Filters
	 */
	public static Filter<Game> getGoalFilter(int goals)
	{
		Filter<Game> f = g -> g.getGoals() == goals;
		return f;
	}
	public static Filter<Game> getGoalMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoals() >= minGoals;
		return f;
	}
	public static Filter<Game> getGoalMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoals() <= maxGoals;
		return f;
	}
	
	public static Filter<Game> getGoal1Filter(int goals)
	{
		Filter<Game> f = g -> g.getGoals1() == goals;
		return f;
	}
	public static Filter<Game> getGoal1MinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoals1() >= minGoals;
		return f;
	}
	public static Filter<Game> getGoal1MaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoals1() <= maxGoals;
		return f;
	}
	
	public static Filter<Game> getGoal2Filter(int goals)
	{
		Filter<Game> f = g -> g.getGoals2() == goals;
		return f;
	}
	public static Filter<Game> getGoal2MinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoals2() >= minGoals;
		return f;
	}
	public static Filter<Game> getGoal2MaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoals2() <= maxGoals;
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
	
	public static Filter<Game> getTeamHomeFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeam1().equals(team);
		return f;
	}
	
	public static Filter<Game> getTeamAwayFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeam2().equals(team);
		return f;
	}
	/*
	 * Advanced Team Filters
	 */
	public static Filter<Game> getDuelFilter(String team1, String team2)
	{
		Filter<Game> f = g -> g.containsTeam(team1) && g.containsTeam(team2);
		return f;
	}
	
	public static Filter<Game> getSubLeagueFilter(Collection<String> teams)
	{
		Filter<Game> f = g -> teams.contains(g.getTeam1()) && teams.contains(g.getTeam2());
		return f;
	}
	
	public static Filter<Game> getSubLeagueFilter(String... teams)
	{
		return getSubLeagueFilter(Arrays.asList(teams));
	}

	
	/*
	 * Composite Filters
	 */
	public static Filter<Game> getTeamFilter(String...teams) 
	{
		List<Filter<Game>> filters = new ArrayList<>();
		for(String team : teams)
			filters.add( getTeamFilter(team) );
		return LogicalFilter.getORFilter(filters);
	}
	
	public static Filter<Game> getTeamResultFilter(String team, int result) 
	{
		Filter<Game> isHome = getTeamHomeFilter(team);
		Filter<Game> isAway = getTeamAwayFilter(team);
		Filter<Game> homeWinner = getWinnerFilter(result);
		Filter<Game> awayWinner = getWinnerFilter(-result);
		Filter<Game> falseFilter = LogicalFilter.getFALSEFilter();
		Filter<Game> isHomeWinner = LogicalFilter.getIF_THEN_ELSEFilter(isHome, homeWinner, falseFilter);
		Filter<Game> isAwayWinner = LogicalFilter.getIF_THEN_ELSEFilter(isAway, awayWinner, falseFilter);
		return LogicalFilter.getORFilter(isHomeWinner,isAwayWinner);
	}
	
	/*
	 * The thing that a Filter<Game> does best.
	 */
	public static List<Game> filterGameList (Iterable<Game> games, Filter<Game> gameFilter)
	{
		List<Game> filteredGames = new ArrayList<>();
		for(Game game : games)
			if(gameFilter.check(game))
				filteredGames.add(game);
		return filteredGames;
	}

}
