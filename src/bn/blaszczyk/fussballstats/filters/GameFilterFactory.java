package bn.blaszczyk.fussballstats.filters;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import bn.blaszczyk.fussballstats.core.Game;

public class GameFilterFactory
{
	
	/*
	 * Goal Filters
	 */
	public static Filter<Game> createGoalFilter(int goals)
	{
		Filter<Game> f = g -> g.getGoals() == goals;
		return f;
	}
	public static Filter<Game> createGoalMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoals() >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoals() <= maxGoals;
		return f;
	}
	/*
	 * Home Goal Filters
	 */
	public static Filter<Game> createGoalHFilter(int goals)
	{
		Filter<Game> f = g -> g.getGoalsH() == goals;
		return f;
	}
	public static Filter<Game> createGoalHMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoalsH() >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalHMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoalsH() <= maxGoals;
		return f;
	}
	/*
	 * Away Goal Filters
	 */
	public static Filter<Game> createGoalAFilter(int goals)
	{
		Filter<Game> f = g -> g.getGoalsA() == goals;
		return f;
	}
	public static Filter<Game> createGoalAMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoalsA() >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalAMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoalsA() <= maxGoals;
		return f;
	}
	/*
	 * Goal Difference Filters
	 */
	public static Filter<Game> createGoalDiffFilter(int goals)
	{
		Filter<Game> f = g -> g.getDiff() == goals;
		return f;
	}
	public static Filter<Game> createGoalDiffMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getDiff() >= minGoals;
		return f;
	}
	public static Filter<Game> createGoalDiffMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getDiff() <= maxGoals;
		return f;
	}

	/*
	 * Basic Team Filters
	 */
	public static Filter<Game> createTeamFilter(String team) 
	{
		Filter<Game> f = g ->g.getTeamHAlias().equals(team) || g.getTeamAAlias().equals(team);
		return f;
	}
	
	public static Filter<Game> createTeamHomeFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeamHAlias().equals(team);
		return f;
	}
	
	public static Filter<Game> createTeamAwayFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeamAAlias().equals(team);
		return f;
	}
	
	/*
	 * SubLeague Filters
	 */	
	public static Filter<Game> createSubLeagueFilter(Collection<String> teams)
	{
		Filter<Game> f = g -> teams.contains(g.getTeamHAlias()) && teams.contains(g.getTeamAAlias());
		return f;
	}
	
	public static Filter<Game> createSubLeagueFilter(String... teams)
	{
		return createSubLeagueFilter(Arrays.asList(teams));
	}

	/*
	 * MatchDay filters
	 */
	public static Filter<Game> createMatchDayFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchDay() == matchDay;
		return f;
	}
	public static Filter<Game> createMatchDayMinFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchDay() >= matchDay;
		return f;
	}
	public static Filter<Game> createMatchDayMaxFilter(int matchDay)
	{
		Filter<Game> f = g -> g.getMatchDay() <= matchDay;
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
