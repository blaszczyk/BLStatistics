package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.League;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;

@SuppressWarnings("serial")
public class SingleLeagueFilterPanel extends AbstractFilterPanel<Season>
{
	private JComboBox<League> cboLeagues;
	private JCheckBox boxRecursive = new JCheckBox("Alle",true);

	public SingleLeagueFilterPanel(Iterable<League> allLeagues)
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		ComboBoxFactory cbf = new ComboBoxFactory(allLeagues, ComboBoxFactory.LEAGUE);
		cboLeagues = cbf.createLeagueBox();
		cboLeagues.addActionListener(e -> resetFilter());

		boxRecursive.setMinimumSize(new Dimension(60, 30));
		boxRecursive.setMinimumSize(new Dimension(60, 30));
		boxRecursive.setInheritsPopupMenu(true);
		boxRecursive.addActionListener(e -> resetFilter());
	}

	public SingleLeagueFilterPanel(Iterable<League> allLeagues, String league, boolean isRecursive)
	{
		this(allLeagues);
		cboLeagues.setSelectedItem(findLeague(allLeagues, league));
		boxRecursive.setSelected(isRecursive);
	}
	
	public League getSelectedLeague()
	{
		return (League) cboLeagues.getSelectedItem();
	}

	public boolean isRecursive()
	{
		return boxRecursive.isSelected();
	}
	
	private League findLeague(Iterable<League> leagues, String name)
	{
		for(League l : leagues)
			if(l.getName().equals(name))
				return l;
		return null;
	}
	private void resetFilter()
	{
		League league = (League) cboLeagues.getSelectedItem();
		Filter<Season> filter;
		if(league == null)
			filter = LogicalFilter.getTRUEFilter();
		else if( boxRecursive.isSelected())
			filter = SeasonFilter.getLeagueRecursiveFilter(league);
		else
			filter = SeasonFilter.getLeagueFilter(league);
		setFilter(filter);
		notifyListeners(new FilterEvent<Season>(this, getFilter(), FilterEvent.RESET_FILTER));
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
