package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;

import javax.swing.JMenu;

public interface PanelMenu<T,U> {
	public BiFilterPanel<T, U> getPanel();
	public void addMenuItems(JMenu menu, ActionListener listener);
}
