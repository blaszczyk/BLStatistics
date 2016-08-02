package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;

import bn.blaszczyk.blstatistics.core.Game;

@SuppressWarnings("serial")
public class GameTable extends SwingTable<Game>
{

	private String selectedTeam;
	
	public GameTable(Iterable<Game> source)
	{
		super(source);
	}
	
	public GameTable()
	{
		super();
	}
	
	public void setSelectedTeam(String team)
	{
		this.selectedTeam = team;
		setCellRenderer();
		repaint();
	}

	@Override
	protected boolean isThisRowSelected(int rowIndex)
	{
		return // super.isThisRowSelected(rowIndex) || 
				getModel().getValueAt(rowIndex, 1).equals(selectedTeam) || getModel().getValueAt(rowIndex, 2).equals(selectedTeam);
	}

	@Override
	protected Comparator<Game> comparator(int columnIndex)
	{
		switch(columnIndex)
		{
		case 1:
			return Game.TEAM1_COMPARATOR;
		case 2:
			return Game.TEAM2_COMPARATOR;
		case 3:
			return Game.GOALS1_COMPARATOR;
		case 4:
			return Game.GOALS2_COMPARATOR;
//		case 0:
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
		if(columnIndex == 0)
			return 100;
		if( columnIndex < 3)
			return 150;
		return 100;
	}

}
