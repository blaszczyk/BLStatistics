package bn.blaszczyk.blstatistics.core;

import java.util.*;

import bn.blaszczyk.blstatistics.tools.BLException;

public class Season implements Iterable<MatchDay>
{
	private int year;
	private List<MatchDay> matchDays = new ArrayList<>();
	private List<String> teams = new ArrayList<>();

	public Season(int year)
	{
		this.year = year;
	}

	public MatchDay getMatchDay(int index)
	{
		return matchDays.get(index);
	}

	public int getMatchDayCount()
	{
		return matchDays.size();
	}
	

	public List<String> getTeams()
	{
		return teams;
	}

	public String getTeam(int index)
	{
		return teams.get(index);
	}

	public int getTeamCount()
	{
		return teams.size();
	}
	
	public int getYear()
	{
		return year;
	}
	
	public List<Game> getAllGames()
	{
		List<Game> gameList = new ArrayList<>();
		for(MatchDay matchDay : this)
			for(Game game : matchDay)
				gameList.add(game);
		return gameList;
	}

	public boolean addGame(String gameString)
	{
		int matchDayIndex = Integer.parseInt( gameString.substring(0, gameString.indexOf('.') ) )-1;
		while(matchDayIndex >= matchDays.size())
			matchDays.add(new MatchDay());
		String gameDetails = gameString.substring( gameString.indexOf('g') + 2  );
		try
		{
			Game game =  new Game(gameDetails);
			matchDays.get(matchDayIndex).addGame(game);
			if(!teams.contains(game.getTeam1()))
				teams.add(game.getTeam1());
			if(!teams.contains(game.getTeam2()))
				teams.add(game.getTeam2());
			return true;			
		}
		catch (BLException e)
		{
			System.err.println(e.getErrorMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public void addGames(Iterable<String> source)
	{
		Iterator<String> iterator = source.iterator();
		while(iterator.hasNext())
			addGame(iterator.next());
	}
	
	public Collection<String> getAllGamesAsString()
	{
		Stack<String> gameStack = new Stack<>();
		for(int i = 0; i < getMatchDayCount(); i++)
			for(Game g : getMatchDay(i))
				gameStack.push( (i+1) + ". Spieltag" + g + '\n');
		return gameStack;
	}

	@Override
	public Iterator<MatchDay> iterator()
	{
		return matchDays.iterator();
	}
	
//	public Iterator<Game> getGameIterator()
//	{
//		if(matchDays.isEmpty())
//			return new Iterator<Game>(){
//				@Override
//				public boolean hasNext(){return false;}
//				@Override
//				public Game next(){return null;}		
//			};
//		return new Iterator<Game>(){
//			
//			private Iterator<MatchDay> matchDayIterator = matchDays.iterator();
//			private Iterator<Game> innerIterator = matchDayIterator.next().getGameIterator();
//			
//			@Override
//			public boolean hasNext()
//			{
//				return innerIterator.hasNext();
//			}
//
//			@Override
//			public Game next()
//			{
//				Game returnGame = null;
//				if( innerIterator.hasNext() )
//					returnGame = innerIterator.next();
//				while(!innerIterator.hasNext() && matchDayIterator.hasNext())
//					innerIterator = matchDayIterator.next().getGameIterator();
//				return returnGame;
//			}
//		};
//	}
}
