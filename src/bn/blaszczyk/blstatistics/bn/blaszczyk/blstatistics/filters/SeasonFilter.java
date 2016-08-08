package bn.blaszczyk.blstatistics.filters;

import java.util.ArrayList;
import java.util.List;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.League;
import bn.blaszczyk.blstatistics.core.Season;

public class SeasonFilter
{
	/*
	 * Pure Season Filters
	 */
	public static Filter<Season> getSeasonFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() == year;
		return f;
	}
	public static Filter<Season> getSeasonMinFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() >= year;
		return f;
	}
	public static Filter<Season> getSeasonMaxFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() <= year;
		return f;
	}
	
	/*
	 * Pure League Filter
	 */
	public static Filter<Season> getLeagueFilter(League league)
	{
		Filter<Season> f = s -> s.getLeague().equals(league);
		return f;
	}
	
	/*
	 * Hin- und Rückrunde
	 */

	public static BiFilter<Season,Game> getFirstRoundFilter()
	{
		BiFilter<Season,Game> f = (s,g) -> 2 * g.getMatchDay() <= s.getMatchDayCount();
		return f;
	}
	
	public static BiFilter<Season,Game> getSecondRoundFilter()
	{
		BiFilter<Season,Game> f = (s,g) -> 2 * g.getMatchDay() > s.getMatchDayCount();
		return f;
	}
	
	/*
	 * What these filters do best.
	 */
	
	public static List<Game> getGames( League league, BiFilter<Season,Game> filter)
	{
		List<Game> gameList = new ArrayList<>();
		for(Season season : league)
			for(Game game : league.getAllGames())
				if(filter.check(season, game))
					gameList.add(game);
		return gameList;
	}
	
}
