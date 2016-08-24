package bn.blaszczyk.fussballstats.gui.filters;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilterFactory;

@SuppressWarnings("serial")
public class NoFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>
{
	/*
	 * Constants
	 */
	public static final String NAME = "NoFilter";
	
	/*
	 * Constructor
	 */
	public NoFilterPanel()
	{
		super(false);
		setFilter(LogicalBiFilterFactory.createTRUEBiFilter());
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	}

	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void addComponents()
	{
		add(Box.createRigidArea(new Dimension(350,50)));
	}
	

	@Override
	protected void setFilter()
	{
	}

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return "Kein Filter";
	}
}
