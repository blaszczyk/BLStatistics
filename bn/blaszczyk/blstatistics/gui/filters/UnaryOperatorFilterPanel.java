package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class UnaryOperatorFilterPanel<T,U> extends AbstractBiFilterPanel<T, U> implements BiFilterListener<T,U> {

	private BiFilterPanel<T,U> originalPanel;
	private JLabel label = new JLabel("NOT");
	private boolean isNot = true;
	
	public UnaryOperatorFilterPanel(PanelMenu<T,U> panelMenu)
	{
		this(panelMenu,new BlankFilterPanel<T, U>(panelMenu));
	}
	
	public UnaryOperatorFilterPanel(PanelMenu<T,U> panelMenu, BiFilterPanel<T, U> originalPanel) 
	{
		super(panelMenu);
		JMenuItem toggle = new JMenuItem("Umschalten");
		toggle.addActionListener( e -> toggleFilter());
		addPopupMenuItem(toggle);
		
		JMenu setPanel = new JMenu("Setze Filter");
		panelMenu.addMenuItems(setPanel, e -> {
			setOriginalPanel( panelMenu.getPanel());
		});
		addPopupMenuItem(setPanel);		
		addPopupMenuItem(setActive);
		
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
	}
	
	private void toggleFilter()
	{
		isNot = !isNot;
		setOperator();
	}
	
	private void setOperator()
	{
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
		notifyListeners(new BiFilterEvent<T, U>(this,getFilter(),BiFilterEvent.RESET_FILTER));
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(originalPanel.getPanel());
	}



	private void setOriginalPanel(BiFilterPanel<T,U> originalPanel)
	{
		this.originalPanel = originalPanel;
		originalPanel.addFilterListener(this);
		originalPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		setOperator();
	}

	@Override
	public void paint()
	{
		originalPanel.paint();
		super.paint();
	}

	@Override
	public void filter(BiFilterEvent<T,U> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource().equals(originalPanel))
			setOriginalPanel(e.getPanel());
		else
			notifyListeners(e);
	}

	@Override
	public String toString()
	{
		return label.getText() + " " + originalPanel;
	}
}
