package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;

@SuppressWarnings("serial")
public abstract class AbstractTeamFilterPanel extends AbstractFilterPanel<Game> implements MouseWheelListener
{

	protected Iterable<String> allTeams;
	
	protected AbstractTeamFilterPanel(Iterable<String> allTeams)
	{
		this.allTeams = allTeams;
	}
	

	protected JComboBox<String> createTeamBox(Iterable<String> allTeams)
	{
		JComboBox<String> teamBox = new JComboBox<String>();
		for(String team : allTeams)
			teamBox.addItem(team);
		teamBox.setMaximumSize(new Dimension(110,30));
		teamBox.setMinimumSize(new Dimension(110,30));
		teamBox.setEditable(true);
		teamBox.addMouseWheelListener(this);
		return teamBox;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int diff = (int) (4 * e.getPreciseWheelRotation());
		if( e.getSource() instanceof JComboBox)
		{
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			int newIndex = box.getSelectedIndex() + diff;
			if( newIndex >= 0 && newIndex < box.getItemCount())
				box.setSelectedIndex( newIndex );
		}
	}


	
	

}
