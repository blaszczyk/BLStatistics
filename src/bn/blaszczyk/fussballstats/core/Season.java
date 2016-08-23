package bn.blaszczyk.fussballstats.core;

import java.util.*;


public class Season implements Iterable<Game>
{
	private int year;
	private League league; 
	private List<String> teams = new ArrayList<>();
	private List<Game> games = new ArrayList<>();
	private int matchDayCount = 0;

	/*
	 * Constructor
	 */
	public Season(int year, League league)
	{
		this.year = year;
		this.league = league;
	}

	/*
	 * Getters
	 */

	public int getMatchDayCount()
	{
		return matchDayCount;
	}

	public int getTeamCount()
	{
		return teams.size();
	}
	
	public int getYear()
	{
		return year;
	}
	
	public League getLeague()
	{
		return league;
	}

	public int getGameCount()
	{
		return games.size();
	}	

	public void setGames(List<Game> games)
	{
		this.games = games;
		matchDayCount = 0;
		teams.clear();
		for(Game game : games)
		{
			matchDayCount = Math.max(matchDayCount, game.getMatchDay());
			addTeam(game.getTeamHAlias());
			addTeam(game.getTeamAAlias());
		}
	}
	

	private void addTeam(String team)
	{
		if(teams.contains(team))
			return;
		teams.add(team);
		league.addTeam(team);
	}
	
	@Override
	public Iterator<Game> iterator()
	{
		return games.iterator();
	}
	
	@Override
	public String toString()
	{
		return league.getName() + " - " + year;
	}
}
