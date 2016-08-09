package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractBiFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.FilterPanelManager;

@SuppressWarnings("serial")
public class RoundFilterPanel extends AbstractBiFilterPanel<Season, Game> implements ActionListener
{
	private JCheckBox first = new JCheckBox("Hinrunde",true);
	private JCheckBox second = new JCheckBox("Rückrunde",true);

	public RoundFilterPanel(FilterPanelManager<Season,Game> filterFactory)
	{
		super(filterFactory);
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		first.addActionListener(this);
		first.setInheritsPopupMenu(true);
		second.addActionListener(this);
		second.setInheritsPopupMenu(true);
		setFilter();
	}

	public RoundFilterPanel(FilterPanelManager<Season,Game> filterFactory, boolean firstRound, boolean secondRound)
	{
		this(filterFactory);
		first.setSelected(firstRound);
		second.setSelected(secondRound);
		setFilter();
	}
	
	private void setFilter()
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JCheckBox)
		{
			JCheckBox box = (JCheckBox) e.getSource();
			setFilter();
			box.requestFocusInWindow();
		}
	}
}
