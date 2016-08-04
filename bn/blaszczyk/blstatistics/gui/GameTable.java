package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.Game;

@SuppressWarnings("serial")
public class GameTable extends MyTable<Game>
{

	private List<String> selectedTeams;
	
	public GameTable(Iterable<Game> source)
	{
		super(source);
	}
	
	public GameTable()
	{
		super();
	}
	
	public void setSelectedTeams(List<String> teams)
	{
		this.selectedTeams = teams;
		repaint();
	}

	@Override
	protected boolean isThisRowSelected(int rowIndex)
	{
		if(selectedTeams == null)
			return false;
		return selectedTeams.contains(getModel().getValueAt(rowIndex, 1)) || selectedTeams.contains(getModel().getValueAt(rowIndex, 5));
	}

	@Override
	protected Comparator<Game> comparator(int columnIndex)
	{
		switch(columnIndex)
		{
		case 1:
			return Game.TEAM1_COMPARATOR;
		case 2:
			return Game.GOALS1_COMPARATOR;
		case 3:
			return Game.DIFF_COMPARATOR;
		case 4:
			return Game.GOALS2_COMPARATOR;
		case 5:
			return Game.TEAM2_COMPARATOR;
		default:
			return Game.DATE_COMPARATOR;
		}
	}

	@Override
	protected MyTableModel<Game> createTableModel(List<Game> tList)
	{
		return new GameTableModel(tList);
	}

	@Override
	protected void doPopup(MouseEvent e)
	{
		
	}

	@Override
	protected int columnWidth(int columnIndex)
	{
		if(columnIndex == 0)							//Date
			return 100;
		if( columnIndex == 2 || columnIndex == 4)		//Goals
			return 30;
		if( columnIndex == 3)							// " : "
			return 20;
		return 200;										//Teams
	}

	@Override
	protected int columnAlignment(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
		case 3:
			return SwingConstants.CENTER;
		case 1:
		case 2:
			return SwingConstants.RIGHT;
		case 4:
		case 5:
			return SwingConstants.LEFT;
		}
		return 0;
	}

}
