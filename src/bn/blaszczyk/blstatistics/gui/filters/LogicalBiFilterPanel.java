package bn.blaszczyk.blstatistics.gui.filters;


@SuppressWarnings("serial")
public abstract class LogicalBiFilterPanel<T, U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	public LogicalBiFilterPanel()
	{
		super(true);
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
	
	protected void notifyReplacement(BiFilterPanel<T, U> newPanel, BiFilterPanel<T, U> oldPanel)
	{
		passFilterEvent(new BiFilterEvent<>(oldPanel, newPanel, BiFilterEvent.RESET_PANEL));
	}
}
