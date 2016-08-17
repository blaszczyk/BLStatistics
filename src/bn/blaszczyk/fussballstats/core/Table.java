package bn.blaszczyk.fussballstats.core;

import java.util.*;
import bn.blaszczyk.fussballstats.filters.*;

public class Table implements Iterable<TeamResult>
{
	private List<TeamResult> teamResults = new ArrayList<>();
	private int pointsForWin = 3;

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
			teamResultFilter = LogicalBiFilter.getTRUEBiFilter();
		for(Game game : games)
			consumeGame(game,teamResultFilter);
	}

	public void sort()
	{
		if(teamResults == null || teamResults.isEmpty())
			return;
		teamResults.sort( TeamResult.COMPARE_POSITION );
		int lastPos = 1;
		teamResults.get(0).setPosition(1);
		for(int i = 1; i < getTeamCount(); i++)
			if(TeamResult.COMPARE_POSITION.compare(getTeamResult(i), getTeamResult(i-1)) == 0)
				getTeamResult(i).setPosition(lastPos);
			else
				getTeamResult(i).setPosition(lastPos = i + 1);	
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
	
	public List<String> getTeamList()
	{
		List<String> teamList = new ArrayList<>();
		for(TeamResult tr : this)
			teamList.add(tr.getTeam());
		Collections.sort(teamList);
		return teamList;
	}
	
	private void consumeGame(Game game, BiFilter<TeamResult,Game> teamResultFilter)
	{
		if(getTeamIndex(game.getTeamH())<0)
			teamResults.add( new TeamResult(game.getTeamH()));
		if(getTeamIndex(game.getTeamA())<0)
			teamResults.add( new TeamResult(game.getTeamA()));
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