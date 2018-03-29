package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.Team;
import bn.blaszczyk.fussballstats.filters.GameFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class SubLeagueFilterPanel extends AbstractFilterPanel<Game> {

	/*
	 * Constants
	 */
	public static final String NAME = "DirekterVergleich";
	private static final String[] EMPTY_TWO_ARRAY = {"",""};
	
	/*
	 * Components
	 */
	private final JLabel label = new JLabel("Direkter Vergleich");
	private final List<JComboBox<Team>> teamBoxes = new ArrayList<>();
	private final JButton btnNewTeam = new JButton("Verein Hinzufügen");

	/*
	 * Menus
	 */
	private JMenu menuRemoveTeam = new JMenu("Verein Entfernen");
	
	/*
	 * Constructors
	 */
	public SubLeagueFilterPanel()
	{
		this(Arrays.asList(EMPTY_TWO_ARRAY));
	}

	public SubLeagueFilterPanel(Iterable<String> selectedTeams)
	{
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		label.setAlignmentX(LEFT_ALIGNMENT);
		
		btnNewTeam.setMaximumSize(new Dimension(250,30));
		btnNewTeam.setMinimumSize(new Dimension(250,30));
		btnNewTeam.setAlignmentX(LEFT_ALIGNMENT);
		btnNewTeam.setInheritsPopupMenu(true);
		btnNewTeam.setMnemonic('v');
		btnNewTeam.addActionListener( e -> addTeamBox(null) );
		
		for(String team : selectedTeams)
			addTeamBox(team);
	}
	
	/*
	 * Getters, Delegates
	 */
	public int getTeamCount()
	{
		return teamBoxes.size();
	}
	
	public String getTeam(int index)
	{
		return (String) teamBoxes.get(index).getSelectedItem();
	}

	public JMenuItem getMenuRemoveTeam()
	{
		return menuRemoveTeam;
	}
	/*
	 * Internal Methods
	 */
	private JComboBox<Team> addTeamBox(String team)
	{
		JComboBox<Team> box =  new MyComboBox<>(TeamFilterPanel.getTeams(),250,true,Team[]::new);
		box.addActionListener(setFilterListener);
		box.setAlignmentX(LEFT_ALIGNMENT);
		if(team != null)
			box.setSelectedItem(team);
		teamBoxes.add(box);
		setFilter();
		box.requestFocusInWindow();
		return box;
	}

	private void removeTeam(JComboBox<Team> box)
	{
		teamBoxes.remove(box);
		setFilter();
	}
	
	private void setRemoveMenu()
	{
		menuRemoveTeam.removeAll();
		for(JComboBox<Team> box : teamBoxes)
		{
			JMenuItem remove = new JMenuItem(box.getSelectedItem().toString());
			remove.addActionListener( e -> removeTeam(box));
			menuRemoveTeam.add(remove);
		}
	}
	
	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		List<Team> teams = new ArrayList<>(teamBoxes.size());
		for(JComboBox<Team> box : teamBoxes)
			if(box.getSelectedItem() != null )
				teams.add((Team) box.getSelectedItem());
		setRemoveMenu();
		setFilter(GameFilterFactory.createSubLeagueFilter(teams));
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		for(JComboBox<Team> box : teamBoxes)
			add(box);
		add(btnNewTeam);			
	}

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return "Direkter Vergleich " + getTeamCount() + " Vereine";
	}

}
