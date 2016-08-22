package bn.blaszczyk.fussballstats.filters;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import bn.blaszczyk.fussballstats.core.Game;

public abstract class GameFilter implements Filter<Game>
{
	
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
	/*
	 * Home Goal Filters
	 */
	public static Filter<Game> getGoal1Filter(int goals)
	{
		Filter<Game> f = g -> g.getGoalsH() == goals;
		return f;
	}
	public static Filter<Game> getGoal1MinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoalsH() >= minGoals;
		return f;
	}
	public static Filter<Game> getGoal1MaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoalsH() <= maxGoals;
		return f;
	}
	/*
	 * Away Goal Filters
	 */
	public static Filter<Game> getGoal2Filter(int goals)
	{
		Filter<Game> f = g -> g.getGoalsA() == goals;
		return f;
	}
	public static Filter<Game> getGoal2MinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getGoalsA() >= minGoals;
		return f;
	}
	public static Filter<Game> getGoal2MaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getGoalsA() <= maxGoals;
		return f;
	}
	/*
	 * Goal Difference Filters
	 */
	public static Filter<Game> getGoalDiffFilter(int goals)
	{
		Filter<Game> f = g -> g.getDiff() == goals;
		return f;
	}
	public static Filter<Game> getGoalDiffMinFilter(int minGoals)
	{
		Filter<Game> f = g -> g.getDiff() >= minGoals;
		return f;
	}
	public static Filter<Game> getGoalDiffMaxFilter(int maxGoals)
	{
		Filter<Game> f = g -> g.getDiff() <= maxGoals;
		return f;
	}

	/*
	 * Basic Team Filters
	 */
	public static Filter<Game> getTeamFilter(String team) 
	{
		Filter<Game> f = g ->g.getTeamHAlias().equals(team) || g.getTeamAAlias().equals(team);
		return f;
	}
	
	public static Filter<Game> getTeamHomeFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeamHAlias().equals(team);
		return f;
	}
	
	public static Filter<Game> getTeamAwayFilter(String team) 
	{
		Filter<Game> f = g -> g.getTeamAAlias().equals(team);
		return f;
	}
	
	/*
	 * SubLeague Filters
	 */	
	public static Filter<Game> getSubLeagueFilter(Collection<String> teams)
	{
		Filter<Game> f = g -> teams.contains(g.getTeamHAlias()) && teams.contains(g.getTeamAAlias());
		return f;
	}
	
	public static Filter<Game> getSubLeagueFilter(String... teams)
	{
		return getSubLeagueFilter(Arrays.asList(teams));
	}

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
	 * Date Filters
	 */
	public static Filter<Game> getDateFilter(Date date)
	{
		Filter<Game> f = g -> g.getDate().equals(date);
		return f;
	}
	
	public static Filter<Game> getDateMinFilter(Date date)
	{
		Filter<Game> f = g -> g.getDate().compareTo(date)>=0;
		return f;
	}
	
	public static Filter<Game> getDateMaxFilter(Date date)
	{
		Filter<Game> f = g -> g.getDate().compareTo(date)<=0;
		return f;
	}
	
	/*
	 * DayOfWeek Filter
	 */
	public static Filter<Game> getDayOfWeekFilter(int dayOfWeek) // 0 = Sunday ... 6 = Saturday
	{
		final Calendar calendar = new GregorianCalendar();
		Filter<Game> f = g -> {
			calendar.setTime(g.getDate()); 
			return calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek;
		};
		return f;
	}
	
}
