package bn.blaszczyk.blstatistics.core;

import java.util.*;

public class MatchDay implements Iterable<Game>
{
	private List<Game> games = new ArrayList<>();
	/*
	 *  Constructor needs gameList
	 */
	
	public MatchDay()
	{}
	/*
	 * Getters and Delegates
	 */
	
	public void addGame(Game game)
	{
		games.add(game);
	}

	
	public Game getGame(String team)
	{
		for(Game game:this)
			if(game.containsTeam(team))
				return game;
		return null;
	}
	
	public int getGameCount()
	{
		return games.size();
	}
	
	public int getGoals()
	{
		int goals = 0;
		for(Game game : this)
			goals += game.getGoals();
		return goals;		
	}
	
	@Override
	public Iterator<Game> iterator()
	{
		return games.iterator();
	}

	
}
