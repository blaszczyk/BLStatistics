package bn.blaszczyk.fussballstats.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class AbsoluteOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>{

	public static final String TRUE_NAME = "TRUE";
	public static final String FALSE_NAME = "FALSE";
	private JLabel label = new JLabel();
	
	public AbsoluteOperatorFilterPanel(boolean value) 
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		if(value)
		{
			setFilter(LogicalBiFilter.getTRUEBiFilter());
			label.setText(TRUE_NAME);
		}
		else
		{
			setFilter(LogicalBiFilter.getFALSEBiFilter());
			label.setText(FALSE_NAME);
		}
	}
	

	@Override
	protected void addComponents()
	{
		add(label);
	}
	
	public boolean getValue()
	{
		return label.getText() == TRUE_NAME;
	}

	@Override
	public String toString()
	{
		return label.getText();
	}

	@Override
	protected void setFilter()
	{
	}

}
