package bn.blaszczyk.fussballstats.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
