package bn.blaszczyk.fussballstats.gui.filters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


import bn.blaszczyk.fussballstats.filters.LogicalBiFilterFactory;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class MultiOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T, U>, Iterable<BiFilterPanel<T,U>> 
{
	/*
	 * Constants
	 */
	public static final String NAME = "MultiOperator";
	
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String XOR = "XOR";
	private static final String[] OPERATORS = {AND,OR,XOR};
	
	/*
	 * Components
	 */
	private JComboBox<String> boxOperator;
	private List<BiFilterPanel<T,U>> panels = new ArrayList<>();
	
	/*
	 * Menu
	 */
	private JMenu menuRemoveFilter = new JMenu("Filter Entfernen");

	/*
	 * Constructor
	 */
	public MultiOperatorFilterPanel(Iterable<BiFilterPanel<T, U>> panels, String operator) 
	{
		super(true);
		for(BiFilterPanel<T, U> panel : panels)
			this.panels.add(panel);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	
		boxOperator = new MyComboBox<>(OPERATORS,80,false);
		boxOperator.setAlignmentX(LEFT_ALIGNMENT);
		boxOperator.addActionListener(setFilterListener);
		boxOperator.setSelectedItem(operator);
		
		for(BiFilterPanel<T, U> panel : panels)
			panel.addFilterListener(this);		
	}
	
	/*
	 * Getters, 
	 */
	public String getOperator()
	{
		return (String)boxOperator.getSelectedItem();
	}

	public JMenu getMenuRemoveFilter()
	{
		return menuRemoveFilter;
	}

	public void addPanel(BiFilterPanel<T,U> newPanel)
	{
		newPanel.addFilterListener(this);
		newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panels.add(newPanel);
		notifyListeners(new BiFilterEvent<>(null, newPanel, BiFilterEvent.SET_PANEL));
		setFilter();
	}
	
	/*
	 * Internal Methods
	 */
	private void replacePanel(int index, BiFilterPanel<T,U> newPanel)
	{
		if(index < 0 || index >= panels.size())
			return;
		BiFilterPanel<T, U> oldPanel = panels.get(index) ;
		oldPanel.removeFilterListener(this);
		if( newPanel == null || newPanel instanceof NoFilterPanel )
			panels.remove(oldPanel);
		else
		{
			newPanel.addFilterListener(this);
			newPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
			panels.set(index,newPanel);
		}
		setFilter();
	}
	
	private void setDeleteMenu()
	{
		menuRemoveFilter.removeAll();
		for(BiFilterPanel<T, U> panel : panels)
		{
			JMenuItem remove = new JMenuItem(panel.toString());
			remove.addActionListener( e -> panel.replaceMe(null) );
			menuRemoveFilter.add(remove);
		}
	}
	
	/*
	 * AbstractBiFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		setDeleteMenu();
		switch(getOperator())
		{
		case AND:
			setFilter(LogicalBiFilterFactory.createANDBiFilter(panels));
			break;
		case OR:
			setFilter(LogicalBiFilterFactory.createORBiFilter(panels));
			break;
		case XOR:
			setFilter(LogicalBiFilterFactory.createXORBiFilter(panels));
			break;
		}		
	}
	
	@Override
	protected void addComponents()
	{
		add(boxOperator);
		for(int i = 0; i < panels.size(); i++)
		{
			if(i > 0)
			{
				JLabel label = new JLabel(boxOperator.getSelectedItem().toString() + "     ");
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
		notifyListeners(e);
		if(e.getType() == BiFilterEvent.SET_PANEL)
			for(int i = 0; i < panels.size(); i++)
				if(panels.get(i).equals(e.getSource()))
					replacePanel(i, e.getNewPanel());
		else
			setDeleteMenu();
	}
	
	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return boxOperator.getSelectedItem().toString() + ": " + panels.size() + " Filter";
	}

	/*
	 * Iterable Method
	 */
	@Override
	public Iterator<BiFilterPanel<T, U>> iterator()
	{
		return panels.iterator();
	}



}
