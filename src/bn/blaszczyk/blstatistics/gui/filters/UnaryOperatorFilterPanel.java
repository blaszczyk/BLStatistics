package bn.blaszczyk.blstatistics.gui.filters;

//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
//import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.tools.NewFilterMenu;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends LogicalBiFilterPanel<T, U>
{

	private BiFilterPanel<T,U> innerPanel;
	private JLabel label = new JLabel("NOT");
	
	public UnaryOperatorFilterPanel()
	{
		this(new NoFilterPanel<T, U>());
	}
	
	public UnaryOperatorFilterPanel(BiFilterPanel<T, U> originalPanel) 
	{
		setInnerPanel(originalPanel);
		label.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public BiFilterPanel<T, U> getInnerPanel()
	{
		return innerPanel;
	}
	
	private void setInnerPanel(BiFilterPanel<T,U> innerPanel)
	{
		if(innerPanel instanceof UnaryOperatorFilterPanel)
		{
			replaceMe(((UnaryOperatorFilterPanel<T, U>)innerPanel).getInnerPanel());
			return;
		}
		this.innerPanel = replaceFilterPanel(innerPanel, this.innerPanel);
		setFilter();
	}	
	
	private void setFilter()
	{
		setFilter(LogicalBiFilter.getNOTBiFilter(innerPanel));
	}

	@Override
	protected void addPopupMenuItems()
	{
		JMenu popupSetPanel = new JMenu("Setze Inneren Filter");
		NewFilterMenu.addMenuItems(popupSetPanel, e -> innerPanel.replaceMe( NewFilterMenu.getPanel() ));
		addPopupMenuItem(popupSetPanel);	
		super.addPopupMenuItems();
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(innerPanel.getPanel());
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
		passFilterEvent(e);
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource().equals(innerPanel))
			//The method AbstractBiFilterPanel.negate() causes a Panel x to request NOT(x) to replace x by NOT(x), thus we need:
			if(!e.getPanel().equals(this))
				setInnerPanel(e.getPanel());
	}

	@Override
	public String toString()
	{
		return "NOT " + innerPanel;
	}
}
