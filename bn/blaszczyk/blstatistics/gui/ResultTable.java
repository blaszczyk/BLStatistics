package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.table.TableModel;

import bn.blaszczyk.blstatistics.core.TeamResult;

@SuppressWarnings("serial")
public class ResultTable extends SwingTable<TeamResult>
{

	public ResultTable(Iterable<TeamResult> source)
	{
		super(source);
	}

	@Override
	protected Comparator<TeamResult> comparator(int columnIndex)
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

	@Override
	protected TableModel tableModel(List<TeamResult> ts)
	{
		return new ResultTableModel(ts);
	}

	@Override
	protected void doPopup(MouseEvent e)
	{
	}

	@Override
	protected int columnWidth(int columnIndex)
	{
		if( columnIndex == 0)
			return 50;
		if( columnIndex < 2)
			return 200;
		if( columnIndex < 5)
			return 100;
		if( columnIndex < 8)
			return 75;
		return 75;
	}

}
