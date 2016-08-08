package bn.blaszczyk.blstatistics.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.gui.tools.MyTable;
import bn.blaszczyk.blstatistics.gui.tools.MyTableModel;

@SuppressWarnings("serial")
public class GameTable extends MyTable<Game>
{

	private List<String> selectedTeams;
	
	public GameTable()
	{
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().addListSelectionListener( e -> repaint());
	}

	public List<String> getSelectedTeams()
	{
		int selectedIndex = getSelectionModel().getAnchorSelectionIndex();
		if(selectedIndex < 0)
			return selectedTeams;
		selectedTeams = new ArrayList<>();
		selectedTeams.add( (String)getModel().getValueAt(selectedIndex, 1) );
		selectedTeams.add( (String)getModel().getValueAt(selectedIndex, 5) );
		return selectedTeams;
	}
	
	public void setSelectedTeams(List<String> teams)
	{
		this.selectedTeams = teams;
		getSelectionModel().setAnchorSelectionIndex(-1);
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
	protected int columnWidth(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return 100;
		case 1:
			return 200;
		case 2:
		case 4:
			return 30;
		case 3:
			return 20;
		default:
			return 230;	
		}								//Teams
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
