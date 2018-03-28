package bn.blaszczyk.fussballstats.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilterFactory;

@SuppressWarnings("serial")
public class AbsoluteOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>{

	/*
	 * Constants
	 */
	public static final String TRUE_NAME = "TRUE";
	public static final String FALSE_NAME = "FALSE";
	/*
	 * Components
	 */
	private final JLabel label = new JLabel();
	
	/*
	 * Constructor
	 */
	public AbsoluteOperatorFilterPanel(boolean value) 
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		if(value)
		{
			setFilter(LogicalBiFilterFactory.createTRUEBiFilter());
			label.setText(TRUE_NAME);
		}
		else
		{
			setFilter(LogicalBiFilterFactory.createFALSEBiFilter());
			label.setText(FALSE_NAME);
		}
	}
	
	public AbsoluteOperatorFilterPanel()
	{
		this(true);
	}

	/*
	 * Getter
	 */
	public boolean getValue()
	{
		return label.getText() == TRUE_NAME;
	}

	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
	}
	
	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return label.getText();
	}

}
