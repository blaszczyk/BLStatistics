package bn.blaszczyk.blstatistics.core;

import java.util.*;

import bn.blaszczyk.blstatistics.tools.BLException;


public class League implements Iterable<Season>
{
	private List<Season> seasons = new ArrayList<>();
	private List<String> teams = new ArrayList<>();
	private String name;

	public League(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public boolean addSeason(Season e)
	{
		return seasons.add(e);
	}
	
	public Season getSeason(int year) throws BLException
	{
		for(Season s : this)
			if(s.getYear() == year)
				return s;
		throw new BLException("Season " + year + " of League " + name + " not found.");
	}
	
	public List<Game> getAllGames()
	{
		List<Game> games = new ArrayList<Game>();
		for( Season s : this)
			games.addAll(s.getAllGames());
		return games;
	}
	

	@Override
	public Iterator<Season> iterator()
	{
		return seasons.iterator();
	}
	
	public void addTeam(String team)
	{
		if(!teams.contains(team))
			teams.add(team);
	}

	public Iterable<String> getTeams()
	{
		Collections.sort(teams);
		return teams;
	}
}
