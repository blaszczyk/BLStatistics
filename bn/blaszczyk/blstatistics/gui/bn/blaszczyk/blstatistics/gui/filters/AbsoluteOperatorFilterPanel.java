package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class AbsoluteOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>{

	private JLabel label = new JLabel("TRUE");
	private boolean value = true;
	
	public AbsoluteOperatorFilterPanel(boolean value, FilterPanelManager<T,U> filterManager) 
	{
		super(filterManager);
		
		JMenuItem popupToggle = new JMenuItem("Umschalten");
		popupToggle.addActionListener( e -> toggleFilter());
		addPopupMenuItem(popupToggle);

		addMouseListener( new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
					toggleFilter();
			}
		});
		
		if(value)
			this.value = false;
		toggleFilter();
		
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}
	
	private void toggleFilter()
	{
		value = !value;
		if(value)
		{
			setFilter(LogicalBiFilter.getTRUEBiFilter());
			label.setText("TRUE");
		}
		else
		{
			setFilter(LogicalBiFilter.getFALSEBiFilter());
			label.setText("FALSE");
		}
	}

	@Override
	protected void addComponents()
	{
		add(label);
	}

	@Override
	public String toString()
	{
		return label.getText();
	}

}
