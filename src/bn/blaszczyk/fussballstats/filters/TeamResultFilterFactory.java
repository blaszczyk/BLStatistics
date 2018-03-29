package bn.blaszczyk.fussballstats.filters;

import bn.blaszczyk.fussballstats.core.*;
import bn.blaszczyk.fussballstats.model.Game;

public class TeamResultFilterFactory
{
	/*
	 * Home- and Away-Table
	 */
	
	public static BiFilter<TeamResult,Game> createHomeGameFilter()
	{
		BiFilter<TeamResult,Game> f = (tr,g) ->tr.getTeam().equals(g.getTeamHome());
		return f;
	}
	
	public static BiFilter<TeamResult,Game> createAwayGameFilter()
	{
		BiFilter<TeamResult,Game> f = (tr,g) ->tr.getTeam().equals(g.getTeamAway());
		return f;
	}
	
}
