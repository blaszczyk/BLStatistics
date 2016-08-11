package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.tools.MyComboBox;

@SuppressWarnings({"serial"})
public class TeamSearchFilterPanel extends AbstractFilterPanel<Game>
{
	public static final String NAME = "TeamSearch";
	private static final List<String> TEAM_LIST = new ArrayList<>();
	
	private JComboBox<String> teamBox;
	private JCheckBox strictBox;
	
	public TeamSearchFilterPanel()
	{
		this("",false);
	}
	
	public TeamSearchFilterPanel(String team, boolean strict)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		teamBox = new MyComboBox<>(TEAM_LIST,250,true);
		teamBox.addActionListener(setFilterListener);
		
		strictBox = new JCheckBox("Exakt",true);
		strictBox.setInheritsPopupMenu(true);
		strictBox.addActionListener(setFilterListener);
		strictBox.setMaximumSize(new Dimension(90,30));
		strictBox.setMinimumSize(new Dimension(90,30));
		

		setMaximumSize(new Dimension(350,30));
		setMinimumSize(new Dimension(350,30));
		teamBox.setSelectedItem(team);
		strictBox.setSelected(strict);	
		setFilter();
	}
	
	public String getTeam()
	{
		return (String) teamBox.getSelectedItem();
	}

	public boolean isStrict()
	{
		return strictBox.isSelected();
	}
	

	public static void setTeamList(Iterable<String> teamList)
	{
		TeamSearchFilterPanel.TEAM_LIST.clear();
		for(String team : teamList)
			TeamSearchFilterPanel.TEAM_LIST.add(team);
	}
	
	public static List<String> getTeamList()
	{
		return TeamSearchFilterPanel.TEAM_LIST;
	}
	
	protected void setFilter()
	{
		String team = (String) teamBox.getSelectedItem();
		Filter<Game> filter;
		if(strictBox.isSelected())
			filter = GameFilter.getTeamFilter(team);
		else
			filter = GameFilter.getTeamContainsFilter(team);
		setFilter(filter);
	}
	
	
	@Override
	protected void addComponents()
	{
		add(teamBox);
		add(strictBox);
	}	

	@Override
	public String toString()
	{
		return "Team Suche " + teamBox.getSelectedItem();
	}
	
}
