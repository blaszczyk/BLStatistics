package bn.blaszczyk.fussballstats.core;

import java.util.*;


public class Season implements Iterable<Game>
{
	
	/*
	 * Variables
	 */
	private final int year;
	private final League league; 
	private final List<String> teams = new ArrayList<>();
	private List<Game> games = new ArrayList<>();
	private int matchDayMin = 0;
	private int matchDayMax = -1;

	/*
	 * Constructor
	 */
	public Season(int year, League league)
	{
		this.year = year;
		this.league = league;
	}

	/*
	 * Getters, Setters, Delegates
	 */

	public int getMatchDayCount()
	{
		return matchDayMax - matchDayMin + 1;
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
		teams.clear();
		matchDayMin = 1;
		matchDayMax = 0;
		if(!games.isEmpty())
			matchDayMin = matchDayMax = games.get(0).getMatchDay();
		for(Game game : games)
		{
			matchDayMin = Math.min(matchDayMin, game.getMatchDay());
			matchDayMax = Math.max(matchDayMax, game.getMatchDay());
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
	
	/*
	 * Iterator Method
	 */
	@Override
	public Iterator<Game> iterator()
	{
		return games.iterator();
	}
	
	/*
	 * Object Method
	 */
	@Override
	public String toString()
	{
		return league.getName() + " - " + year;
	}
}
