package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.tools.ComboBoxFactory;

@SuppressWarnings({"serial"})
public class TeamFilterPanel extends AbstractFilterPanel<Game>
{
	private JComboBox<String> teamBox;
	private JCheckBox homeBox;
	private JCheckBox awayBox;
	
	public TeamFilterPanel(List<String> allTeams)
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		ActionListener listener = e -> resetFilter();
		
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(allTeams);
		
		teamBox = cbf.createComboBox();
		teamBox.addActionListener(listener);
		
		homeBox = new JCheckBox("H",true);
		homeBox.setInheritsPopupMenu(true);
		homeBox.addActionListener(listener);
		homeBox.setMaximumSize(new Dimension(50,30));
		homeBox.setMinimumSize(new Dimension(50,30));
		
		awayBox = new JCheckBox("A",true);
		awayBox.setInheritsPopupMenu(true);
		awayBox.addActionListener(listener);
		awayBox.setMaximumSize(new Dimension(50,30));
		awayBox.setMinimumSize(new Dimension(50,30));
		

		setMaximumSize(new Dimension(350,30));
		setMinimumSize(new Dimension(350,30));
	}
	
	public TeamFilterPanel(List<String> allTeams, String team, boolean home, boolean away)
	{
		this(allTeams);
		int index = allTeams.indexOf(team);
		if(index >= 0)
			teamBox.setSelectedIndex(index);
		homeBox.setSelected(home);
		awayBox.setSelected(away);		
	}

	private void resetFilter()
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
