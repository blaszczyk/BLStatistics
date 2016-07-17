package bn.blaszczyk.blstatistics.gui.filters;


import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.filters.FilterAdapter;

public class FilterPanelAdapter {

	public static <T,U> BiFilterPanel<T, U> getFirstArgAdapter(FilterPanel<T> panel)
	{
		return new FirstArgAdapter<T,U>(panel);
	}

	public static <T,U> BiFilterPanel<T, U> getSecondArgAdapter(FilterPanel<U> panel)
	{
		return new SecondArgAdapter<T,U>(panel);
	}
	
	public static class FirstArgAdapter<T, U> implements BiFilterPanel<T, U>
	{
		
		private FilterPanel<T> panel;
		
		public FirstArgAdapter(FilterPanel<T> panel)
		{
			this.panel = panel;
		}

		@Override
		public void paint()
		{
			panel.paint();
		}

		@Override
		public boolean check(T t, U u)
		{
			return panel.check(t);
		}

		@Override
		public JPanel getPanel()
		{
			return panel.getPanel();
		}

		@Override
		public void addFilterListener(BiFilterListener<T, U> listener)
		{
			panel.addFilterListener(FilterAdapter.getListenerAdapterArg1(listener));	
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			panel.removeFilterListener(FilterAdapter.getListenerAdapterArg1(listener));
		}

		@Override
		public void notifyListeners()
		{
			panel.notifyListeners();
		}
		
	}

	
	public static class SecondArgAdapter<T, U> implements BiFilterPanel<T, U>
	{
		
		private FilterPanel<U> panel;
		
		public SecondArgAdapter(FilterPanel<U> panel)
		{
			this.panel = panel;
		}

		@Override
		public void paint()
		{
			panel.paint();
		}

		@Override
		public boolean check(T t, U u)
		{
			return panel.check(u);
		}

		@Override
		public JPanel getPanel()
		{
			return panel.getPanel();
		}

		@Override
		public void addFilterListener(BiFilterListener<T, U> listener)
		{
			panel.addFilterListener(FilterAdapter.getListenerAdapterArg2(listener));	
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			panel.removeFilterListener(FilterAdapter.getListenerAdapterArg2(listener));
		}

		@Override
		public void notifyListeners()
		{
			panel.notifyListeners();
		}
		
	}

}