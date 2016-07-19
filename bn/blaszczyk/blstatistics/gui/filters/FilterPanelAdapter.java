package bn.blaszczyk.blstatistics.gui.filters;


import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.FilterAdapter;

public class FilterPanelAdapter {

	public static <T,U> BiFilterPanel<T, U> getFirstArgAdapter(FilterPanel<T> panel,PanelMenu<T,U> panelMenu )
	{
		return new FirstArgAdapter<T,U>(panel,panelMenu);
	}

	public static <T,U> BiFilterPanel<T, U> getSecondArgAdapter(FilterPanel<U> panel,PanelMenu<T,U> panelMenu )
	{
		return new SecondArgAdapter<T,U>(panel,panelMenu);
	}

	@SuppressWarnings("serial")
	public static class FirstArgAdapter<T, U> extends AbstractBiFilterPanel<T, U> implements FilterListener<T>
	{
		
		private FilterPanel<T> panel;
		private PanelMenu<T,U> panelMenu;
		
		public FirstArgAdapter(FilterPanel<T> panel,PanelMenu<T,U> panelMenu )
		{
			super(panelMenu);
			this.panelMenu = panelMenu;
			this.panel = panel;
			panel.addPopupMenuItem(replace);
			panel.addFilterListener(this);
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
		public String toString()
		{
			return panel.toString();
		}

		@Override
		public void filter(FilterEvent<T> e)
		{
			if(e.getType() == FilterEvent.RESET_PANEL)
				notifyListeners(new BiFilterEvent<T,U>(this,getFirstArgAdapter(e.getPanel(),panelMenu),BiFilterEvent.RESET_PANEL));
			else
				notifyListeners(new BiFilterEvent<T,U>(this,FilterAdapter.toBiFilterArg1(e.getFilter()),e.getType()));
		}

		@Override
		protected void addComponents()
		{
		}
		
	}
	@SuppressWarnings("serial")
	public static class SecondArgAdapter<T, U> extends AbstractBiFilterPanel<T, U> implements FilterListener<U>
	{
		
		private FilterPanel<U> panel;
		private PanelMenu<T,U> panelMenu;
		
		public SecondArgAdapter(FilterPanel<U> panel,PanelMenu<T,U> panelMenu )
		{
			super(panelMenu);
			this.panelMenu = panelMenu;
			this.panel = panel;
			panel.addPopupMenuItem(replace);
			panel.addFilterListener(this);
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
		public String toString()
		{
			return panel.toString();
		}

		@Override
		public void filter(FilterEvent<U> e)
		{
			if(e.getType() == FilterEvent.RESET_PANEL)
				notifyListeners(new BiFilterEvent<T,U>(this,getSecondArgAdapter(e.getPanel(),panelMenu),BiFilterEvent.RESET_PANEL));
			else
				notifyListeners(new BiFilterEvent<T,U>(this,FilterAdapter.toBiFilterArg2(e.getFilter()),e.getType()));
		}

		@Override
		protected void addComponents()
		{
		}
		
	}


}