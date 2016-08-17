package bn.blaszczyk.fussballstats.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bn.blaszczyk.fussballstats.tools.FussballException;
import bn.blaszczyk.fussballstats.tools.TeamAlias;

public class Game
{
	/*
	 * Entsprechend der Rückgabe von getWinner()
	 */
	public static final int HOME = 1;
	public static final int DRAW = 0;
	public static final int AWAY = -1;
	
	public static final int WIN = HOME;
	public static final int LOSS = AWAY;
	
	/*
	 * Standard Date Format
	 */
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
	
	/*
	 * Object Properties
	 */
	private int matchDay;
	private Date date;
	private String teamH;
	private String teamA;
	private int goalsH;
	private int goalsA;
	
	/*
	 * Constructors
	 */
	

	public Game(int matchDay, Date date, String teamH, String teamA, int goalsH, int goalsA)
	{
		this.matchDay = matchDay;
		this.date = date;
		this.teamH = teamH;
		this.teamA = teamA;
		this.goalsH = goalsH;
		this.goalsA = goalsA;
	}
	
	public Game(String gameString) throws FussballException
	{
		try
		{
			matchDay = Integer.parseInt( gameString.substring(0, gameString.indexOf('.') ).trim() );
		}
		catch(StringIndexOutOfBoundsException e)
		{
			throw new FussballException("Wrong game format in '" + gameString + "'" );
		}
		if(matchDay < 1)
			throw new FussballException("Wrong matchDay in '" + gameString + "'" );
		String gameDetails = gameString.substring( gameString.indexOf('g') + 2  );
		String[] split = gameDetails.trim().split(":");
		if(split.length != 3)
			throw new FussballException("Wrong game Format in '" + gameDetails + "'");
		try
		{
			date = DATE_FORMAT.parse( split[0].trim());
		}
		catch (ParseException e)
		{
			throw new FussballException("Wrong date Format in '" + gameDetails + "'",e);
		} 
		try
		{
			goalsH = Integer.parseInt( split[1].substring(split[1].lastIndexOf(' ')+1).trim() );
			goalsA = Integer.parseInt(split[2].trim());
		}
		catch( NumberFormatException e)
		{
			throw new FussballException("Wrong goal Format in '" + gameDetails + "'",e);
		}
		String teams = split[1].substring(0, split[1].lastIndexOf(' '));
		int splitIntex = teams.indexOf(" - ");
		if(splitIntex < 0)
			throw new FussballException("Wrong game Format in '" + gameDetails + "'");
		teamH = teams.substring(0, splitIntex) .trim();
		teamA = teams.substring(splitIntex + 3).trim();
	}
	
	/*
	 * only Getters
	 */
	public int getGoalsH()
	{
		return goalsH;
	}
	public int getGoalsA()
	{
		return goalsA;
	}

	public int getDiff()
	{
		return goalsH - goalsA;
	}
	
	public int getGoals()
	{
		return goalsH + goalsA;
	}
	
	public String getTeamH()
	{
		return TeamAlias.getAlias(teamH);
	}
	public String getTeamA()
	{
		return TeamAlias.getAlias(teamA);
	}
	
	public Date getDate()
	{
		return date;
	}

	public int getMatchDay()
	{
		return matchDay;
	}
	

	/*
	 * Specific useful Methods
	 */
	public int getWinner()
	{
		switch(Integer.signum(goalsH-goalsA))
		{
		case 1:
			return HOME;
		case 0:
			return DRAW;
		case -1:
			return AWAY;
		}
		return 0;
		//Simpler but unsafe:		
//		return Integer.signum(goals1-goals2);
	}
	
	public boolean containsTeam(String team)
	{
		if(team != null)	
			return team.equals(getTeamH()) || team.equals(getTeamA());
		return false;
	}

	@Override
	public String toString()
	{
		return String.format( "%2d. Spieltag  %s: %15s - %15s %2d:%d" , matchDay, DATE_FORMAT.format(date), teamH, teamA, goalsH, goalsA);
	}
}
