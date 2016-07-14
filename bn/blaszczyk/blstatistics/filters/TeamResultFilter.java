package bn.blaszczyk.blstatistics.filters;

import bn.blaszczyk.blstatistics.core.*;

public class TeamResultFilter
{
	public static Filter<TeamResult> getTeamFilter(String team)
	{
		Filter<TeamResult> f = tr -> tr.getTeam().equals(team);
		return f;
	}

	public static BiFilter<TeamResult,Game> getHomeGameFilter()
	{
		BiFilter<TeamResult,Game> f = (tr,g) ->tr.getTeam().equals(g.getTeam1());
		return f;
	}
	
	public static BiFilter<TeamResult,Game> getAwayGameFilter()
	{
		BiFilter<TeamResult,Game> f = (tr,g) ->tr.getTeam().equals(g.getTeam2());
		return f;
	}
	
}
