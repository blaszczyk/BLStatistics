package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.BiFilterListener;

public interface BiFilterPanel<T,U> extends BiFilter<T,U> {
	
	public void paint();
	public JPanel getPanel();
	
	public void addFilterListener(BiFilterListener<T,U> listener);
	public void removeFilterListener(BiFilterListener<T,U> listener);
	public void notifyListeners();

}
