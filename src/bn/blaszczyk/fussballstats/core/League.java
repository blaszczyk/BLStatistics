package bn.blaszczyk.fussballstats.core;

import java.util.*;

public class League implements Iterable<Season>
{
	
	/*
	 * What is the most recent season?
	 */
	private static Calendar CALENDAR = new GregorianCalendar();
	public static final int THIS_SEASON = CALENDAR.get(Calendar.YEAR) + ( (CALENDAR.get(Calendar.MONTH)  > Calendar.JUNE) ? 1 : 0  );		 
	
	/*
	 * Variables
	 */
	private final List<Season> seasons = new ArrayList<>();
	private final List<String> teams = new ArrayList<>();
	private final String pathName;
	private final String urlFormat;
	private final String name;

	/*
	 * Constructor
	 */
	public League(String name, String urlFormat, String pathName, int[] yearBounds)
	{
		this.name = name;
		this.urlFormat = urlFormat;
		this.pathName = pathName;	 
		
		int nrBounds = yearBounds.length;
		for(int i = 0; i < nrBounds; i+=2)
		{
			int endYear = THIS_SEASON;
			if( i+1 < nrBounds) 
				endYear = yearBounds[i+1];
			for(int year = yearBounds[i]; year <= endYear; year++)
				seasons.add(new Season(year,this));				
		}
	}

	/*
	 * Getters, Setters, Delegates
	 */
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
		return urlFormat;
	}
	
	public String getSQLName()
	{
		return pathName.replace("/", "_");
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
			for(Game g : s)
				games.add(g);
		return games;
	}
	
	public void addTeam(String team)
	{
		if(!teams.contains(team))
		{
			teams.add(team);
			Collections.sort(teams);
		}
	}

	public List<String> getTeams()
	{
		return teams;
	}

	public int getSeasonCount()
	{
		return seasons.size();
	}
	public boolean hasSeason(int year)
	{
		for(Season s : this)
			if(s.getYear() == year)
				return true;
		return false;
	}
	
	/*
	 * Iterable Method
	 */
	@Override
	public Iterator<Season> iterator()
	{
		return seasons.iterator();
	}
	
	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return name;
	}
}
