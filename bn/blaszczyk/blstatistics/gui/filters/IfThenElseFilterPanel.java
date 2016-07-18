package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class IfThenElseFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>
{
	private BiFilterPanel<T,U> ifFilter;
	private BiFilterPanel<T,U> thenFilter;
	private BiFilterPanel<T,U> elseFilter;
	
	public IfThenElseFilterPanel(PanelMenu<T,U> panelMenu)
	{
		setIfFilter(new AbsoluteOperatorFilterPanel<>(true));
		setThenFilter(new AbsoluteOperatorFilterPanel<>(true));
		setElseFilter(new AbsoluteOperatorFilterPanel<>(false));
		
		JMenu setIf = new JMenu("Setze IF Filter");
		panelMenu.addMenuItems(setIf, e -> setIfFilter(panelMenu.getPanel()));
		addPopupMenuItem(setIf);
		
		JMenu setThen = new JMenu("Setze THEN Filter");
		panelMenu.addMenuItems(setThen, e -> setThenFilter(panelMenu.getPanel()));
		addPopupMenuItem(setThen);
		
		JMenu setElse = new JMenu("Setze ELSE Filter");
		panelMenu.addMenuItems(setElse, e -> setElseFilter(panelMenu.getPanel()));
		addPopupMenuItem(setElse);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setOperator();
	}

	
	private void setIfFilter(BiFilterPanel<T,U> panel)
	{
		if(ifFilter != null)
			ifFilter.removeFilterListener(this);
		ifFilter = panel;
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		setOperator();
	}
	
	private void setThenFilter(BiFilterPanel<T,U> panel)
	{
		if(thenFilter != null)
			thenFilter.removeFilterListener(this);
		thenFilter = panel;
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		setOperator();
	}
	
	private void setElseFilter(BiFilterPanel<T,U> panel)
	{
		if(elseFilter != null)
			elseFilter.removeFilterListener(this);
		elseFilter = panel;
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		setOperator();
	}
	
	private void setOperator()
	{
		setFilter( LogicalBiFilter.getIF_THEN_ELSEBiFilter(ifFilter, thenFilter, elseFilter));
		notifyListeners();
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
	public void filter()
	{
		notifyListeners();
	}
	
}
