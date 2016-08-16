package bn.blaszczyk.fussballstats.gui.corefilters;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.filters.LogicalBiFilter;
import bn.blaszczyk.fussballstats.filters.SeasonFilter;
import bn.blaszczyk.fussballstats.gui.filters.AbstractBiFilterPanel;

@SuppressWarnings("serial")
public class RoundFilterPanel extends AbstractBiFilterPanel<Season, Game>
{
	public static final String NAME = "Runde";
	
	private JCheckBox first = new JCheckBox("Hinrunde",true);
	private JCheckBox second = new JCheckBox("R�ckrunde",true);

	public RoundFilterPanel()
	{
		this(true,true);
	}

	public RoundFilterPanel(boolean firstRound, boolean secondRound)
	{
		super(false);
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		first.addActionListener(setFilterListener);
		first.setInheritsPopupMenu(true);
		second.addActionListener(setFilterListener);
		second.setInheritsPopupMenu(true);
		
		first.setSelected(firstRound);
		second.setSelected(secondRound);
		
		setFilter();
	}
	
	protected void setFilter()
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
		return "Hin- / R�ckrunde";
	}
}
