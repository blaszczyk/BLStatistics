package bn.blaszczyk.blstatistics.gui.filters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class MultiOperatorFilterPanel<T,U> extends LogicalBiFilterPanel<T, U> implements Iterable<BiFilterPanel<T,U>> {

	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String XOR = "XOR";
	private static final String[] OPERATORS = {AND,OR,XOR};
	
	private List<BiFilterPanel<T,U>> panels;
	private JComboBox<String> operatorBox;
	
	private JMenu popupRemoveFilter;

	public MultiOperatorFilterPanel(FilterPanelManager<T,U> filterManager, List<BiFilterPanel<T, U>> panels, String operator) 
	{
		super(filterManager);
		this.panels = panels;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(OPERATORS);
		cbf.setBoxWidth(80);
		operatorBox = cbf.createComboBox();
		operatorBox.setAlignmentX(LEFT_ALIGNMENT);
		operatorBox.addActionListener( e -> setFilter() );
		operatorBox.setSelectedItem(operator);
		
		for(BiFilterPanel<T, U> panel : panels)
			panel.addFilterListener(this);		
	}

	public MultiOperatorFilterPanel(FilterPanelManager<T,U> filterManager) 
	{
		this(filterManager,new ArrayList<>(),AND);
		addPanel(new BlankFilterPanel<>(filterManager));
		addPanel(new BlankFilterPanel<>(filterManager));
	}
	
	public String getOperator()
	{
		return (String)operatorBox.getSelectedItem();
	}

	private void addPanel(BiFilterPanel<T,U> panel)
	{
		panels.add(replaceFilterPanel(panel, null) );
		setFilter();
	}
	
	private void replacePanel(int index, BiFilterPanel<T,U> panel)
	{
		if(index < 0 || index >= panels.size())
			return;
		panels.set(index, replaceFilterPanel(panel, panels.get(index)));
		setFilter();
	}
	
	private void removePanel(BiFilterPanel<T,U> panel)
	{
		panels.remove(panel);
		setFilter();
	}
	
	private void setDeleteMenu()
	{
		popupRemoveFilter.removeAll();
		for(BiFilterPanel<T, U> panel : panels)
		{
			JMenuItem remove = new JMenuItem(panel.toString());
			remove.addActionListener( e -> removePanel(panel));
			popupRemoveFilter.add(remove);
		}
	}
	
	private void setFilter()
	{
		setDeleteMenu();
		switch(getOperator())
		{
		case AND:
			setFilter(LogicalBiFilter.getANDBiFilter(panels));
			break;
		case OR:
			setFilter(LogicalBiFilter.getORBiFilter(panels));
			break;
		case XOR:
			setFilter(LogicalBiFilter.getXORBiFilter(panels));
			break;
		}		
	}

	@Override
	protected void addPopupMenuItems()
	{
		JMenu popupAddFilter = new JMenu("Neuer Filter");
		filterManager.addMenuItems(popupAddFilter, e -> addPanel(filterManager.getPanel()));
		addPopupMenuItem(popupAddFilter);
		popupRemoveFilter = new JMenu("Entferne Feld");
		addPopupMenuItem(popupRemoveFilter);
		super.addPopupMenuItems();
	}

	@Override
	protected void addComponents()
	{
		add(operatorBox);
		for(int i = 0; i < panels.size(); i++)
		{
			if(i > 0)
			{
				JLabel label = new JLabel(operatorBox.getSelectedItem().toString());
				label.setAlignmentX(LEFT_ALIGNMENT);
				add(label);
			}
			add(panels.get(i).getPanel());
		}		
	}

	@Override
	public void paint()
	{
		for(BiFilterPanel<T, U> panel : panels)
			panel.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilterEvent<T, U> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL)
		{
			for(int i = 0; i < panels.size(); i++)
				if(panels.get(i).equals(e.getSource()))
					replacePanel(i, e.getPanel());
		}
		else
		{
			setDeleteMenu();
			passFilterEvent(e);
		}
	}
	
	@Override
	public String toString()
	{
		return operatorBox.getSelectedItem().toString() + " " + panels.size() + " Komponenten";
	}


	@Override
	public Iterator<BiFilterPanel<T, U>> iterator()
	{
		return panels.iterator();
	}

}
