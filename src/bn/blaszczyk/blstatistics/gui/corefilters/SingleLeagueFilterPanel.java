package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.tools.ComboBoxFactory;

@SuppressWarnings("serial")
public class SingleLeagueFilterPanel extends AbstractFilterPanel<Season>
{
	public static final String NAME = "Liga";
	
	private final static List<String> LEAGUE_LIST = new ArrayList<>();
	
	private JComboBox<String> cboLeagues;
	private JCheckBox boxRecursive = new JCheckBox("Alle",true);

	public SingleLeagueFilterPanel()
	{
		this("",true);
	}

	public SingleLeagueFilterPanel(String league, boolean isRecursive)
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(LEAGUE_LIST);
		cboLeagues = cbf.createComboBox();
		cboLeagues.addActionListener(setFilterListener);

		boxRecursive.setMinimumSize(new Dimension(60, 30));
		boxRecursive.setMinimumSize(new Dimension(60, 30));
		boxRecursive.setInheritsPopupMenu(true);
		boxRecursive.addActionListener(setFilterListener);
		
		cboLeagues.setSelectedItem(league);
		boxRecursive.setSelected(isRecursive);
		
		setFilter();
	}
	
	public String getSelectedLeague()
	{
		return (String) cboLeagues.getSelectedItem();
	}

	public boolean isRecursive()
	{
		return boxRecursive.isSelected();
	}

	public static void setLeagueList(Iterable<String> leagueList)
	{
		SingleLeagueFilterPanel.LEAGUE_LIST.clear();
		for(String league : leagueList)
			SingleLeagueFilterPanel.LEAGUE_LIST.add(league);
	}
	
	protected void setFilter()
	{
		String league = (String) cboLeagues.getSelectedItem();
		Filter<Season> filter;
		if(league == null)
			filter = LogicalFilter.getTRUEFilter();
		else if( boxRecursive.isSelected())
			filter = SeasonFilter.getLeagueRecursiveFilter(league);
		else
			filter = SeasonFilter.getLeagueFilter(league);
		setFilter(filter);
	}

	@Override
	protected void addComponents()
	{
		add(cboLeagues);
		add(boxRecursive);
	}
	
	@Override
	public String toString()
	{
		return getSelectedLeague().toString();
	}

}
