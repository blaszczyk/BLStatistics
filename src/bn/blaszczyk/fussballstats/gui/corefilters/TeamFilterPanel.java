package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.GameFilter;
import bn.blaszczyk.fussballstats.filters.LogicalFilter;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings({"serial"})
public class TeamFilterPanel extends AbstractFilterPanel<Game>
{
	public static final String NAME = "Verein";
	private static final List<String> TEAM_LIST = new ArrayList<>();
	
	private JComboBox<String> boxTeam;
	private JCheckBox chbHome;
	private JCheckBox chbAway;
	
	public TeamFilterPanel()
	{
		this("",true,true);
	}
	
	public TeamFilterPanel(String team, boolean home, boolean away)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		boxTeam = new MyComboBox<>(TEAM_LIST,250,true);
		boxTeam.addActionListener(setFilterListener);
		
		chbHome = new JCheckBox("H",true);
		chbHome.setInheritsPopupMenu(true);
		chbHome.addActionListener(setFilterListener);
		chbHome.setMaximumSize(new Dimension(50,30));
		chbHome.setMinimumSize(new Dimension(50,30));
		
		chbAway = new JCheckBox("A",true);
		chbAway.setInheritsPopupMenu(true);
		chbAway.addActionListener(setFilterListener);
		chbAway.setMaximumSize(new Dimension(50,30));
		chbAway.setMinimumSize(new Dimension(50,30));

		setMaximumSize(new Dimension(350,30));
		setMinimumSize(new Dimension(350,30));
		boxTeam.setSelectedItem(team);
		chbHome.setSelected(home);
		chbAway.setSelected(away);		
		setFilter();
	}
	
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
	
	protected void setFilter()
	{
		String team = (String) boxTeam.getSelectedItem();
		Filter<Game> filter = LogicalFilter.getFALSEFilter();
		if(chbHome.isSelected())
			if(chbAway.isSelected())
				filter = GameFilter.getTeamFilter(team);
			else
				filter = GameFilter.getTeamHomeFilter(team);
		else
			if(chbAway.isSelected())
				filter = GameFilter.getTeamAwayFilter(team);
		setFilter(filter);
	}
	
	
	@Override
	protected void addComponents()
	{
		add(boxTeam);
		add(chbHome);
		add(chbAway);
	}	

	@Override
	public String toString()
	{
		return "Verein " + boxTeam.getSelectedItem();
	}
	
}
