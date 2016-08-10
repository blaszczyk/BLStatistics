package bn.blaszczyk.blstatistics.gui.filters;


import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class FilterPanelAdapter {

	public static <T,U> BiFilterPanel<T, U> getFirstArgAdapter(FilterPanel<T> panel)
	{
		return new FirstArgAdapter<T,U>(panel);
	}

	public static <T,U> BiFilterPanel<T, U> getSecondArgAdapter(FilterPanel<U> panel)
	{
		return new SecondArgAdapter<T,U>(panel);
	}

	@SuppressWarnings("serial")
	public static class FirstArgAdapter<T, U> extends AbstractBiFilterPanel<T, U>
	{
		private FilterPanel<T> innerPanel;
		
		public FirstArgAdapter(FilterPanel<T> panel)
		{
			this.innerPanel = panel;
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
			return String.valueOf(innerPanel);
		}

		@Override
		public void addFilterListener(BiFilterListener<T, U> listener)
		{
			super.addFilterListener(listener);
			innerPanel.addFilterListener( FilterListenerAdapter.getFirstArgAdapter(listener));
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			super.addFilterListener(listener);
			innerPanel.removeFilterListener( FilterListenerAdapter.getFirstArgAdapter(listener));
		}

		@Override
		public void addPopupMenuItem(JMenuItem item)
		{
			innerPanel.addPopupMenuItem(item);
		}


		@Override
		public void setActive(boolean active)
		{
			innerPanel.setActive(active);
		}

		@Override
		public boolean isActive()
		{
			return innerPanel.isActive();
		}

		@Override
		protected void addComponents()
		{
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!(obj instanceof FirstArgAdapter))
				return false;
			FirstArgAdapter<?,?> that = (FirstArgAdapter<?,?>) obj;
			return innerPanel.equals(that.innerPanel);
		}
	}
	
	
	@SuppressWarnings("serial")
	public static class SecondArgAdapter<T, U> extends AbstractBiFilterPanel<T, U>
	{
		private FilterPanel<U> innerPanel;
		
		public SecondArgAdapter(FilterPanel<U> panel)
		{
			this.innerPanel = panel;
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
			return String.valueOf(innerPanel);
		}

		@Override
		public void addFilterListener(BiFilterListener<T, U> listener)
		{
			super.addFilterListener(listener);
			innerPanel.addFilterListener( FilterListenerAdapter.getSecondArgAdapter(listener));
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			super.addFilterListener(listener);
			innerPanel.removeFilterListener( FilterListenerAdapter.getSecondArgAdapter(listener));
		}

		@Override
		public void addPopupMenuItem(JMenuItem item)
		{
			innerPanel.addPopupMenuItem(item);
		}


		@Override
		public void setActive(boolean active)
		{
			innerPanel.setActive(active);
		}

		@Override
		public boolean isActive()
		{
			return innerPanel.isActive();
		}

		@Override
		protected void addComponents()
		{
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!(obj instanceof FirstArgAdapter))
				return false;
			FirstArgAdapter<?,?> that = (FirstArgAdapter<?,?>) obj;
			return innerPanel.equals(that.innerPanel);
		}
	}
}