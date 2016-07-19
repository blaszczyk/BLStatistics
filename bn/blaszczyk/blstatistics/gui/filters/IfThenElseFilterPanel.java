package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;
	
	public IfThenElseFilterPanel(PanelMenu<T,U> panelMenu)
	{
		super(panelMenu);
		setIfFilter(new BlankFilterPanel<T, U>(panelMenu));
		setThenFilter(new BlankFilterPanel<T, U>(panelMenu));
		setElseFilter(new BlankFilterPanel<T, U>(panelMenu));
		
		JMenu setIf = new JMenu("Setze IF Filter");
		panelMenu.addMenuItems(setIf, e -> setIfFilter(panelMenu.getPanel()));
		addPopupMenuItem(setIf);
		
		JMenu setThen = new JMenu("Setze THEN Filter");
		panelMenu.addMenuItems(setThen, e -> setThenFilter(panelMenu.getPanel()));
		addPopupMenuItem(setThen);
		
		JMenu setElse = new JMenu("Setze ELSE Filter");
		panelMenu.addMenuItems(setElse, e -> setElseFilter(panelMenu.getPanel()));
		addPopupMenuItem(setElse);

		addPopupMenuItem(setActive);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setOperator();
	}

	
	private void setIfFilter(BiFilterPanel<T,U> panel)
	{
		ifFilter = panel;
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		setOperator();
	}
	
	private void setThenFilter(BiFilterPanel<T,U> panel)
	{
		thenFilter = panel;
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		setOperator();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> panel)
	{
		elseFilter = panel;
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		setOperator();
	}
	
	private void setOperator()
	{
		setFilter( LogicalBiFilter.getIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
		notifyListeners(new BiFilterEvent<T, U>(this,getFilter(),BiFilterEvent.RESET_FILTER));
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
