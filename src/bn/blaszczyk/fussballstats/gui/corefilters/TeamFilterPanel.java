package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.Team;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.GameFilterFactory;
import bn.blaszczyk.fussballstats.filters.LogicalFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings({"serial"})
public class TeamFilterPanel extends AbstractFilterPanel<Game>
{
	/*
	 * Constants
	 */
	public static final String NAME = "Verein";
	private static final Map<String,Team> TEAM_MAP = new HashMap<>();
	
	/*
	 * Components
	 */
	private final JComboBox<Team> boxTeam = new MyComboBox<>(TEAM_MAP.values(),250,true,Team[]::new);
	private final JCheckBox chbHome = new JCheckBox("H",true);
	private final JCheckBox chbAway = new JCheckBox("A",true);
	
	/*
	 * Constructors
	 */
	public TeamFilterPanel()
	{
		this("",true,true);
	}
	
	public TeamFilterPanel(String team, boolean home, boolean away)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		boxTeam.addActionListener(setFilterListener);
		
		chbHome.setInheritsPopupMenu(true);
		chbHome.setOpaque(false);
		chbHome.addActionListener(setFilterListener);
		chbHome.setMaximumSize(new Dimension(50,30));
		chbHome.setMinimumSize(new Dimension(50,30));
		
		chbAway.setInheritsPopupMenu(true);
		chbAway.setOpaque(false);
		chbAway.addActionListener(setFilterListener);
		chbAway.setMaximumSize(new Dimension(50,30));
		chbAway.setMinimumSize(new Dimension(50,30));

		boxTeam.setSelectedItem(team);
		chbHome.setSelected(home);
		chbAway.setSelected(away);		
		setFilter();
	}
	
	/*
	 * Getters
	 */
	public Team getTeam()
	{
		final Object selectedItem = boxTeam.getSelectedItem();
		if(selectedItem instanceof Team)
			return (Team) selectedItem;
		if(selectedItem instanceof String)
			return TEAM_MAP.get(selectedItem);
		return null;
	}

	public boolean isHome()
	{
		return chbHome.isSelected();
	}
	
	public boolean isAway()
	{
		return chbAway.isSelected();
	}

	/*
	 * Static Methods for TeamList
	 */
	public static void setTeams(Collection<Team> teamList)
	{
		teamList.forEach(t -> TEAM_MAP.put(t.getName(), t));
	}
	
	public static Collection<Team> getTeams()
	{
		return TEAM_MAP.values();
	}
	
	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		final Team team = getTeam();
		if(team != null)
		{
			final Filter<Game> filter = isHome() ?
				( isAway() ? GameFilterFactory.createTeamFilter(team) : GameFilterFactory.createTeamHomeFilter(team) ) :
				( isAway() ? GameFilterFactory.createTeamAwayFilter(team) : LogicalFilterFactory.createFALSEFilter() ) ;
			setFilter(filter);
		}
		else
			setFilter(LogicalFilterFactory.createTRUEFilter());
	}
	
	@Override
	protected void addComponents()
	{
		add(boxTeam);
		add(chbHome);
		add(chbAway);
	}	

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return "Verein " + boxTeam.getSelectedItem();
	}
	
}
