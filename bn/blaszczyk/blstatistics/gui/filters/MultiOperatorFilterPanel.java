package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class MultiOperatorFilterPanel<T,U> extends LogicalBiFilterPanel<T, U> implements Iterable<BiFilterPanel<T,U>> {

	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String XOR = "XOR";
	
	private List<BiFilterPanel<T,U>> panels;
	private String[] operators = {AND,OR,XOR};
	private JComboBox<String> operatorBox;
	private JPanel top = new JPanel();
	
	private JMenu removeMenu;

	public MultiOperatorFilterPanel(FilterPanelManager<T,U> filterManager, List<BiFilterPanel<T, U>> panels, String operator) 
	{
		super(filterManager);
		this.panels = panels;
		
		operatorBox = new JComboBox<>(operators);
		operatorBox.setAlignmentX(LEFT_ALIGNMENT);
		operatorBox.addActionListener( e -> setFilter() );
		operatorBox.setSelectedItem(operator);
		operatorBox.setMaximumSize(new Dimension(50,30));
		operatorBox.setInheritsPopupMenu(true);
		

		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(operatorBox);
		top.add(Box.createRigidArea(new Dimension(150,30)));
		top.setInheritsPopupMenu(true);
		top.setAlignmentX(LEFT_ALIGNMENT);
		
		for(BiFilterPanel<T, U> panel : panels)
			panel.addFilterListener(this);
		
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
	}

	public MultiOperatorFilterPanel(FilterPanelManager<T,U> filterManager) 
	{
		this(filterManager,new ArrayList<>(),AND);
		addPanel(new BlankFilterPanel<>(filterManager));
		addPanel(new BlankFilterPanel<>(filterManager));
	}

	private void addPanel(BiFilterPanel<T,U> panel)
	{
		panels.add(replaceFilterPanel(panel, null) );
		resetDeleteMenu();
		setFilter();
	}
	
	private void replacePanel(int index, BiFilterPanel<T,U> panel)
	{
		if(index < 0 || index >= panels.size())
			return;
		
		panels.set(index, replaceFilterPanel(panel, panels.get(index)));
		resetDeleteMenu();
		setFilter();
	}
	
	private void removePanel(BiFilterPanel<T,U> panel)
	{
		panels.remove(panel);
		resetDeleteMenu();
		setFilter();
	}
	
	private void resetDeleteMenu()
	{
		removeMenu.removeAll();
		for(BiFilterPanel<T, U> panel : panels)
		{
			JMenuItem remove = new JMenuItem(panel.toString());
			remove.addActionListener( e -> removePanel(panel));
			removeMenu.add(remove);
		}
	}
	
	public String getOperator()
	{
		return (String)operatorBox.getSelectedItem();
	}
	
	private void setFilter()
	{
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
		JMenu addMenu = new JMenu("Neues Feld");
		filterManager.addMenuItems(addMenu, e -> addPanel(filterManager.getPanel()));
		addPopupMenuItem(addMenu);
		removeMenu = new JMenu("Entferne Feld");
		addPopupMenuItem(removeMenu);
		super.addPopupMenuItems();
	}

	@Override
	protected void addComponents()
	{
		add(top);
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
			resetDeleteMenu();
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
