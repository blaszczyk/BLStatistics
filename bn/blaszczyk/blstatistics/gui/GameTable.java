package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.TableModel;

import bn.blaszczyk.blstatistics.core.Game;

@SuppressWarnings("serial")
public class GameTable extends SwingTable<Game>
{

	public GameTable(Iterable<Game> source)
	{
		super(source);
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
	protected TableModel tableModel(List<Game> tList)
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
			return 200;
		return 100;
	}

}
