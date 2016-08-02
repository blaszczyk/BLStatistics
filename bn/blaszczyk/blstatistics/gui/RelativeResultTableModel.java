package bn.blaszczyk.blstatistics.gui;

import java.util.Comparator;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.blstatistics.core.TeamResult;


public class RelativeResultTableModel implements TableModel {
	
	private List<TeamResult> results;

	public RelativeResultTableModel(List<TeamResult> results)
	{
		this.results = results;
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
			return TeamResult.COMPARE_POINTS_REL;
		case 4:
			return TeamResult.COMPARE_DIFF_REL;
		case 5:
			return TeamResult.COMPARE_WINS_REL;
		case 6:
			return TeamResult.COMPARE_DRAWS_REL;
		case 7:
			return TeamResult.COMPARE_LOSSES_REL;
		case 8:
			return TeamResult.COMPARE_GOALS_TEAM_REL;
		case 9:
			return TeamResult.COMPARE_GOALS_OPPONENT_REL;
		// case 0:
		default:
			return TeamResult.COMPARE_POSITION;
		}
	}
	
	
	/*
	 * Table Model Methods
	 */
	@Override
	public int getRowCount()
	{
		return results.size();
	}
	
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
//		if(columnIndex == 1)
			return String.class;
//		return Double.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		TeamResult result = results.get(rowIndex);
		double value = 0;
		switch(columnIndex)
		{
		case 0:
			return result.getPosition();
		case 1:
			return result.getTeam();
		case 2:
			return "" + result.getGames();
		case 3:
			value =  result.getPoints();
			break;
		case 4:
			value =  result.getGoalDifference();
			break;
		case 5:
			value =  result.getWins();
			break;
		case 6:
			value =  result.getDraws();
			break;
		case 7:
			value =  result.getLosses();
			break;
		case 8:
			value =  result.getTeamGoals();
			break;
		case 9:
			value =  result.getOpponentGoals();	
			break;	
		}
		if(result.getGames() == 0)
			return "0";
		return String.format("%3.3f", value / result.getGames() );
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}
	
	@Override
	public void addTableModelListener(TableModelListener l)
	{
	}
	
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
	}
	
}



