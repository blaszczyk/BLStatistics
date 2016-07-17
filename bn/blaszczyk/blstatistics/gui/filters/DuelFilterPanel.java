package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.filters.GameFilter;

@SuppressWarnings("serial")
public class DuelFilterPanel extends AbstractTeamFilterPanel {

	private JComboBox<String> team1Box;
	private JComboBox<String> team2Box;
	
	public DuelFilterPanel(Iterable<String> allTeams)
	{
		super(allTeams);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		team1Box = createTeamBox(allTeams);
		team2Box = createTeamBox(allTeams);
		ActionListener listener = e -> {
			String team1 = team1Box.getSelectedItem().toString();
			String team2 = team2Box.getSelectedItem().toString();
			setFilter(GameFilter.getDuelFilter(team1, team2));
			notifyListeners();
		};
		team1Box.addActionListener(listener);
		team2Box.addActionListener(listener);
	}
	
	public DuelFilterPanel(List<String> allTeams, String team1, String team2)
	{
		this(allTeams);
		int index = allTeams.indexOf(team1);
		if(index>=0)
			team1Box.setSelectedIndex(index);
		index = allTeams.indexOf(team2);
		if(index>=0)
			team2Box.setSelectedIndex(index);
	}
	
	@Override
	protected void addComponents()
	{
		add(team1Box);
		add(new JLabel(" vs "));
		add(team2Box);
	}

}
