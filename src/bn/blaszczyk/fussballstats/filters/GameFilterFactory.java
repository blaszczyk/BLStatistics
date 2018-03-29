package bn.blaszczyk.fussballstats.filters;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.Team;

public class GameFilterFactory
{
	
	/*
	 * Goal Filters
	 */
	public static Filter<Game> createGoalFilter(int goals)
	{
		Filter<Game> f = g -> getGoals(g) == goals;
		return f;
	}
	
	public static Filter<Game> createGoalMinFilter(int minGoals)
	{
		Filter<Game> f = g -> getGoals(g) >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> getGoals(g) <= maxGoals;
		return f;
	}

	private static int getGoals(Game g)
	{
		return g.getGoalsAway() + g.getGoalsHome();
	}
	
	/*
	 * Home Goal Filters
	 */
	public static Filter<Game> createGoalHFilter(int goals)
	{
		Filter<Game> f = g -> g.getGoalsHome() == goals;
		return f;
	}
	public static Filter<Game> createGoalHMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoalsHome() >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalHMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoalsHome() <= maxGoals;
		return f;
	}
	/*
	 * Away Goal Filters
	 */
	public static Filter<Game> createGoalAFilter(int goals)
	{
		Filter<Game> f = g -> g.getGoalsAway() == goals;
		return f;
	}
	public static Filter<Game> createGoalAMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoalsAway() >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalAMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoalsAway() <= maxGoals;
		return f;
	}
	/*
	 * Goal Difference Filters
	 */
	public static Filter<Game> createGoalDiffFilter(int goals)
	{
		Filter<Game> f = g -> getDiff(g) == goals;
		return f;
	}
	public static Filter<Game> createGoalDiffMinFilter(int minGoals)
	{
		Filter<Game> f = g -> getDiff(g) >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalDiffMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> getDiff(g) <= maxGoals;
		return f;
	}
	private static int getDiff(final Game g)
	{
		return g.getGoalsHome() - g.getGoalsAway();
	}

	/*
	 * Basic Team Filters
	 */
	public static Filter<Game> createTeamFilter(Team team) 
	{
		Filter<Game> f = g ->g.getTeamHome().equals(team) || g.getTeamAway().equals(team);
		return f;
	}
	
	public static Filter<Game> createTeamHomeFilter(Team team) 
	{
		Filter<Game> f = g -> g.getTeamHome().equals(team);
		return f;
	}
	
	public static Filter<Game> createTeamAwayFilter(Team team) 
	{
		Filter<Game> f = g -> g.getTeamAway().equals(team);
		return f;
	}
	
	/*
	 * SubLeague Filters
	 */	
	public static Filter<Game> createSubLeagueFilter(Collection<Team> teams)
	{
		Filter<Game> f = g -> teams.contains(g.getTeamHome()) && teams.contains(g.getTeamAway());
		return f;
	}
	
	public static Filter<Game> createSubLeagueFilter(Team... teams)
	{
		return createSubLeagueFilter(Arrays.asList(teams));
	}

	/*
	 * MatchDay filters
	 */
	public static Filter<Game> createMatchDayFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchday().getCount().intValue() == matchDay;
		return f;
	}
	public static Filter<Game> createMatchDayMinFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchday().getCount().intValue() >= matchDay;
		return f;
	}
	public static Filter<Game> createMatchDayMaxFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchday().getCount().intValue() <= matchDay;
		return f;
	}

	/*
	 * Date Filters
	 */
	public static Filter<Game> createDateFilter(Date date)
	{
		Filter<Game> f = g -> g.getDate().equals(date);
		return f;
	}
	
	public static Filter<Game> createDateMinFilter(Date date)
	{
		Filter<Game> f = g -> g.getDate().compareTo(date)>=0;
		return f;
	}
	
	public static Filter<Game> createDateMaxFilter(Date date)
	{
		Filter<Game> f = g -> g.getDate().compareTo(date)<=0;
		return f;
	}
	
	/*
	 * DayOfWeek Filter
	 */
	public static Filter<Game> createDayOfWeekFilter(int dayOfWeek) // 1 = Sunday ... 7 = Saturday
	{
		final Calendar calendar = new GregorianCalendar();
		Filter<Game> f = g -> {
			calendar.setTime(g.getDate()); 
			return calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek;
		};
		return f;
	}
	
}
