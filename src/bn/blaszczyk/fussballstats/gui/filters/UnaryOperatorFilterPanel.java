package bn.blaszczyk.fussballstats.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	public static final String NAME = "UnaryOperator";

	private BiFilterPanel<T,U> innerPanel;
	private JLabel label = new JLabel("NOT");
	
	public UnaryOperatorFilterPanel(BiFilterPanel<T, U> originalPanel) 
	{
		super(true);
		setInnerPanel(originalPanel);
		label.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public BiFilterPanel<T, U> getInnerPanel()
	{
		return innerPanel;
	}
	
	private void setInnerPanel(BiFilterPanel<T,U> innerPanel)
	{
		if(innerPanel instanceof UnaryOperatorFilterPanel)
		{
			replaceMe(((UnaryOperatorFilterPanel<T, U>)innerPanel).getInnerPanel());
			return;
		}
		if(this.innerPanel != null)
			this.innerPanel.removeFilterListener(this);
		innerPanel.addFilterListener(this);
		innerPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		this.innerPanel = innerPanel;
		setFilter();
	}
	
	protected void setFilter()
	{
		setFilter(LogicalBiFilter.getNOTBiFilter(innerPanel));
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
		passFilterEvent(e);
		if(e.getType() == BiFilterEvent.SET_PANEL && innerPanel.equals(e.getSource()))
			//The method AbstractBiFilterPanel.negate() causes a Panel x to request NOT(x) to replace x by NOT(x), thus we need:
			if(!e.getNewPanel().equals(this))
				setInnerPanel(e.getNewPanel());
	}

	@Override
	public String toString()
	{
		return "NOT " + innerPanel;
	}
}
