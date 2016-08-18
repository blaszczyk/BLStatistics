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
	
	private JCheckBox boxFirst = new JCheckBox("Hinrunde",true);
	private JCheckBox boxSecond = new JCheckBox("Rückrunde",true);

	public RoundFilterPanel()
	{
		this(true,true);
	}

	public RoundFilterPanel(boolean firstRound, boolean secondRound)
	{
		super(false);
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		boxFirst.addActionListener(setFilterListener);
		boxFirst.setInheritsPopupMenu(true);
		boxSecond.addActionListener(setFilterListener);
		boxSecond.setInheritsPopupMenu(true);
		
		boxFirst.setSelected(firstRound);
		boxSecond.setSelected(secondRound);
		
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
		return boxFirst.isSelected();
	}
	
	public boolean isSecondRound()
	{
		return boxSecond.isSelected();
	}
	
	@Override
	protected void addComponents()
	{
		add(boxFirst);
		add(boxSecond);
	}
	
	@Override
	public String toString()
	{
		return "Hin- / Rückrunde";
	}
}
