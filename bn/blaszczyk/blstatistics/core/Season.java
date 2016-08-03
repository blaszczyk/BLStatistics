package bn.blaszczyk.blstatistics.core;

import java.util.*;

public class Season implements Iterable<MatchDay>
{
	private int year;
	private League league;
	private List<MatchDay> matchDays = new ArrayList<>();
	private List<String> teams = new ArrayList<>();

	/*
	 * Constructor
	 */
	public Season(int year, League league)
	{
		this.year = year;
		this.league = league;
	}

	public MatchDay getMatchDay(int index)
	{
		return matchDays.get(index);
	}

	public int getMatchDayCount()
	{
		return matchDays.size();
	}

//	public List<String> getTeams()
//	{
//		return teams;
//	}
//
//	public String getTeam(int index)
//	{
//		return teams.get(index);
//	}
//
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
		int count = 0;
		for(MatchDay matchDay : this)
			count += matchDay.getGameCount();
		return count;
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
		league.addTeam(game.getTeam1());
		league.addTeam(game.getTeam2());
		if(!teams.contains(game.getTeam1()))
			teams.add(game.getTeam1());
		if(!teams.contains(game.getTeam2()))
			teams.add(game.getTeam2());
	}
	
	public void consumeGames(Iterable<Game> source)
	{
		matchDays = new ArrayList<>();
		for(Game game : source)
			addGame(game);
	}

	@Override
	public Iterator<MatchDay> iterator()
	{
		return matchDays.iterator();
	}

	
}
