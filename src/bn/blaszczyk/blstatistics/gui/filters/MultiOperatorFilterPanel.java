package bn.blaszczyk.blstatistics.gui.filters;

import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class MultiOperatorFilterPanel<T,U> extends LogicalBiFilterPanel<T, U> implements Iterable<BiFilterPanel<T,U>> {

	public static final String NAME = "MultiOperator";
	
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String XOR = "XOR";
	private static final String[] OPERATORS = {AND,OR,XOR};
	
	private List<BiFilterPanel<T,U>> panels;
	private JComboBox<String> operatorBox;
	
	private JMenu miRemoveFilter = new JMenu("Entferne Filter");

	public MultiOperatorFilterPanel(List<BiFilterPanel<T, U>> panels, String operator) 
	{
		this.panels = panels;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	
		operatorBox = new MyComboBox<>(OPERATORS,80,false);
		operatorBox.setAlignmentX(LEFT_ALIGNMENT);
		operatorBox.addActionListener(setFilterListener);
		operatorBox.setSelectedItem(operator);
		
		for(BiFilterPanel<T, U> panel : panels)
			panel.addFilterListener(this);		
	}
	
	public String getOperator()
	{
		return (String)operatorBox.getSelectedItem();
	}

	public void addPanel(BiFilterPanel<T,U> panel)
	{
		panels.add(replaceFilterPanel(panel, null) );
		notifyReplacement(panel, null);
		setFilter();
	}
	
	private void replacePanel(int index, BiFilterPanel<T,U> newPanel)
	{
		if(index < 0 || index >= panels.size())
			return;
		BiFilterPanel<T, U> oldPanel = panels.get(index) ;
		if(newPanel instanceof NoFilterPanel)
			removePanel( oldPanel );
		else
		{
			notifyReplacement(newPanel, oldPanel);
			panels.set(index, replaceFilterPanel(newPanel, oldPanel));
			setFilter();
		}
	}
	
	private void removePanel(BiFilterPanel<T,U> panel)
	{
		if(panel != null)
			panel.removeFilterListener(this);
		notifyReplacement(null, panel);
		panels.remove(panel);
		setFilter();
	}
	
	private void setDeleteMenu()
	{
		miRemoveFilter.removeAll();
		for(BiFilterPanel<T, U> panel : panels)
		{
			JMenuItem remove = new JMenuItem(panel.toString());
			remove.addActionListener( e -> removePanel(panel));
			miRemoveFilter.add(remove);
		}
	}
	
	protected void setFilter()
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

	public JMenu getMiRemoveFilter()
	{
		return miRemoveFilter;
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
			passFilterEvent(e);
			setDeleteMenu();
		}
	}
	
	@Override
	public String toString()
	{
		return operatorBox.getSelectedItem().toString() + ": " + panels.size() + " Filter";
	}


	@Override
	public Iterator<BiFilterPanel<T, U>> iterator()
	{
		return panels.iterator();
	}



}
