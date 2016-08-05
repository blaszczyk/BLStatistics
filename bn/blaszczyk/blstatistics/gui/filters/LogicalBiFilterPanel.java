package bn.blaszczyk.blstatistics.gui.filters;

import bn.blaszczyk.blstatistics.filters.BiFilter;

@SuppressWarnings("serial")
public abstract class LogicalBiFilterPanel<T, U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{

	public LogicalBiFilterPanel(FilterPanelManager<T, U> filterManager)
	{
		super(filterManager);
	}

	public LogicalBiFilterPanel(BiFilter<T, U> filter, FilterPanelManager<T, U> filterManager)
	{
		super(filter, filterManager);
	}
	
	protected BiFilterPanel<T, U> replaceFilterPanel(BiFilterPanel<T, U> newPanel, BiFilterPanel<T, U> oldPanel)
	{
		if(newPanel == null)
			return oldPanel;
		if(oldPanel != null)
			oldPanel.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		return newPanel;
	}


}
