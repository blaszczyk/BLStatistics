package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends LogicalBiFilterPanel<T, U>
{
	public static final String NAME = "IfThenElse";
	
	private JLabel ifLabel = new JLabel("IF");
	private JLabel thenLabel = new JLabel("THEN");
	private JLabel elseLabel = new JLabel("ELSE");
	
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;

	public IfThenElseFilterPanel( BiFilterPanel<T,U> ifFilter, BiFilterPanel<T,U> thenFilter, BiFilterPanel<T,U> elseFilter)
	{
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		ifLabel.setAlignmentX(LEFT_ALIGNMENT);
		thenLabel.setAlignmentX(LEFT_ALIGNMENT);
		elseLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		setIfFilter(ifFilter);
		setThenFilter(thenFilter);
		setElseFilter(elseFilter);
		
		setFilter();
	}

	public BiFilterPanel<T, U> getIfFilter()
	{
		return ifFilter;
	}


	public BiFilterPanel<T, U> getThenFilter()
	{
		return thenFilter;
	}


	public BiFilterPanel<T, U> getElseFilter()
	{
		return elseFilter;
	}
	
	private void setIfFilter(BiFilterPanel<T,U> panel)
	{
		ifFilter = replaceFilterPanel(panel, ifFilter);
		setFilter();
	}
	private void setThenFilter(BiFilterPanel<T,U> panel)
	{
		thenFilter = replaceFilterPanel(panel, thenFilter);
		setFilter();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> panel)
	{
		elseFilter = replaceFilterPanel(panel, elseFilter);
		setFilter();
	}
	
	protected void setFilter()
	{
		setFilter( LogicalBiFilter.getIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
	}

	@Override
	protected void addComponents()
	{
		add(ifLabel);
		add(ifFilter.getPanel());
		add(thenLabel);
		add(thenFilter.getPanel());
		add(elseLabel);
		add(elseFilter.getPanel());
	}

	@Override
	public void paint()
	{
		ifFilter.paint();
		thenFilter.paint();
		elseFilter.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilterEvent<T, U> e)
	{
		passFilterEvent(e);
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource() != null)
		{
			if(e.getSource().equals(ifFilter))
				setIfFilter(e.getPanel());
			if(e.getSource().equals(thenFilter))
				setThenFilter(e.getPanel());
			if(e.getSource().equals(elseFilter))
				setElseFilter(e.getPanel());
		}
	}
	
	@Override
	public String toString()
	{
		return "IF_THEN_ELSE";
	}
}