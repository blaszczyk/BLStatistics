package bn.blaszczyk.blstatistics.core;

import java.util.*;

import bn.blaszczyk.blstatistics.tools.BLException;

public class Season implements Iterable<MatchDay>
{
	private int year;
	private List<MatchDay> matchDays = new ArrayList<>();
	private List<String> teams = new ArrayList<>();

	/*
	 * Constructor
	 */
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

	public void addGame(Game game)
	{
		int matchDayIndex = game.getMatchDay() - 1;
		while(matchDayIndex >= matchDays.size())
			matchDays.add(new MatchDay());
		matchDays.get(matchDayIndex).addGame(game);
		if(!teams.contains(game.getTeam1()))
			teams.add(game.getTeam1());
		if(!teams.contains(game.getTeam2()))
			teams.add(game.getTeam2());
	}
	
	public void addGames(Iterable<Game> source)
	{
		for(Game game : source)
			addGame(game);
	}

	@Override
	public Iterator<MatchDay> iterator()
	{
		return matchDays.iterator();
	}
	
}
