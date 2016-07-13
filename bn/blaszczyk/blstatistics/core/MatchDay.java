package bn.blaszczyk.blstatistics.core;

import java.util.*;

public class MatchDay
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

	public List<Game> getGames()
	{
		return games;
	}
	
	public void addGame(Game game)
	{
		games.add(game);
	}

	public Game getGame(int index)
	{
		return games.get(index);
	}

	public int GamesCount()
	{
		return games.size();
	}

//	public Iterator<Game> getGameIterator()
//	{
//		return games.iterator();
//	}
	
}
