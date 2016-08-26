package bn.blaszczyk.fussballstats.gui.corefilters;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.filters.LogicalBiFilterFactory;
import bn.blaszczyk.fussballstats.filters.SeasonFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.AbstractBiFilterPanel;

@SuppressWarnings("serial")
public class RoundFilterPanel extends AbstractBiFilterPanel<Season, Game>
{
	/*
	 * Constants
	 */
	public static final String NAME = "Runde";
	
	/*
	 * Components
	 */
	private final JCheckBox boxFirst = new JCheckBox("Hinrunde",true);
	private final JCheckBox boxSecond = new JCheckBox("Rückrunde",true);

	/*
	 * Constructors
	 */
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
		boxFirst.setOpaque(false);
		boxSecond.addActionListener(setFilterListener);
		boxSecond.setInheritsPopupMenu(true);
		boxSecond.setOpaque(false);
		
		boxFirst.setSelected(firstRound);
		boxSecond.setSelected(secondRound);
		
		setFilter();
	}
	
	/*
	 * Getters
	 */
	public boolean isFirstRound()
	{
		return boxFirst.isSelected();
	}
	
	public boolean isSecondRound()
	{
		return boxSecond.isSelected();
	}
	
	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		if(isFirstRound())
			if(isSecondRound())
				setFilter(LogicalBiFilterFactory.createTRUEBiFilter());
			else
				setFilter(SeasonFilterFactory.createFirstRoundFilter());
		else
			if(isSecondRound())
				setFilter(SeasonFilterFactory.createSecondRoundFilter());
			else
				setFilter(LogicalBiFilterFactory.createFALSEBiFilter());
	}
	
	@Override
	protected void addComponents()
	{
		add(boxFirst);
		add(boxSecond);
	}

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return "Hin- / Rückrunde";
	}
}
