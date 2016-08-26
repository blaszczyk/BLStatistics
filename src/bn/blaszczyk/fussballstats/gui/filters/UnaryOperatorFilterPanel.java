package bn.blaszczyk.fussballstats.gui.filters;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilterFactory;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	/*
	 * Constants
	 */
	public static final String NAME = "UnaryOperator";

	public static final Color ACTIVE_BG = new Color(255,200,200);

	/*
	 * Components
	 */
	private final JLabel label = new JLabel("NOT");
	private BiFilterPanel<T,U> innerPanel;
	
	/*
	 * Constructors
	 */
	public UnaryOperatorFilterPanel(BiFilterPanel<T, U> originalPanel) 
	{
		super(true);
		setInnerPanel(originalPanel);
		label.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/*
	 * Getters
	 */
	public BiFilterPanel<T, U> getInnerPanel()
	{
		return innerPanel;
	}
	
	/*
	 * Internal Methods
	 */
	private void setInnerPanel(BiFilterPanel<T,U> innerPanel)
	{
		if(this.innerPanel != null)
			this.innerPanel.removeFilterListener(this);
		if(innerPanel instanceof UnaryOperatorFilterPanel)
		{
			replaceMe(((UnaryOperatorFilterPanel<T, U>)innerPanel).getInnerPanel());
			return;
		}
		innerPanel.addFilterListener(this);
		innerPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		this.innerPanel = innerPanel;
		setFilter();
	}
	
	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		setFilter(LogicalBiFilterFactory.createNOTBiFilter(innerPanel));
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(innerPanel.getPanel());
	}

	@Override
	public void paint()
	{
		innerPanel.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilterEvent<T,U> e)
	{
		notifyListeners(e);
		if(e.getType() == BiFilterEvent.SET_PANEL && innerPanel.equals(e.getSource()))
			//The method AbstractBiFilterPanel.negate() causes a Panel x to request NOT(x) to replace x by NOT(x), thus we need:
			if(!e.getNewPanel().equals(this))
				setInnerPanel(e.getNewPanel());
	}

	@Override
	protected Color getActiveBG()
	{
		return ACTIVE_BG;
	}
	
	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return "NOT " + innerPanel;
	}
}
