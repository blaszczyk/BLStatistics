package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class MultiOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T,U> {

	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String XOR = "XOR";
	
	private List<BiFilterPanel<T,U>> panels = new ArrayList<>();
	private String[] operators = {AND,OR,XOR};
	private JComboBox<String> operatorBox;

	public MultiOperatorFilterPanel(PanelMenu<T,U> panelMenu, List<BiFilterPanel<T, U>> panels, String operator) 
	{
		this.panels = panels;
		operatorBox = new JComboBox<>(operators);
		operatorBox.setAlignmentX(LEFT_ALIGNMENT);
		operatorBox.addActionListener( e -> {
			setOperator();
		});
		operatorBox.setSelectedItem(operator);
		operatorBox.setMaximumSize(new Dimension(50,30));
		for(BiFilterPanel<T, U> panel : panels)
			panel.addFilterListener(this);
		
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JMenu addPanel = new JMenu("Neues Feld");
		panelMenu.addMenuItems(addPanel, e -> {
			addPanel(panelMenu.getPanel());
			notifyListeners();
		});
		addPopupMenuItem(addPanel);
	}

	
	public MultiOperatorFilterPanel(PanelMenu<T,U> panelMenu) 
	{
		this(panelMenu,new ArrayList<>(),AND);
		addPanel( new AbsoluteOperatorFilterPanel<>(true));
	}

	private void addPanel(BiFilterPanel<T,U> panel)
	{
		panel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		panel.addFilterListener(this);
		panels.add(panel);
		setOperator();
	}
	
	private void setOperator()
	{
		switch((String)operatorBox.getSelectedItem())
		{
		case AND:
			setFilter(LogicalBiFilter.getANDBiFilter(panels));
			break;
		case OR:
			setFilter(LogicalBiFilter.getORBiFilter(panels));
			break;
		case XOR:
			break;
		}		
		notifyListeners();
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
	public void filter(BiFilter<T, U> filter)
	{
		notifyListeners();
	}

}
