package bn.blaszczyk.blstatistics.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bn.blaszczyk.blstatistics.tools.BLException;

public class Season
{
	private int year;
	private List<MatchDay> matchDays;
	private List<String> teams;
	
	public Season(int year, List<String> teams)
	{
		this.year = year;
		this.teams = teams;
		resetMatchDays();
	}

	public Season(int year)
	{
		this.year = year;
	}

	public List<MatchDay> getMatchDays()
	{
		return matchDays;
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
		for(MatchDay m : matchDays)
			gameList.addAll(m.getGames());
		return gameList;
	}
	
	private void resetMatchDays()
	{
		if(teams == null)
			teams = new ArrayList<>();
		int matchDayCount= 2*(teams.size()-1);
		matchDays = new ArrayList<>(matchDayCount);
		for(int i = 0; i < matchDayCount; i++)
			matchDays.add(new MatchDay());
	}

	public boolean addGame(String gameString)
	{
		int matchDayIndex = Integer.parseInt( gameString.substring(0, gameString.indexOf('.') ) )-1;
		if(matchDayIndex >= matchDays.size())
			return false;
		String gameDetails = gameString.substring( gameString.indexOf('g') + 2  );
		try
		{
			Game game =  new Game(gameDetails);
			matchDays.get(matchDayIndex).addGame(game);
			if(!teams.contains(game.getTeam1()))
			{
				System.out.println("Added Team '" + game.getTeam1() + "'");
				teams.add(game.getTeam1());
			}
			if(!teams.contains(game.getTeam2()))
			{
				System.out.println("Added Team '" + game.getTeam2() + "'");
				teams.add(game.getTeam2());
			}
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
	
	public void write( Writer osw )
	{
		try
		{
			osw.write("Teams\n");
			for(String team : teams)
				osw.write(team+ '\n');
			osw.write("Games\n");
			for(int i = 0; i < getMatchDayCount(); i++)
				for(Game g : getMatchDay(i).getGames())
					osw.write( (i+1) + ". Spieltag" + g + '\n');
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void read( Reader reader )
	{
		String line;
		boolean readTeams = false;
		boolean readGames = false;
		BufferedReader br = new BufferedReader(reader);
		teams = new ArrayList<>();
		try
		{
			while( (line = br.readLine()) != null )
			{
				if(line.equalsIgnoreCase("Games"))
				{
					readGames = !(readTeams = false);
					resetMatchDays();
				}
				else if(line.equalsIgnoreCase("Teams"))
					readGames = !(readTeams = true);
				else if(readTeams)
					teams.add(line);
				else if(readGames)
					addGame(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
