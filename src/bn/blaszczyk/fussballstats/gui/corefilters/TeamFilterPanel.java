package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.core.Game;
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
	private static final List<String> TEAM_LIST = new ArrayList<>();
	
	/*
	 * Components
	 */
	private final JComboBox<String> boxTeam = new MyComboBox<>(TEAM_LIST,250,true);
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
	public String getTeam()
	{
		return (String) boxTeam.getSelectedItem();
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
	public static void setTeamList(Iterable<String> teamList)
	{
		TEAM_LIST.clear();
		for(String team : teamList)
			TEAM_LIST.add(team);
	}
	
	public static List<String> getTeamList()
	{
		return TeamFilterPanel.TEAM_LIST;
	}
	
	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		String team = (String) boxTeam.getSelectedItem();
		Filter<Game> filter = LogicalFilterFactory.createFALSEFilter();
		if(chbHome.isSelected())
			if(chbAway.isSelected())
				filter = GameFilterFactory.createTeamFilter(team);
			else
				filter = GameFilterFactory.createTeamHomeFilter(team);
		else
			if(chbAway.isSelected())
				filter = GameFilterFactory.createTeamAwayFilter(team);
		setFilter(filter);
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
