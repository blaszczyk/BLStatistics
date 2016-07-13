package bn.blaszczyk.blstatistics.core;

import java.util.*;

public class Table implements Iterable<TeamResult>
{
	private List<TeamResult> teamResults = new ArrayList<>();
	private int pointsForWin = 2;
	
	public Table(List<Game> games)
	{
		for(Game game : games)
			consumeGame(game);
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
	
	private void consumeGame(Game game)
	{
		if(getTeamIndex(game.getTeam1())<0)
			teamResults.add( new TeamResult(game.getTeam1()));
		if(getTeamIndex(game.getTeam2())<0)
			teamResults.add( new TeamResult(game.getTeam2()));
		for(TeamResult t : teamResults)
			t.consumeGame(game,pointsForWin);
	}

	@Override
	public Iterator<TeamResult> iterator()
	{
		return teamResults.iterator();
	}

}
