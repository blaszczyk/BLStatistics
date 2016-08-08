package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public class BlankFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>
{
	public BlankFilterPanel(FilterPanelManager<T,U> filterManager)
	{
		super(filterManager);
		setFilter(LogicalBiFilter.getTRUEBiFilter());
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JMenu setPanel = new JMenu("Setzte Filter");
		filterManager.addMenuItems(setPanel, e -> {
			replaceMe( filterManager.getPanel() );
		});
		addPopupMenuItem(setPanel);
		removePopupMenuItem(popupReplace);
		removePopupMenuItem(popupSetActive);
		removePopupMenuItem(popupNegate);
	}

	@Override
	protected void addComponents()
	{
		add(Box.createRigidArea(new Dimension(300,50)));
	}
	
	@Override
	public String toString()
	{
		return "Blank Filter";
	}
}
