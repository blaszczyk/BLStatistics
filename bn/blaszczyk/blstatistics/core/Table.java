package bn.blaszczyk.blstatistics.core;

import java.util.*;

import bn.blaszczyk.blstatistics.filters.*;

public class Table implements Iterable<TeamResult>
{
	private List<TeamResult> teamResults = new ArrayList<>();
	private int pointsForWin = 2;

	/*
	 * Constructors for different sets of Filters
	 */
	public Table(Iterable<Game> games)
	{
		this(games,null,null);
	}

	public Table(Iterable<Game> games, Filter<Game> gameFilter)
	{
		this(games,gameFilter,null);
	}
	
	public Table(Iterable<Game> games, BiFilter<TeamResult,Game> teamResultFilter)
	{
		this(games,null,teamResultFilter);
	}
	
	public Table(Iterable<Game> games, Filter<Game> gameFilter, BiFilter<TeamResult,Game> teamResultFilter)
	{
		if(gameFilter == null)
			gameFilter = LogicalFilterFactory.getTRUEFilter();
		if(teamResultFilter == null)
			teamResultFilter = LogicalBiFilterFactory.getTRUEBiFilter();
		for(Game game : games)
			if(gameFilter.check(game))
				consumeGame(game,teamResultFilter);
	}

	public void sort()
	{
		teamResults.sort( TeamResult.COMPARATOR );
	}
	
	public int getTeamCount()
	{
		return teamResults.size();
	}
	
	public TeamResult getTeamResult(int index)
	{
		return teamResults.get(index);
	}
	
	public int getTeamIndex(String team)
	{
		for(int i = 0; i < teamResults.size(); i++)
			if( teamResults.get(i).getTeam().equals(team))
				return i;
		return -1;
	}
	
	private void consumeGame(Game game, BiFilter<TeamResult,Game> teamResultFilter)
	{
		if(getTeamIndex(game.getTeam1())<0)
			teamResults.add( new TeamResult(game.getTeam1()));
		if(getTeamIndex(game.getTeam2())<0)
			teamResults.add( new TeamResult(game.getTeam2()));
		for(TeamResult t : teamResults)
			if(teamResultFilter.check(t, game))
				t.consumeGame(game,pointsForWin);
	}

	@Override
	public Iterator<TeamResult> iterator()
	{
		return teamResults.iterator();
	}

}
