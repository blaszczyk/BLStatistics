package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.LogicalFilter;
import bn.blaszczyk.fussballstats.filters.SeasonFilter;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class SingleLeagueFilterPanel extends AbstractFilterPanel<Season>
{
	public static final String NAME = "Liga";
	
	private final static List<String> LEAGUE_LIST = new ArrayList<>();
	
	private JComboBox<String> cboLeagues;
	private JCheckBox boxContains = new JCheckBox("Alle",true);

	public SingleLeagueFilterPanel()
	{
		this("",true);
	}

	public SingleLeagueFilterPanel(String league, boolean isContains)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		cboLeagues = new MyComboBox<>(LEAGUE_LIST,250,false);
		cboLeagues.addActionListener(setFilterListener);

		boxContains.setMinimumSize(new Dimension(60, 30));
		boxContains.setMinimumSize(new Dimension(60, 30));
		boxContains.setInheritsPopupMenu(true);
		boxContains.addActionListener(setFilterListener);
		
		cboLeagues.setSelectedItem(league);
		boxContains.setSelected(isContains);
		
		setFilter();
	}
	
	public String getSelectedLeague()
	{
		return (String) cboLeagues.getSelectedItem();
	}

	public boolean isRecursive()
	{
		return boxContains.isSelected();
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
		else if( boxContains.isSelected())
			filter = SeasonFilter.getLeagueContainsFilter(league);
		else
			filter = SeasonFilter.getLeagueFilter(league);
		setFilter(filter);
	}

	@Override
	protected void addComponents()
	{
		add(cboLeagues);
		add(boxContains);
	}
	
	@Override
	public String toString()
	{
		return getSelectedLeague().toString();
	}

}
