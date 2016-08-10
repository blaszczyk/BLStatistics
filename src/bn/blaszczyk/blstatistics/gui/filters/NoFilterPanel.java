package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class NoFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>
{
	public static final String NAME = "NoFilter";
	
	public NoFilterPanel()
	{
		setFilter(LogicalBiFilter.getTRUEBiFilter());
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	}

	@Override
	protected void addComponents()
	{
		add(Box.createRigidArea(new Dimension(300,50)));
	}
	
	@Override
	public String toString()
	{
		return "Kein Filter";
	}

	@Override
	protected void setFilter()
	{
	}
}
