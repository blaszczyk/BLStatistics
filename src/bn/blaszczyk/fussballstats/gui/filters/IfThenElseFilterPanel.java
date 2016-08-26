package bn.blaszczyk.fussballstats.gui.filters;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.filters.LogicalBiFilterFactory;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	/*
	 * Constants
	 */
	public static final String NAME = "IfThenElse";

	private static final Color ACTIVE_BG = new Color(255,255,220);
	/*
	 * Components
	 */
	private final JLabel lblIf = new JLabel("IF");
	private final JLabel lblThen = new JLabel("THEN");
	private final JLabel lblElse = new JLabel("ELSE");
	
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;

	/*
	 * Constructors
	 */
	public IfThenElseFilterPanel( BiFilterPanel<T,U> ifFilter, BiFilterPanel<T,U> thenFilter, BiFilterPanel<T,U> elseFilter)
	{
		super(true);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		setIfFilter(ifFilter);
		setThenFilter(thenFilter);
		setElseFilter(elseFilter);
		
		setFilter();
	}

	/*
	 * Getters, Setters
	 */
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
	
	private void setIfFilter(BiFilterPanel<T,U> newPanel)
	{
		if(ifFilter != null)
			ifFilter.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		ifFilter = newPanel;
		setFilter();
	}
	private void setThenFilter(BiFilterPanel<T,U> newPanel)
	{
		if(thenFilter != null)
			thenFilter.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		thenFilter = newPanel;
		setFilter();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> newPanel)
	{
		if(elseFilter != null)
			elseFilter.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		elseFilter = newPanel;
		setFilter();
	}
	
	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		setFilter( LogicalBiFilterFactory.createIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
	}

	@Override
	protected void addComponents()
	{
		add(lblIf);
		add(ifFilter.getPanel());
		add(lblThen);
		add(thenFilter.getPanel());
		add(lblElse);
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
	protected Color getActiveBG()
	{
		return ACTIVE_BG;
	}
	
	/*
	 * BiFilterListener Methods
	 */
	@Override
	public void filter(BiFilterEvent<T, U> e)
	{
		notifyListeners(e);
		if(e.getType() == BiFilterEvent.SET_PANEL && e.getSource() != null)
		{
			if(e.getSource().equals(ifFilter))
				setIfFilter(e.getNewPanel());
			if(e.getSource().equals(thenFilter))
				setThenFilter(e.getNewPanel());
			if(e.getSource().equals(elseFilter))
				setElseFilter(e.getNewPanel());
		}
	}
	
	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return "IF_THEN_ELSE";
	}
}
