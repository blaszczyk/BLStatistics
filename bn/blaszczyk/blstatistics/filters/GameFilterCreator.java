package bn.blaszczyk.blstatistics.filters;

import bn.blaszczyk.blstatistics.core.Game;

public class GameFilterCreator
{

		/*
		 * MatchDay filters
		 */
		public static FilterCreator<Game,Integer> getMatchDayFilter()
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getMatchDayFilter(i);
			return c;
		}
		public static FilterCreator<Game, Integer> getMatchDayMinFilter()
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getMatchDayMinFilter(i);
			return c;
		}
		public static FilterCreator<Game, Integer> getMatchDayMaxFilter()
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getMatchDayMaxFilter(i);
			return c;
		}
		
		/*
		 * Goal Filters
		 */
		public static FilterCreator<Game, Integer> getGoalFilter()
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getGoalFilter(i);
			return c;
		}
		public static FilterCreator<Game, Integer> getGoalMinFilter()
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getGoalFilter(i);
			return c;
		}
		public static FilterCreator<Game, Integer> getGoalMaxFilter()
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getGoalFilter(i);
			return c;
		}
		
		/*
		 * Result Filters
		 */
		public static FilterCreator<Game, Integer> getWinnerFilter() 
		{
			FilterCreator<Game,Integer> c = i -> GameFilter.getWinnerFilter(i);
			return c;
		}

		/*
		 * Basic Team Filters
		 */
		public static FilterCreator<Game,String> getTeamFilter() 
		{
			FilterCreator<Game,String> c = s -> GameFilter.getTeamFilter(s);
			return c;
		}
		
		public static FilterCreator<Game,String> getTeamHomeFilter() 
		{
			FilterCreator<Game,String> c = s -> GameFilter.getTeamHomeFilter(s);
			return c;
		}
		
		public static FilterCreator<Game,String> getTeamAwayFilter() 
		{
			FilterCreator<Game,String> c = s -> GameFilter.getTeamAwayFilter(s);
			return c;
		}
}
