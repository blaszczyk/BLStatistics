package bn.blaszczyk.fussballstats.gui;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.fussballstats.core.TeamResult;
import bn.blaszczyk.fussballstats.tools.TeamAlias;

public class ResultTableModel implements TableModel
{	
	private List<TeamResult> results;
	
	public ResultTableModel(List<TeamResult> results)
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
		return Integer.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		TeamResult result = results.get(rowIndex);
		switch(columnIndex)
		{
		case 0:
			return result.getPosition();
		case 1:
			return TeamAlias.getAlias(result.getTeam());
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
			return " : ";		
		case 10:
			return result.getOpponentGoals();		
		}
		return null;
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
