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
	public static final String NAME = "Team";
	private static final List<String> TEAM_LIST = new ArrayList<>();
	
	private JComboBox<String> teamBox;
	private JCheckBox homeBox;
	private JCheckBox awayBox;
	
	public TeamFilterPanel()
	{
		this("",true,true);
	}
	
	public TeamFilterPanel(String team, boolean home, boolean away)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		teamBox = new MyComboBox<>(TEAM_LIST,250,false);
		teamBox.addActionListener(setFilterListener);
		
		homeBox = new JCheckBox("H",true);
		homeBox.setInheritsPopupMenu(true);
		homeBox.addActionListener(setFilterListener);
		homeBox.setMaximumSize(new Dimension(50,30));
		homeBox.setMinimumSize(new Dimension(50,30));
		
		awayBox = new JCheckBox("A",true);
		awayBox.setInheritsPopupMenu(true);
		awayBox.addActionListener(setFilterListener);
		awayBox.setMaximumSize(new Dimension(50,30));
		awayBox.setMinimumSize(new Dimension(50,30));

		setMaximumSize(new Dimension(350,30));
		setMinimumSize(new Dimension(350,30));
		teamBox.setSelectedItem(team);
		homeBox.setSelected(home);
		awayBox.setSelected(away);		
		setFilter();
	}
	
	public String getTeam()
	{
		return (String) teamBox.getSelectedItem();
	}

	public boolean getHome()
	{
		return homeBox.isSelected();
	}
	
	public boolean getAway()
	{
		return awayBox.isSelected();
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
		String team = (String) teamBox.getSelectedItem();
		Filter<Game> filter = LogicalFilter.getFALSEFilter();
		if(homeBox.isSelected())
			if(awayBox.isSelected())
				filter = GameFilter.getTeamFilter(team);
			else
				filter = GameFilter.getTeamHomeFilter(team);
		else
			if(awayBox.isSelected())
				filter = GameFilter.getTeamAwayFilter(team);
		setFilter(filter);
	}
	
	
	@Override
	protected void addComponents()
	{
		add(teamBox);
		add(homeBox);
		add(awayBox);
	}	

	@Override
	public String toString()
	{
		return "Team " + teamBox.getSelectedItem();
	}
	
}
