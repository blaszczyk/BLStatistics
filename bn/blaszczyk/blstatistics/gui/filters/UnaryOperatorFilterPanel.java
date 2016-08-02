package bn.blaszczyk.blstatistics.gui.filters;

//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
//import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends LogicalBiFilterPanel<T, U>
{

	private BiFilterPanel<T,U> innerPanel;
	private JLabel label = new JLabel("NOT");
	
	public UnaryOperatorFilterPanel(FilterPanelManager<T,U> filterManager)
	{
		this(filterManager,new BlankFilterPanel<T, U>(filterManager));
	}
	
	public UnaryOperatorFilterPanel(FilterPanelManager<T,U> filterManager, BiFilterPanel<T, U> originalPanel) 
	{
		super(filterManager);
		
		JMenu setPanel = new JMenu("Setze Filter");
		filterManager.addMenuItems(setPanel, e -> {
			setInnerPanel( filterManager.getPanel());
		});
		addPopupMenuItem(setPanel);		
		addPopupMenuItem(setActive);
		
		setInnerPanel(originalPanel);
		label.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	protected void setOperator()
	{
		setFilter(LogicalBiFilter.getNOTBiFilter(innerPanel));
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
		if(innerPanel instanceof UnaryOperatorFilterPanel)
		{
			notifyListeners(new BiFilterEvent<T, U>(this, ((UnaryOperatorFilterPanel<T, U>)innerPanel).getInnerPanel(), BiFilterEvent.RESET_PANEL));
			return;
		}
		this.innerPanel = replaceFilterPanel(innerPanel, this.innerPanel);
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
