package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractBiFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.FilterPanelManager;

@SuppressWarnings("serial")
public class RoundFilterPanel extends AbstractBiFilterPanel<Season, Game>
{
	private JCheckBox first = new JCheckBox("Hinrunde",true);
	private JCheckBox second = new JCheckBox("Rückrunde",true);

	public RoundFilterPanel(FilterPanelManager<Season,Game> filterFactory)
	{
		super(filterFactory);
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		ActionListener listener = e -> resetFilter();
		first.addActionListener(listener);
		first.setInheritsPopupMenu(true);
		second.addActionListener(listener);
		second.setInheritsPopupMenu(true);
		resetFilter();
	}

	public RoundFilterPanel(FilterPanelManager<Season,Game> filterFactory, boolean firstRound, boolean secondRound)
	{
		this(filterFactory);
		first.setSelected(firstRound);
		second.setSelected(secondRound);
		resetFilter();
	}
	
	private void resetFilter()
	{
		if(isFirstRound())
			if(isSecondRound())
				setFilter(LogicalBiFilter.getTRUEBiFilter());
			else
				setFilter(SeasonFilter.getFirstRoundFilter());
		else
			if(isSecondRound())
				setFilter(SeasonFilter.getSecondRoundFilter());
			else
				setFilter(LogicalBiFilter.getFALSEBiFilter());
		notifyListeners(new BiFilterEvent<Season,Game>(this, getFilter(), FilterEvent.RESET_FILTER));
	}

	public boolean isFirstRound()
	{
		return first.isSelected();
	}
	
	public boolean isSecondRound()
	{
		return second.isSelected();
	}
	
	@Override
	protected void addComponents()
	{
		add(first);
		add(second);
	}
	
	@Override
	public String toString()
	{
		return "Hin- / Rückrunde";
	}

}
