package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.model.League;
import bn.blaszczyk.fussballstats.model.Season;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.LogicalFilterFactory;
import bn.blaszczyk.fussballstats.filters.SeasonFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class LeagueFilterPanel extends AbstractFilterPanel<Season>
{
	/*
	 * Constants
	 */
	public static final String NAME = "Liga";
	private static final  List<League> LEAGUE_LIST = new ArrayList<>();
	
	/*
	 * Components
	 */
	private final JComboBox<League> boxLeagues = new MyComboBox<>(LEAGUE_LIST,250,false,League[]::new);
	private final JCheckBox chbContains = new JCheckBox("Subligen",true);

	/*
	 * Constructors
	 */
	public LeagueFilterPanel()
	{
		this("",true);
	}

	public LeagueFilterPanel(String league, boolean isContains)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		boxLeagues.addActionListener(setFilterListener);

		chbContains.setMinimumSize(new Dimension(60, 30));
		chbContains.setMinimumSize(new Dimension(60, 30));
		chbContains.setInheritsPopupMenu(true);
		chbContains.setOpaque(false);
		chbContains.addActionListener(setFilterListener);
		
		boxLeagues.setSelectedItem(league);
		chbContains.setSelected(isContains);
		
		setFilter();
	}
	
	/*
	 * Getters
	 */
	public League getSelectedLeague()
	{
		return (League) boxLeagues.getSelectedItem();
	}

	public boolean isContains()
	{
		return chbContains.isSelected();
	}

	/*
	 * Static Setter
	 */
	public static void setLeagues(Collection<League> leagues)
	{
		LeagueFilterPanel.LEAGUE_LIST.clear();
		LEAGUE_LIST.addAll(leagues);
	}
	
	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		League league = (League) boxLeagues.getSelectedItem();
		Filter<Season> filter;
		if(league == null)
			filter = LogicalFilterFactory.createTRUEFilter();
		else if( chbContains.isSelected())
			filter = SeasonFilterFactory.createLeagueContainsFilter(league);
		else
			filter = SeasonFilterFactory.createLeagueFilter(league);
		setFilter(filter);
	}

	@Override
	protected void addComponents()
	{
		add(boxLeagues);
		add(chbContains);
	}

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		if(getSelectedLeague() != null)
			return getSelectedLeague().getName();
		return "No League Selected";
	}

}
