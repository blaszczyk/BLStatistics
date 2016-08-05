package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends LogicalBiFilterPanel<T, U>
{
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;
	
	public IfThenElseFilterPanel(FilterPanelManager<T,U> filterManager)
	{
		super(filterManager);
		setIfFilter(new BlankFilterPanel<T, U>(filterManager));
		setThenFilter(new BlankFilterPanel<T, U>(filterManager));
		setElseFilter(new BlankFilterPanel<T, U>(filterManager));
		
		JMenu setIf = new JMenu("Setze IF Filter");
		filterManager.addMenuItems(setIf, e -> setIfFilter(filterManager.getPanel()));
		addPopupMenuItem(setIf);
		
		JMenu setThen = new JMenu("Setze THEN Filter");
		filterManager.addMenuItems(setThen, e -> setThenFilter(filterManager.getPanel()));
		addPopupMenuItem(setThen);
		
		JMenu setElse = new JMenu("Setze ELSE Filter");
		filterManager.addMenuItems(setElse, e -> setElseFilter(filterManager.getPanel()));
		addPopupMenuItem(setElse);

		addPopupMenuItem(setActive);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setOperator();
	}

	public IfThenElseFilterPanel(FilterPanelManager<T,U> filterManager, BiFilterPanel<T,U> ifFilter, BiFilterPanel<T,U> thenFilter, BiFilterPanel<T,U> elseFilter)
	{
		this(filterManager);
		setIfFilter(ifFilter);
		setThenFilter(thenFilter);
		setElseFilter(elseFilter);
	}
	
	private void setIfFilter(BiFilterPanel<T,U> panel)
	{
		ifFilter = replaceFilterPanel(panel, ifFilter);
		setOperator();
	}
	
	private void setThenFilter(BiFilterPanel<T,U> panel)
	{
		thenFilter = replaceFilterPanel(panel, thenFilter);
		setOperator();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> panel)
	{
		elseFilter = replaceFilterPanel(panel, elseFilter);
		setOperator();
	}
	
	private void setOperator()
	{
		setFilter( LogicalBiFilter.getIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
		notifyListeners(new BiFilterEvent<T, U>(this,getFilter(),BiFilterEvent.RESET_FILTER));
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


	@Override
	protected void addComponents()
	{
		JLabel label = new JLabel("IF");
		label.setAlignmentX(LEFT_ALIGNMENT);
		add(label);
		add(ifFilter.getPanel());
		label = new JLabel("THEN");
		label.setAlignmentX(LEFT_ALIGNMENT);
		add(label);
		add(thenFilter.getPanel());
		label = new JLabel("ELSE");
		label.setAlignmentX(LEFT_ALIGNMENT);
		add(label);
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
		if(e.getType() == BiFilterEvent.RESET_PANEL)
		{
			if(e.getSource().equals(ifFilter))
				setIfFilter(e.getPanel());
			if(e.getSource().equals(thenFilter))
				setThenFilter(e.getPanel());
			if(e.getSource().equals(elseFilter))
				setElseFilter(e.getPanel());
		}
		else
			notifyListeners(e);
	}
	
	@Override
	public String toString()
	{
		return "IF_THEN_ELSE";
	}
}