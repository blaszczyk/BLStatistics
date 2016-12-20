package bn.blaszczyk.fussballstats.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


public abstract class FilterPanelAdapter<T,U> implements BiFilterPanel<T, U>
{
	
	/*
	 * Factory Methods
	 */
	public static <T,U> BiFilterPanel<T, U> createFirstArgAdapter(FilterPanel<T> panel)
	{
		return new FirstArgAdapter<T,U>(panel);
	}

	public static <T,U> BiFilterPanel<T, U> createSecondArgAdapter(FilterPanel<U> panel)
	{
		return new SecondArgAdapter<T,U>(panel);
	}

	
	/*
	 * Components
	 */
	protected final List<BiFilterListener<T,U>> listeners = new ArrayList<>();

	/*
	 * Abstract Methods
	 */
	public abstract FilterPanel<?> getInnerPanel();

	/*
	 * BiFilterPanel Methods
	 */
	@Override
	public void paint()
	{
		getInnerPanel().paint();
	}

	@Override
	public JPanel getPanel()
	{
		return getInnerPanel().getPanel();
	}


	@Override
	public void setActive(boolean active)
	{
		getInnerPanel().setActive(active);
	}

	@Override
	public boolean isActive()
	{
		return getInnerPanel().isActive();
	}

	@Override
	public void replaceMe(BiFilterPanel<T, U> panel)
	{
		BiFilterEvent<T, U> e = new BiFilterEvent<>(this, panel, BiFilterEvent.SET_PANEL);
		List<BiFilterListener<T,U>> copy = new ArrayList<>(listeners.size());
		for(BiFilterListener<T, U> listener : listeners)
			copy.add(listener);
		for(BiFilterListener<T, U> listener : copy)
			listener.filter(e);		
	}

	@Override
	public String toString()
	{
		return String.valueOf(getInnerPanel());
	}	
	
	/*
	 * First Argument Adapter Subclass
	 */
	public static class FirstArgAdapter<T, U> extends FilterPanelAdapter<T, U>
	{
		private final FilterPanel<T> innerPanel;
		
		public FirstArgAdapter(FilterPanel<T> panel)
		{
			this.innerPanel = panel;
		}

		@Override
		public FilterPanel<T> getInnerPanel()
		{
			return innerPanel;
		}
		
		@Override
		public boolean check(T t, U u)
		{
			return innerPanel.check(t);
		}
		
		@Override
		public void addFilterListener(BiFilterListener<T, U> listener)
		{
			listeners.add(listener);
			innerPanel.addFilterListener( FilterListenerAdapterFactory.createFirstArgAdapter(listener));
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			int i = listeners.indexOf(listener);
			if( i >= 0 )
				listeners.remove(i);
			innerPanel.removeFilterListener( FilterListenerAdapterFactory.createFirstArgAdapter(listener));
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
	
	/*
	 * Second Argument Adapter Subclass
	 */
	public static class SecondArgAdapter<T, U> extends FilterPanelAdapter<T, U>
	{
		private final FilterPanel<U> innerPanel;
		
		public SecondArgAdapter(FilterPanel<U> panel)
		{
			this.innerPanel = panel;
		}

		@Override
		public FilterPanel<U> getInnerPanel()
		{
			return innerPanel;
		}

		@Override
		public boolean check(T t, U u)
		{
			return innerPanel.check(u);
		}

		@Override
		public void addFilterListener(BiFilterListener<T, U> listener)
		{
			listeners.add(listener);
			innerPanel.addFilterListener( FilterListenerAdapterFactory.createSecondArgAdapter(listener));
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			int i = listeners.indexOf(listener);
			if( i >= 0 )
				listeners.remove(i);
			innerPanel.removeFilterListener( FilterListenerAdapterFactory.createSecondArgAdapter(listener));
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!(obj instanceof SecondArgAdapter))
				return false;
			SecondArgAdapter<?,?> that = (SecondArgAdapter<?,?>) obj;
			return innerPanel.equals(that.innerPanel);
		}
	}
}