package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;

import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;

@SuppressWarnings("serial")
public abstract class AbstractTeamFilterPanel extends AbstractFilterPanel<Game> {

	protected Iterable<String> allTeams;
	
	protected AbstractTeamFilterPanel(Iterable<String> allTeams)
	{
		this.allTeams = allTeams;
	}
	

	protected static JComboBox<String> createTeamBox(Iterable<String> allTeams)
	{
		JComboBox<String> teamBox = new JComboBox<String>();
		for(String team : allTeams)
			teamBox.addItem(team);
		teamBox.setMaximumSize(new Dimension(110,30));
		teamBox.setMinimumSize(new Dimension(110,30));
		teamBox.setEditable(true);
		return teamBox;
	}

}
