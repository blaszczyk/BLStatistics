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
	
	private JComboBox<String> boxLeagues;
	private JCheckBox chbContains = new JCheckBox("Alle",true);

	public SingleLeagueFilterPanel()
	{
		this("",true);
	}

	public SingleLeagueFilterPanel(String league, boolean isContains)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		boxLeagues = new MyComboBox<>(LEAGUE_LIST,250,false);
		boxLeagues.addActionListener(setFilterListener);

		chbContains.setMinimumSize(new Dimension(60, 30));
		chbContains.setMinimumSize(new Dimension(60, 30));
		chbContains.setInheritsPopupMenu(true);
		chbContains.addActionListener(setFilterListener);
		
		boxLeagues.setSelectedItem(league);
		chbContains.setSelected(isContains);
		
		setFilter();
	}
	
	public String getSelectedLeague()
	{
		return (String) boxLeagues.getSelectedItem();
	}

	public boolean isRecursive()
	{
		return chbContains.isSelected();
	}

	public static void setLeagueList(Iterable<String> leagueList)
	{
		SingleLeagueFilterPanel.LEAGUE_LIST.clear();
		for(String league : leagueList)
			SingleLeagueFilterPanel.LEAGUE_LIST.add(league);
	}
	
	protected void setFilter()
	{
		String league = (String) boxLeagues.getSelectedItem();
		Filter<Season> filter;
		if(league == null)
			filter = LogicalFilter.getTRUEFilter();
		else if( chbContains.isSelected())
			filter = SeasonFilter.getLeagueContainsFilter(league);
		else
			filter = SeasonFilter.getLeagueFilter(league);
		setFilter(filter);
	}

	@Override
	protected void addComponents()
	{
		add(boxLeagues);
		add(chbContains);
	}
	
	@Override
	public String toString()
	{
		return getSelectedLeague().toString();
	}

}
