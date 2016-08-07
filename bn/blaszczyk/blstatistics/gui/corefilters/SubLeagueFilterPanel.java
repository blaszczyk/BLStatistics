package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;

@SuppressWarnings("serial")
public class SubLeagueFilterPanel extends AbstractFilterPanel<Game> {

	private JLabel label = new JLabel("Direkter Vergleich");
	private ComboBoxFactory<String> cbf;
	private List<JComboBox<String>> teamBoxes;
	private JButton more;
	
	public SubLeagueFilterPanel(List<String> allTeams)
	{
		cbf = new ComboBoxFactory<>(allTeams);
		teamBoxes = new ArrayList<>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		label.setAlignmentX(LEFT_ALIGNMENT);
		
		more = new JButton("Neues Team");
		more.setMaximumSize(new Dimension(250,30));
		more.setMinimumSize(new Dimension(250,30));
		more.addActionListener( e -> {
			addTeamBox();
			paint();
		});
		more.setAlignmentX(LEFT_ALIGNMENT);
		addTeamBox();
		addTeamBox();
	}

	public SubLeagueFilterPanel(List<String> allTeams, Iterable<String> selectedTeams)
	{
		this(allTeams);
		teamBoxes.clear();
		int index;
		for(String team : selectedTeams)
			if((index = allTeams.indexOf(team)) >= 0)
				addTeamBox().setSelectedIndex(index);
		setFilter();
	}
	
	
	private void setFilter()
	{
		List<String> teams = new ArrayList<>();
		for(JComboBox<String> box : teamBoxes)
			teams.add(box.getSelectedItem().toString());
		setFilter(GameFilter.getSubLeagueFilter(teams));
	}
	
	private JComboBox<String> addTeamBox()
	{
		JComboBox<String> box = cbf.createComboBox();
		box.addActionListener(e -> setFilter());
		box.setAlignmentX(LEFT_ALIGNMENT);
		teamBoxes.add(box);
		return box;
	}
	
	public int getTeamCount()
	{
		return teamBoxes.size();
	}
	
	public String getTeam(int index)
	{
		return (String) teamBoxes.get(index).getSelectedItem();
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		for(JComboBox<String> box : teamBoxes)
			add(box);
		add(more);			
	}

	@Override
	public String toString()
	{
		return "Direkter Vergleich " + getTeamCount() + " Teams";
	}
}
