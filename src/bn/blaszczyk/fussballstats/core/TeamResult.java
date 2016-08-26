package bn.blaszczyk.fussballstats.core;

import java.util.Comparator;

public class TeamResult
{	
	/*
	 * Compares Results according to Bundesliga Standard
	 */
	public static final Comparator<TeamResult> COMPARE_POSITION = (tr1,tr2) -> {
			int result = 0;
			result += 4 * Integer.compare(tr2.getPoints(), tr1.getPoints());
			result += 2 * Integer.compare(tr2.getGoalDifference(), tr1.getGoalDifference());
			result += 1 * Integer.compare(tr2.getTeamGoals(), tr1.getTeamGoals());
			return Integer.signum(result);
	};
	
	/*
	 * Variables
	 */
	private final String team;
	private int points=0;
	private int games=0;
	private int wins=0;
	private int draws=0;
	private int losses=0;
	private int teamGoals=0;
	private int opponentGoals=0;
	private int position=0;
	
	/*
	 * Constructor
	 */
	public TeamResult(String team)
	{
		this.team = team;
	}
	
	/*
	 * Getters, Setter
	 */
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int position)
	{
		this.position = position;
	}

	public String getTeam()
	{
		return team;
	}

	public int getPoints()
	{
		return points;
	}
	
	public int getGames()
	{
		return games;
	}

	public int getWins()
	{
		return wins;
	}

	public int getDraws()
	{
		return draws;
	}

	public int getLosses()
	{
		return losses;
	}

	public int getTeamGoals()
	{
		return teamGoals;
	}

	public int getOpponentGoals()
	{
		return opponentGoals;
	}
	
	public int getGoalDifference()
	{
		return teamGoals - opponentGoals;
	}
	
	/*
	 * Special Method
	 */
	public void consumeGame(Game game, int pointsForWin)
	{
		if(team.equals(game.getTeamHAlias()))
		{
			switch(game.getWinner())
			{
			case Game.HOME:
				wins++;
				points += pointsForWin;
				break;
			case Game.DRAW:
				draws++;
				points++;
				break;
			case Game.AWAY:
				losses++;
				break;
			}
			games++;
			teamGoals += game.getGoalsH();
			opponentGoals += game.getGoalsA();
		}
		if(team.equals(game.getTeamAAlias()))
		{
			switch(game.getWinner())
			{
			case Game.AWAY:
				wins++;
				points += pointsForWin;
				break;
			case Game.DRAW:
				draws++;
				points++;
				break;
			case Game.HOME:
				losses++;
				break;
			}
			games++;
			teamGoals += game.getGoalsA();
			opponentGoals += game.getGoalsH();
		}
	}
	
	/*
	 * Object Method
	 */
	@Override
	public String toString()
	{
		return String.format("%2d. %30s   %4d Spl   %4d Pkt  %5d Dif      %4d S %4d U %4d N      %4d:%4d", 
				position, team, games, points, getGoalDifference(), wins, draws, losses, teamGoals, opponentGoals );
	}

}
