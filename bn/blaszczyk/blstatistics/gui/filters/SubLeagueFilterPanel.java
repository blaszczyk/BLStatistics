package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.filters.GameFilter;

@SuppressWarnings("serial")
public class SubLeagueFilterPanel extends AbstractTeamFilterPanel {

	private List<JComboBox<String>> teamBoxes;
	private JButton more;
	private ActionListener listener;
	private JLabel label = new JLabel("Direkter Vergleich");
	
	public SubLeagueFilterPanel(Iterable<String> allTeams)
	{
		super(allTeams);
		teamBoxes = new ArrayList<>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		listener = e -> {
				List<String> teams = new ArrayList<>();
				for(JComboBox<String> box : teamBoxes)
					teams.add(box.getSelectedItem().toString());
				setFilter(GameFilter.getSubLeagueFilter(teams));
				notifyListeners();
			};
		label.setAlignmentX(LEFT_ALIGNMENT);
		
		more = new JButton("Neues Team");
		more.addActionListener( e -> {
			addTeamBox();
			paint();
		});
		more.setAlignmentX(LEFT_ALIGNMENT);
		addTeamBox();
		addTeamBox();
	}
	
	public SubLeagueFilterPanel(List<String> allTeams, String...teams)
	{
		super(allTeams);
		teamBoxes.clear();
		int index;
		for(String team : teams)
			if((index = allTeams.indexOf(team)) >= 0)
				addTeamBox().setSelectedIndex(index);
	}
	
	private JComboBox<String> addTeamBox()
	{
		JComboBox<String> box = createTeamBox(allTeams);
		box.addActionListener(listener);
		box.setAlignmentX(LEFT_ALIGNMENT);
		teamBoxes.add(box);
		return box;
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		for(JComboBox<String> box : teamBoxes)
			add(box);
		add(more);			
	}
	
}
