package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;

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
			setFilter(GameFilter.getDuelFilter(getTeam1(), getTeam2()));
			notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
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

	public String getTeam1()
	{
		return (String) team1Box.getSelectedItem();
	}
	
	public String getTeam2()
	{
		return (String) team2Box.getSelectedItem();
	}
	
	@Override
	protected void addComponents()
	{
		add(team1Box);
		add(new JLabel(" vs "));
		add(team2Box);
	}

	@Override
	public String toString()
	{
		return getTeam1() + " VS " + getTeam2();
	}
}
