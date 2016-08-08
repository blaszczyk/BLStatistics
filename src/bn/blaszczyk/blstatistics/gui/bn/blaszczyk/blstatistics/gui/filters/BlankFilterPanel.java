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
		super(LogicalBiFilter.getTRUEBiFilter(),filterManager);
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JMenu setPanel = new JMenu("Setzte Filter");
		filterManager.addMenuItems(setPanel, e -> {
			notifyListeners(new BiFilterEvent<T, U>(this,filterManager.getPanel(),BiFilterEvent.RESET_PANEL));
		});
		addPopupMenuItem(setPanel);
		removePopupMenuItem(replace);
		removePopupMenuItem(setActive);
		removePopupMenuItem(negate);
	}

	@Override
	protected void addComponents()
	{
		add(Box.createRigidArea(new Dimension(100,30)));
	}
	
	@Override
	public String toString()
	{
		return "Blank Filter";
	}
}
