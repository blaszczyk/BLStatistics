package bn.blaszczyk.blstatistics.gui.filters;


import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.FilterAdapter;

public class FilterPanelAdapter {

	public static <T,U> BiFilterPanel<T, U> getFirstArgAdapter(FilterPanel<T> panel,FilterPanelManager<T,U> filterFactory )
	{
		return new FirstArgAdapter<T,U>(panel,filterFactory);
	}

	public static <T,U> BiFilterPanel<T, U> getSecondArgAdapter(FilterPanel<U> panel,FilterPanelManager<T,U> filterFactory )
	{
		return new SecondArgAdapter<T,U>(panel,filterFactory);
	}

	@SuppressWarnings("serial")
	public static class FirstArgAdapter<T, U> extends AbstractBiFilterPanel<T, U> implements FilterListener<T>
	{
		
		private FilterPanel<T> innerPanel;
		private FilterPanelManager<T,U> filterFactory;
		
		public FirstArgAdapter(FilterPanel<T> panel,FilterPanelManager<T,U> filterFactory )
		{
			super(filterFactory);
			this.filterFactory = filterFactory;
			this.innerPanel = panel;
			panel.addPopupMenuItem(replace);
			panel.addPopupMenuItem(negate);
			panel.addFilterListener(this);
		}

		public FilterPanel<T> getInnerPanel()
		{
			return innerPanel;
		}
		
		@Override
		public void paint()
		{
			innerPanel.paint();
		}

		@Override
		public boolean check(T t, U u)
		{
			return innerPanel.check(t);
		}

		@Override
		public JPanel getPanel()
		{
			return innerPanel.getPanel();
		}
		
		@Override
		public String toString()
		{
			return innerPanel.toString();
		}

		@Override
		public void filter(FilterEvent<T> e)
		{
			if(e.getType() == FilterEvent.RESET_PANEL)
				notifyListeners(new BiFilterEvent<T,U>(this,getFirstArgAdapter(e.getPanel(),filterFactory),BiFilterEvent.RESET_PANEL));
			else
				notifyListeners(new BiFilterEvent<T,U>(this,FilterAdapter.toBiFilterArg1(e.getFilter()),e.getType()));
		}

		@Override
		protected void addComponents()
		{
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof FirstArgAdapter))
				return false;
			FirstArgAdapter<T,U> other = (FirstArgAdapter<T,U>) obj;
			return innerPanel.equals(other.innerPanel);
		}

		
		
		
	}
	@SuppressWarnings("serial")
	public static class SecondArgAdapter<T, U> extends AbstractBiFilterPanel<T, U> implements FilterListener<U>
	{
		
		private FilterPanel<U> innerPanel;
		private FilterPanelManager<T,U> filterFactory;
		
		public SecondArgAdapter(FilterPanel<U> panel,FilterPanelManager<T,U> filterFactory )
		{
			super(filterFactory);
			this.filterFactory = filterFactory;
			this.innerPanel = panel;
			panel.addPopupMenuItem(replace);
			panel.addPopupMenuItem(negate);
			panel.addFilterListener(this);
		}
		
		public FilterPanel<U> getInnerPanel()
		{
			return innerPanel;
		}

		@Override
		public void paint()
		{
			innerPanel.paint();
		}

		@Override
		public boolean check(T t, U u)
		{
			return innerPanel.check(u);
		}

		@Override
		public JPanel getPanel()
		{
			return innerPanel.getPanel();
		}
		
		@Override
		public String toString()
		{
			return innerPanel.toString();
		}

		@Override
		public void filter(FilterEvent<U> e)
		{
			if(e.getType() == FilterEvent.RESET_PANEL)
				notifyListeners(new BiFilterEvent<T,U>(this,getSecondArgAdapter(e.getPanel(),filterFactory),BiFilterEvent.RESET_PANEL));
			else
				notifyListeners(new BiFilterEvent<T,U>(this,FilterAdapter.toBiFilterArg2(e.getFilter()),e.getType()));
		}

		@Override
		protected void addComponents()
		{
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof SecondArgAdapter))
				return false;
			SecondArgAdapter<T,U> other = (SecondArgAdapter<T,U>) obj;
			return innerPanel.equals(other.innerPanel);
		}
	}


}