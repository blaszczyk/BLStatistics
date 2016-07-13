package bn.blaszczyk.blstatistics.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
		for(MatchDay matchDay : matchDays)
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
	
	public void consumeGameStack(Stack<String> gameStack)
	{
		while(!gameStack.isEmpty())
			addGame(gameStack.pop());
	}
	
	public void write( Writer writer )
	{
		try
		{
			for(int i = 0; i < getMatchDayCount(); i++)
				for(Game g : getMatchDay(i))
					writer.write( (i+1) + ". Spieltag" + g + '\n');
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void read( Reader reader )
	{
		String line;
		BufferedReader br = new BufferedReader(reader);
		teams = new ArrayList<>();
		try
		{
			while( (line = br.readLine()) != null )
					addGame(line);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
