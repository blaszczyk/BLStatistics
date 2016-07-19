package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractBiFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.PanelMenu;

@SuppressWarnings("serial")
public class RoundFilterPanel extends AbstractBiFilterPanel<Season, Game>
{
	private JLabel label = new JLabel("Runden ");
	private JCheckBox first = new JCheckBox("Hinrunde",true);
	private JCheckBox second = new JCheckBox("Rückrunde",true);

	public RoundFilterPanel(PanelMenu<Season,Game> panelMenu)
	{
		super(panelMenu);
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		ActionListener listener = e -> resetFilter();
		first.addActionListener(listener);
		second.addActionListener(listener);
		resetFilter();
	}
	
	private void resetFilter()
	{
		if(first.isSelected())
			if(second.isSelected())
				setFilter(LogicalBiFilter.getTRUEBiFilter());
			else
				setFilter(SeasonFilter.getFirstRoundFilter());
		else
			if(second.isSelected())
				setFilter(SeasonFilter.getSecondRoundFilter());
			else
				setFilter(LogicalBiFilter.getFALSEBiFilter());
		notifyListeners(new BiFilterEvent<Season,Game>(this, getFilter(), FilterEvent.RESET_FILTER));
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		add(first);
		add(second);
	}
	
	@Override
	public String toString()
	{
		return "Hin- / Rückrunde";
	}

}
