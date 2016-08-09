package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
public class TeamFilterPanel extends AbstractFilterPanel<Game> implements ActionListener
{
	private static final List<String> TEAM_LIST = new ArrayList<>();
	
	private JComboBox<String> teamBox;
	private JCheckBox homeBox;
	private JCheckBox awayBox;
	
	public TeamFilterPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(TEAM_LIST);
		teamBox = cbf.createComboBox();
		teamBox.addActionListener(this);
		
		homeBox = new JCheckBox("H",true);
		homeBox.setInheritsPopupMenu(true);
		homeBox.addActionListener(this);
		homeBox.setMaximumSize(new Dimension(50,30));
		homeBox.setMinimumSize(new Dimension(50,30));
		
		awayBox = new JCheckBox("A",true);
		awayBox.setInheritsPopupMenu(true);
		awayBox.addActionListener(this);
		awayBox.setMaximumSize(new Dimension(50,30));
		awayBox.setMinimumSize(new Dimension(50,30));
		

		setMaximumSize(new Dimension(350,30));
		setMinimumSize(new Dimension(350,30));
	}
	
	public TeamFilterPanel(String team, boolean home, boolean away)
	{
		this();
		teamBox.setSelectedItem(team);
		homeBox.setSelected(home);
		awayBox.setSelected(away);		
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
		TeamFilterPanel.TEAM_LIST.clear();
		for(String team : teamList)
			TeamFilterPanel.TEAM_LIST.add(team);
	}
	
	public static List<String> getTeamList()
	{
		return TeamFilterPanel.TEAM_LIST;
	}
	
	private void setFilter()
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JCheckBox)
		{
			JCheckBox box = (JCheckBox) e.getSource();
			setFilter();
			box.requestFocusInWindow();
		}
	}

	
}
