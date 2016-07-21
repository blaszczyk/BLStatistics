package bn.blaszczyk.blstatistics.gui.filters;

//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
//import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T,U> {

	private BiFilterPanel<T,U> innerPanel;
	private JLabel label = new JLabel("NOT");
//	private boolean isNot = true;
	
	public UnaryOperatorFilterPanel(FilterPanelManager<T,U> filterManager)
	{
		this(filterManager,new BlankFilterPanel<T, U>(filterManager));
	}
	
	public UnaryOperatorFilterPanel(FilterPanelManager<T,U> filterManager, BiFilterPanel<T, U> originalPanel) 
	{
		super(filterManager);
//		JMenuItem toggle = new JMenuItem("Umschalten");
//		toggle.addActionListener( e -> toggleFilter());
//		addPopupMenuItem(toggle);
		
		JMenu setPanel = new JMenu("Setze Filter");
		filterManager.addMenuItems(setPanel, e -> {
			setInnerPanel( filterManager.getPanel());
		});
		addPopupMenuItem(setPanel);		
		addPopupMenuItem(setActive);
		
//		addMouseListener( new MouseAdapter(){
//			@Override
//			public void mouseClicked(MouseEvent e)
//			{
//				if(e.getClickCount() == 2)
//					toggleFilter();
//			}
//		});
		
		setInnerPanel(originalPanel);
		label.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
//	private void toggleFilter()
//	{
//		isNot = !isNot;
//		setOperator();
//	}
	
	private void setOperator()
	{
//		if(isNot)
//		{
			setFilter(LogicalBiFilter.getNOTBiFilter(innerPanel));
//			label.setText("NOT");
//		}
//		else
//		{
//			setFilter(innerPanel);
//			label.setText("ID");
//		}
		notifyListeners(new BiFilterEvent<T, U>(this,getFilter(),BiFilterEvent.RESET_FILTER));
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(innerPanel.getPanel());
	}



	private void setInnerPanel(BiFilterPanel<T,U> innerPanel)
	{
		if(this.innerPanel != null)
			this.innerPanel.removeFilterListener(this);
		if(innerPanel instanceof UnaryOperatorFilterPanel)
		{
//			System.out.println("Replace " + this.innerPanel + " by "+innerPanel + " in " + this);
			notifyListeners(new BiFilterEvent<T, U>(this, ((UnaryOperatorFilterPanel<T, U>)innerPanel).getInnerPanel(), BiFilterEvent.RESET_PANEL));
			return;
		}
		this.innerPanel = innerPanel;
		innerPanel.addFilterListener(this);
		innerPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		setOperator();
	}
	
	public BiFilterPanel<T, U> getInnerPanel()
	{
		return innerPanel;
	}

	
	@Override
	public void paint()
	{
		innerPanel.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilterEvent<T,U> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource().equals(innerPanel))
		{
			//The method AbstractBiFilterPanel.negate() causes a Panel x to request NOT(x) to replace x by NOT(x), thus we need:
			if(!e.getPanel().equals(this))
				setInnerPanel(e.getPanel());
		}
		else
			notifyListeners(e);
	}

	@Override
	public String toString()
	{
		return "NOT" + (hashCode()%10) + " " + innerPanel;
	}
}
