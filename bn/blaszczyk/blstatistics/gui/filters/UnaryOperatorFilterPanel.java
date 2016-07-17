package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T,U> {

	private BiFilterPanel<T,U> originalPanel;
	private JLabel label = new JLabel("NOT");
	private boolean isNot = true;
	
	public UnaryOperatorFilterPanel(PanelMenu<T,U> panelMenu)
	{
		this(panelMenu,new AbsoluteOperatorFilterPanel<>(true));
	}
	
	public UnaryOperatorFilterPanel(PanelMenu<T,U> panelMenu, BiFilterPanel<T, U> originalPanel) 
	{
		
		JMenuItem toggle = new JMenuItem("Umschalten");
		toggle.addActionListener( e -> toggleFilter());
		addPopupMenuItem(toggle);
		
		JMenu setPanel = new JMenu("Setze Filter");
		panelMenu.addMenuItems(setPanel, e -> {
			setOriginalPanel( panelMenu.getPanel());
			paint();
			notifyListeners();
		});
		addPopupMenuItem(setPanel);		
		
		addMouseListener( new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
					toggleFilter();
			}
		});
		
		setOriginalPanel(originalPanel);
		label.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		paint();
	}
	
	private void toggleFilter()
	{
		isNot = !isNot;
		if(isNot)
		{
			setFilter(LogicalBiFilter.getNOTBiFilter(originalPanel));
			label.setText("NOT");
		}
		else
		{
			setFilter(originalPanel);
			label.setText("ID");
		}
		notifyListeners();
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(originalPanel.getPanel());
	}


	public BiFilterPanel<T,U> getOriginalPanel()
	{
		return originalPanel;
	}


	public void setOriginalPanel(BiFilterPanel<T,U> originalPanel)
	{
		if(this.originalPanel != null)
			this.originalPanel.removeFilterListener(this);
		this.originalPanel = originalPanel;
		originalPanel.addFilterListener(this);
		originalPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		setFilter(LogicalBiFilter.getNOTBiFilter(originalPanel));
	}

	@Override
	public void paint()
	{
		originalPanel.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilter<T, U> filter)
	{
		notifyListeners();
		paint(); //TODO: REMOVE
	}
	
}
