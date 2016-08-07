package bn.blaszczyk.blstatistics.core;

import java.util.*;

import bn.blaszczyk.blstatistics.tools.BLException;


public class League implements Iterable<Season>
{
	private List<Season> seasons = new ArrayList<>();
	private List<String> teams = new ArrayList<>();
	private String pathName;
	private String wbfURL;
	private String name;

	public League(String name, String wbfURL, String pathName, int[] yearBounds)
	{
		this.name = name;
		this.wbfURL = wbfURL;
		this.pathName = pathName;
		
		Date today = new Date();
		@SuppressWarnings("deprecation")
		int thisYear = 1900 + today.getYear() + ( (today.getMonth() > 6) ? 1 : 0  );		 
		int nrBounds = yearBounds.length;
		
		for(int i = 0; i < nrBounds; i+=2)
		{
			int endYear = thisYear;
			if( i+1 < nrBounds) 
				endYear = yearBounds[i+1];
			for(int year = yearBounds[i]; year <= endYear; year++)
				seasons.add( new Season(year,this));				
		}
	}

	public String getName()
	{
		return name;
	}
	
	public String getPathName()
	{
		return pathName;
	}
	
	public String getURLFormat()
	{
		return wbfURL;
	}

	public boolean hasSeason(int year)
	{
		for(Season s : this)
			if(s.getYear() == year)
				return true;
		return false;
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
