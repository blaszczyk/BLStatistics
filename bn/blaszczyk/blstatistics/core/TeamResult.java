package bn.blaszczyk.blstatistics.core;

import java.util.Comparator;

public class TeamResult
{	
	public static final Comparator<TeamResult> COMPARE_TEAM = (tr1,tr2) -> tr1.getTeam().compareTo(tr2.getTeam());
	public static final Comparator<TeamResult> COMPARE_POINTS = (tr1,tr2) -> Integer.compare(tr2.getPoints(), tr1.getPoints());
	public static final Comparator<TeamResult> COMPARE_GAMES = (tr1,tr2) -> Integer.compare(tr2.getGames(), tr1.getGames());
	public static final Comparator<TeamResult> COMPARE_WINS = (tr1,tr2) -> Integer.compare(tr2.getWins(), tr1.getWins());
	public static final Comparator<TeamResult> COMPARE_DRAWS = (tr1,tr2) -> Integer.compare(tr2.getDraws(), tr1.getDraws());
	public static final Comparator<TeamResult> COMPARE_LOSSES = (tr1,tr2) -> Integer.compare(tr2.getLosses(), tr1.getLosses());
	public static final Comparator<TeamResult> COMPARE_DIFF = (tr1,tr2) -> Integer.compare(tr2.getGoalDifference(), tr1.getGoalDifference());
	public static final Comparator<TeamResult> COMPARE_GOALS_TEAM = (tr1,tr2) -> Integer.compare(tr2.getTeamGoals(), tr1.getTeamGoals());
	public static final Comparator<TeamResult> COMPARE_GOALS_OPPONENT = (tr1,tr2) -> Integer.compare(tr2.getOpponentGoals(), tr1.getOpponentGoals());

	public static final Comparator<TeamResult> COMPARE_POINTS_REL = (tr1,tr2) -> Integer.compare(tr2.getPoints()*tr1.getGames(), tr1.getPoints()*tr2.getGames());
	public static final Comparator<TeamResult> COMPARE_WINS_REL = (tr1,tr2) -> Integer.compare(tr2.getWins()*tr1.getGames(), tr1.getWins()*tr2.getGames());
	public static final Comparator<TeamResult> COMPARE_DRAWS_REL = (tr1,tr2) -> Integer.compare(tr2.getDraws()*tr1.getGames(), tr1.getDraws()*tr2.getGames());
	public static final Comparator<TeamResult> COMPARE_LOSSES_REL = (tr1,tr2) -> Integer.compare(tr2.getLosses()*tr1.getGames(), tr1.getLosses()*tr2.getGames());
	public static final Comparator<TeamResult> COMPARE_DIFF_REL = (tr1,tr2) -> Integer.compare(tr2.getGoalDifference()*tr1.getGames(), tr1.getGoalDifference()*tr2.getGames());
	public static final Comparator<TeamResult> COMPARE_GOALS_TEAM_REL = (tr1,tr2) -> Integer.compare(tr2.getTeamGoals()*tr1.getGames(), tr1.getTeamGoals()*tr2.getGames());
	public static final Comparator<TeamResult> COMPARE_GOALS_OPPONENT_REL = (tr1,tr2) -> Integer.compare(tr2.getOpponentGoals()*tr1.getGames(), tr1.getOpponentGoals()*tr2.getGames());
	
	public static final Comparator<TeamResult> COMPARE_POSITION = (team1,team2) -> {
			int result = 0;
			result += 4 * COMPARE_POINTS.compare(team1, team2);
			result += 2 * COMPARE_DIFF.compare(team1, team2);
			result += 1 * COMPARE_GOALS_TEAM.compare(team1, team2);
			return Integer.signum(result);
	};
	
	private String team;
	private int points=0;
	private int games=0;
	private int wins=0;
	private int draws=0;
	private int losses=0;
	private int teamGoals=0;
	private int opponentGoals=0;
	private int position=0;
	
	public TeamResult(String team)
	{
		this.team = team;
	}
	
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
	
	public void consumeGame(Game game, int pointsForWin)
	{
		if(team.equals(game.getTeam1()))
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
			games ++;
			teamGoals += game.getGoals1();
			opponentGoals += game.getGoals2();
		}
		if(team.equals(game.getTeam2()))
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
			teamGoals += game.getGoals2();
			opponentGoals += game.getGoals1();
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("%2d. %15s     %4d Spl   %4d Pkt  %5d Dif      %4d S %4d U %4d N      %4d:%4d", 
				position, team, games, points, getGoalDifference(), wins, draws, losses, teamGoals, opponentGoals );
	}

}
