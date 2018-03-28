package bn.blaszczyk.fussballstats.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.gui.tools.MyTable;

@SuppressWarnings("serial")
public class GameTable extends MyTable<Game>
{

	/*
	 * Variables
	 */
	private List<String> selectedTeams = new ArrayList<>();
	
	/*
	 * Constructor
	 */
	public GameTable()
	{
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().addListSelectionListener( e -> repaint());
	}

	/*
	 * Getters, Setters
	 */
	public List<String> getSelectedTeams()
	{
		int selectedIndex = getSelectionModel().getAnchorSelectionIndex();
		if(selectedIndex >= 0)
		{
			int modelSelectedIndex = sorter.convertRowIndexToModel(selectedIndex);
			selectedTeams.clear();
			selectedTeams.add( (String)getModel().getValueAt(modelSelectedIndex, 1) );
			selectedTeams.add( (String)getModel().getValueAt(modelSelectedIndex, 5) );
		}
		return selectedTeams;
	}
	
	public void setSelectedTeams(List<String> teams)
	{
		this.selectedTeams = teams;
		getSelectionModel().setAnchorSelectionIndex(-1);
		repaint();
	}
	
	/*
	 * MyTable Methods
	 */
	
	
	@Override
	protected boolean isThisRowSelected(int rowIndex)
	{
		if(selectedTeams == null)
			return false;
		int modelRowIndex = sorter.convertRowIndexToModel(rowIndex);
		return selectedTeams.contains(getModel().getValueAt(modelRowIndex, 1)) 
				|| selectedTeams.contains(getModel().getValueAt(modelRowIndex, 5));
	}

	@Override
	public void setSource(Iterable<Game> source)
	{
		super.setSource(source);
		if(sorter.getSortKeys().size() <2)
			sorter.setSortKeys(Arrays.asList(new  RowSorter.SortKey[]{ new RowSorter.SortKey(0, SortOrder.DESCENDING),new RowSorter.SortKey(1, SortOrder.ASCENDING)}));
	}

	@Override
	protected TableModel createTableModel(List<Game> tList)
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
		}
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
