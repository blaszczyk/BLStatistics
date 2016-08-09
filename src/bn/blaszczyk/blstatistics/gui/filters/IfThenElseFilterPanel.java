package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.tools.NewFilterMenu;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends LogicalBiFilterPanel<T, U>
{
	private JLabel ifLabel = new JLabel("IF");
	private JLabel thenLabel = new JLabel("THEN");
	private JLabel elseLabel = new JLabel("ELSE");
	
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;
	
	public IfThenElseFilterPanel()
	{
		ifLabel.setAlignmentX(LEFT_ALIGNMENT);
		thenLabel.setAlignmentX(LEFT_ALIGNMENT);
		elseLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		setIfFilter(new NoFilterPanel<T, U>());
		setThenFilter(new NoFilterPanel<T, U>());
		setElseFilter(new NoFilterPanel<T, U>());
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setFilter();
	}

	public IfThenElseFilterPanel( BiFilterPanel<T,U> ifFilter, BiFilterPanel<T,U> thenFilter, BiFilterPanel<T,U> elseFilter)
	{
		this();
		setIfFilter(ifFilter);
		setThenFilter(thenFilter);
		setElseFilter(elseFilter);
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
	
	private void setFilter()
	{
		setFilter( LogicalBiFilter.getIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
	}


	

	@Override
	protected void addPopupMenuItems()
	{
		JMenu setIf = new JMenu("Setze IF Filter");
		NewFilterMenu.addMenuItems(setIf, e -> ifFilter.replaceMe(NewFilterMenu.getPanel()));
		addPopupMenuItem(setIf);
		
		JMenu setThen = new JMenu("Setze THEN Filter");
		NewFilterMenu.addMenuItems(setThen, e -> thenFilter.replaceMe(NewFilterMenu.getPanel()));
		addPopupMenuItem(setThen);
		
		JMenu setElse = new JMenu("Setze ELSE Filter");
		NewFilterMenu.addMenuItems(setElse, e -> elseFilter.replaceMe(NewFilterMenu.getPanel()));
		addPopupMenuItem(setElse);
		super.addPopupMenuItems();
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
		if(e.getType() == BiFilterEvent.RESET_PANEL)
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
