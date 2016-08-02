package bn.blaszczyk.blstatistics.gui;

import java.util.Comparator;
import java.util.List;

import bn.blaszczyk.blstatistics.core.TeamResult;

public class ResultTableModel extends MyTableModel<TeamResult> 
{	
	public ResultTableModel(List<TeamResult> results)
	{
		super(results);
	}
	

	public static Comparator<TeamResult> comparator(int columnIndex)
	{	
		switch (columnIndex)
		{
		case 1:
			return TeamResult.COMPARE_TEAM;
		case 2:
			return TeamResult.COMPARE_GAMES;
		case 3:
			return TeamResult.COMPARE_POINTS;
		case 4:
			return TeamResult.COMPARE_DIFF;
		case 5:
			return TeamResult.COMPARE_WINS;
		case 6:
			return TeamResult.COMPARE_DRAWS;
		case 7:
			return TeamResult.COMPARE_LOSSES;
		case 8:
			return TeamResult.COMPARE_GOALS_TEAM;
		case 9:
			return TeamResult.COMPARE_GOALS_OPPONENT;
		// case 0:
		default:
			return TeamResult.COMPARE_POSITION;
		}
	}
	
	
	/*
	 * Table Model Methods
	 */
	
	@Override
	public int getColumnCount()
	{
		return 10;
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return "Platz";
		case 1:
			return "Verein";
		case 2:
			return "Spiele";
		case 3:
			return "Punkte";
		case 4:
			return "Tordifferenz";
		case 5:
			return "Siege";
		case 6:
			return "Unentschieden";
		case 7:
			return "Niederlagen";
		case 8:
			return "Tore";
		case 9:
			return "Gegentore";		
		}
		return null;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex == 1)
			return String.class;
		return Integer.class;
	}

	@Override
	protected Object getColumnValue(TeamResult result, int columnIndex)
	{	
		switch(columnIndex)
		{
		case 0:
			return result.getPosition();
		case 1:
			return result.getTeam();
		case 2:
			return result.getGames();
		case 3:
			return result.getPoints();
		case 4:
			return result.getGoalDifference();
		case 5:
			return result.getWins();
		case 6:
			return result.getDraws();
		case 7:
			return result.getLosses();
		case 8:
			return result.getTeamGoals();
		case 9:
			return result.getOpponentGoals();		
		}
		return null;
	}
	

}
