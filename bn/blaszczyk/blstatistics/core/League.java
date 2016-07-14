package bn.blaszczyk.blstatistics.core;

import java.util.*;


public class League implements Iterable<Season>
{
	private List<Season> seasons = new ArrayList<>();
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
	
	public Season getSeason(int year)
	{
		for(Season s : this)
			if(s.getYear() == year)
				return s;
		return null;
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
}
