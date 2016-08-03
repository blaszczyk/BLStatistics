package bn.blaszczyk.blstatistics.core;

import java.util.*;

import bn.blaszczyk.blstatistics.tools.BLException;


public class League implements Iterable<Season>
{
	private List<Season> seasons = new ArrayList<>();
	private List<String> teams = new ArrayList<>();
	private String pathName;
	private String name;
	
	public League(String name, String pathName, int minSeason, int maxSeason)
	{
		this.name = name;
		this.pathName = pathName;
		for(int year = minSeason; year <= maxSeason; year++)
			seasons.add( new Season(year,this));
	}

	public String getName()
	{
		return name;
	}
	
	public String getPathName()
	{
		return pathName;
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
	
	
	public void addTeam(String team)
	{
		if(!teams.contains(team))
			teams.add(team);
	}

	public List<String> getTeams()
	{
		Collections.sort(teams);
		return teams;
	}

	public int getSeasonCount()
	{
		return seasons.size();
	}

	@Override
	public Iterator<Season> iterator()
	{
		return seasons.iterator();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
