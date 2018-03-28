package bn.blaszczyk.fussballstats.core;

import java.util.*;
import bn.blaszczyk.fussballstats.filters.*;
import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.Team;

public class Table
{
	
	private static final Comparator<Team> BY_NAME = (t1,t2) -> t1.getName().compareTo(t2.getName());
	
	/*
	 * Variables
	 */
	private final Map<Team,TeamResult> teamResults = new HashMap<>();
	private final int pointsForWin;

	/*
	 * Constructors
	 */
	public Table(Iterable<Game> games, int pointsForWin)
	{
		this(games,null,pointsForWin);
	}
	
	public Table(Iterable<Game> games,BiFilter<TeamResult,Game> teamResultFilter, int pointsForWin)
	{
		this.pointsForWin = pointsForWin;
		if(teamResultFilter == null)
			teamResultFilter = LogicalBiFilterFactory.createTRUEBiFilter();
		for(Game game : games)
			consumeGame(game,teamResultFilter);
	}

	/*
	 * Getters, Delegates
	 */
	
	public List<Team> getTeamList()
	{
		List<Team> teamList = new ArrayList<>();
		for(TeamResult tr : teamResults.values())
			teamList.add(tr.getTeam());
		Collections.sort(teamList,BY_NAME);
		return teamList;
	}
	
	/*
	 * Special Methods
	 */
	
	public List<TeamResult> getSortedResults()
	{
		if(teamResults.isEmpty())
			return Collections.emptyList();
		final List<TeamResult> sortedResults = new ArrayList<>(teamResults.values());
		sortedResults.sort( TeamResult.COMPARE_POSITION );
		int lastPos = 1;
		sortedResults.get(0).setPosition(1);
		for(int i = 1; i < sortedResults.size(); i++)
		{
			final TeamResult current = sortedResults.get(i);
			final TeamResult previous = sortedResults.get(i-1);
			if(TeamResult.COMPARE_POSITION.compare(current, previous) == 0)
				current.setPosition(lastPos);
			else
				current.setPosition(lastPos = i + 1);
		}
		return sortedResults;
	}
	
	private void consumeGame(Game game, BiFilter<TeamResult,Game> teamResultFilter)
	{
		final Team teamHome = game.getTeamHome();
		consumeByTeam(game, teamResultFilter, teamHome);
		
		final Team teamAway = game.getTeamAway();
		consumeByTeam(game, teamResultFilter, teamAway);
	}

	private void consumeByTeam(final Game game, final BiFilter<TeamResult, Game> teamResultFilter, final Team team)
	{
		if(!teamResults.containsKey(team))
			teamResults.put(team, new TeamResult(team));
		final TeamResult result = teamResults.get(team);
		if(teamResultFilter.check(result, game))
			result.consumeGame(game, pointsForWin);
	}

}
