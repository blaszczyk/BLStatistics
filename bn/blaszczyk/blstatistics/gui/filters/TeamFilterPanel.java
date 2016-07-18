package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings({"serial"})
public class TeamFilterPanel extends AbstractTeamFilterPanel
{
	private JComboBox<String> teamBox;
	private JCheckBox homeBox;
	private JCheckBox awayBox;
	
	public TeamFilterPanel(Iterable<String> allTeams)
	{
		super(allTeams);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		teamBox = createTeamBox(allTeams);
		homeBox = new JCheckBox("Heim",true);
		awayBox = new JCheckBox("Auswärts",true);
		
		ActionListener listener = e -> resetFilter();
		teamBox.addActionListener(listener);
		homeBox.addActionListener(listener);
		awayBox.addActionListener(listener);
	}
	
	private void resetFilter()
	{
		String team = teamBox.getSelectedItem().toString();
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
		notifyListeners();
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
	
	@Override
	protected void addComponents()
	{
		add(new JLabel("Team"));
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
