package bn.blaszczyk.fussballstats.core;

import java.util.Date;

import bn.blaszczyk.fussballstats.tools.TeamAlias;

public class Game
{
	/*
	 * Corresponding to getWinner()
	 */
	public static final int HOME = 1;
	public static final int DRAW = 0;
	public static final int AWAY = -1;
	
	public static final int WIN = HOME;
	public static final int LOSS = AWAY;
	
	/*
	 * Variables
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
		return teamH;
	}
	
	public String getTeamA()
	{
		return teamA;
	}
	
	public String getTeamHAlias()
	{
		return TeamAlias.getAlias(teamH);
	}
	
	public String getTeamAAlias()
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
	 * Who won the game?
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
//		return Integer.signum(goalsH-goalsA);
	}
	
	/*
	 * Object Method
	 */
	@Override
	public String toString()
	{
		return String.format("%2d. Spieltag, %s% : %30s - %30s %2d:%2d" , matchDay, date, teamH, teamA, goalsH, goalsA);
	}

}
