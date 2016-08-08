package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.tools.ComboBoxFactory;

@SuppressWarnings("serial")
public class SubLeagueFilterPanel extends AbstractFilterPanel<Game> {

	private ComboBoxFactory<String> cbf;
	
	private JLabel label = new JLabel("Direkter Vergleich");
	private List<JComboBox<String>> teamBoxes = new ArrayList<>();
	private JButton btnNewTeam = new JButton("Neues Team");

	private JMenu popupRemoveTeam;
	
	public SubLeagueFilterPanel(List<String> allTeams)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		cbf = new ComboBoxFactory<>(allTeams);
		
		label.setAlignmentX(LEFT_ALIGNMENT);
		
		btnNewTeam.setMaximumSize(new Dimension(250,30));
		btnNewTeam.setMinimumSize(new Dimension(250,30));
		btnNewTeam.setAlignmentX(LEFT_ALIGNMENT);
		btnNewTeam.addActionListener( e -> addTeamBox() );
		
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
	}
	
	public int getTeamCount()
	{
		return teamBoxes.size();
	}
	
	public String getTeam(int index)
	{
		return (String) teamBoxes.get(index).getSelectedItem();
	}
	
	private void setFilter()
	{
		List<String> teams = new ArrayList<>();
		for(JComboBox<String> box : teamBoxes)
			teams.add(box.getSelectedItem().toString());
		setDeleteMenu();
		setFilter(GameFilter.getSubLeagueFilter(teams));
	}
	
	private JComboBox<String> addTeamBox()
	{
		JComboBox<String> box = cbf.createComboBox();
		box.addActionListener(e -> setFilter());
		box.setAlignmentX(LEFT_ALIGNMENT);
		teamBoxes.add(box);
		setFilter();
		return box;
	}

	private void removeTeam(JComboBox<String> box)
	{
		teamBoxes.remove(box);
		setFilter();
	}
	
	private void setDeleteMenu()
	{
		popupRemoveTeam.removeAll();
		for(JComboBox<String> box : teamBoxes)
		{
			JMenuItem remove = new JMenuItem(box.getSelectedItem().toString());
			remove.addActionListener( e -> removeTeam(box));
			popupRemoveTeam.add(remove);
		}
	}
	
	@Override
	protected void addPopupMenuItems()
	{
		popupRemoveTeam  = new JMenu("Entferne Team");
		addPopupMenuItem(popupRemoveTeam);
		super.addPopupMenuItems();
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		for(JComboBox<String> box : teamBoxes)
			add(box);
		add(btnNewTeam);			
	}

	@Override
	public String toString()
	{
		return "Direkter Vergleich " + getTeamCount() + " Teams";
	}
}
