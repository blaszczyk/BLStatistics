package bn.blaszczyk.fussballstats.filters;

import bn.blaszczyk.fussballstats.core.*;

public class TeamResultFilterFactory
{
	/*
	 * Home- and Away-Table
	 */
	
	public static BiFilter<TeamResult,Game> createHomeGameFilter()
	{
		BiFilter<TeamResult,Game> f = (tr,g) ->tr.getTeam().equals(g.getTeamHAlias());
		return f;
	}
	
	public static BiFilter<TeamResult,Game> createAwayGameFilter()
	{
		BiFilter<TeamResult,Game> f = (tr,g) ->tr.getTeam().equals(g.getTeamAAlias());
		return f;
	}
	
}
