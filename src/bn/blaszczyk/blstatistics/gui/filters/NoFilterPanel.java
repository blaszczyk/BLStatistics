package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenu;

import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;
import bn.blaszczyk.blstatistics.tools.NewFilterMenu;

@SuppressWarnings("serial")
public class NoFilterPanel<T,U> extends AbstractBiFilterPanel<T, U>
{
	public NoFilterPanel()
	{
		setFilter(LogicalBiFilter.getTRUEBiFilter());
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JMenu setPanel = new JMenu("Setzte Filter");
		NewFilterMenu.addMenuItems(setPanel, e -> replaceMe( NewFilterMenu.getPanel() ) );
		addPopupMenuItem(setPanel);
		removePopupMenuItem(popupReplace);
		removePopupMenuItem(popupSetActive);
		removePopupMenuItem(popupNegate);
		removePopupMenuItem(popupRemove);
	}

	@Override
	protected void addComponents()
	{
		add(Box.createRigidArea(new Dimension(300,50)));
	}
	
	@Override
	public String toString()
	{
		return "Kein Filter";
	}
}
