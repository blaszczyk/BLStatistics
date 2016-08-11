package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.BiFilter;

public interface BiFilterPanel<T,U> extends BiFilter<T,U> {
	
	public void paint();
	public JPanel getPanel();
	
	public void addFilterListener(BiFilterListener<T,U> listener);
	public void removeFilterListener(BiFilterListener<T,U> listener);

	public void addPopupMenuItem(JMenuItem item);
	public void addPopupMenuLabel(JLabel item);
	public void addPopupMenuSeparator();
	
	public void replaceMe(BiFilterPanel<T,U> panel);
	
	public void setActive(boolean active);
	public boolean isActive();
	

}