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
	
	private BiFilterPanel<T,U> ifPanel;
	private BiFilterPanel<T,U> thenPanel;
	private BiFilterPanel<T,U> elsePanel;

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
		return ifPanel;
	}


	public BiFilterPanel<T, U> getThenFilter()
	{
		return thenPanel;
	}


	public BiFilterPanel<T, U> getElseFilter()
	{
		return elsePanel;
	}
	
	public void replaceIfPanel(final BiFilterPanel<T, U> ifPanel)
	{
		this.ifPanel.replaceMe(ifPanel);
	}
	
	public void replaceThenPanel(final BiFilterPanel<T, U> thenPanel)
	{
		this.thenPanel.replaceMe(thenPanel);
	}
	
	public void replaceElsePanel(final BiFilterPanel<T, U> elsePanel)
	{
		this.elsePanel.replaceMe(elsePanel);
	}
	
	private void setIfFilter(BiFilterPanel<T,U> newPanel)
	{
		if(ifPanel != null)
			ifPanel.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		ifPanel = newPanel;
		setFilter();
	}
	private void setThenFilter(BiFilterPanel<T,U> newPanel)
	{
		if(thenPanel != null)
			thenPanel.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		thenPanel = newPanel;
		setFilter();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> newPanel)
	{
		if(elsePanel != null)
			elsePanel.removeFilterListener(this);
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		elsePanel = newPanel;
		setFilter();
	}
	
	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		setFilter( LogicalBiFilterFactory.createIF_THEN_ELSEBiFilter(ifPanel, thenPanel, elsePanel));
	}

	@Override
	protected void addComponents()
	{
		add(lblIf);
		add(ifPanel.getPanel());
		add(lblThen);
		add(thenPanel.getPanel());
		add(lblElse);
		add(elsePanel.getPanel());
	}

	@Override
	public void paint()
	{
		ifPanel.paint();
		thenPanel.paint();
		elsePanel.paint();
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
			if(e.getSource().equals(ifPanel))
				setIfFilter(e.getNewPanel());
			if(e.getSource().equals(thenPanel))
				setThenFilter(e.getNewPanel());
			if(e.getSource().equals(elsePanel))
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
