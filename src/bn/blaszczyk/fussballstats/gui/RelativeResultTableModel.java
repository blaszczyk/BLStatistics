package bn.blaszczyk.fussballstats.gui;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.fussballstats.core.TeamResult;


public class RelativeResultTableModel implements TableModel
{

	private final List<TeamResult> results;
	
	public RelativeResultTableModel(List<TeamResult> results)
	{
		this.results = results;
	}	
	
	/*
	 * Table Model Methods
	 */
	
	@Override
	public int getColumnCount()
	{
		return 11;
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
			return "Tordiff.";
		case 5:
			return "S";
		case 6:
			return "U";
		case 7:
			return "N";	
		}
		return null;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex == 1 || columnIndex == 9)
			return String.class;
		if(columnIndex >= 3)
			return Double.class;
		return Integer.class;
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
			return result.getGames();
		case 9:
			return " : ";		
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
		case 10:
			value =  result.getOpponentGoals();	
			break;	
		}
		if(result.getGames() == 0)
			return 0.;
		return  value / result.getGames() ;
	}
	
	@Override
	public int getRowCount()
	{
		return results.size();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}


	@Override
	public void addTableModelListener(TableModelListener l)
	{
	}
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}
	
	
}



