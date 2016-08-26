package bn.blaszczyk.fussballstats.gui.filters;

import bn.blaszczyk.fussballstats.filters.FilterAdapterFaytory;

public class FilterListenerAdapterFactory 
{
	/*
	 * Factory Methods
	 */
	public static <T,U> FilterListener<T> createFirstArgAdapter(BiFilterListener<T,U> listener )
	{
		return new FirstArgAdapter<>(listener);
	}
	
	public static <T,U> FilterListener<U> createSecondArgAdapter(BiFilterListener<T,U> listener )
	{
		return new SecondArgAdapter<>(listener);
	}
	
	/*
	 * Adapter Classes
	 */
	private static class FirstArgAdapter<T,U> implements FilterListener<T>
	{
		private final BiFilterListener<T,U> listener;
		public FirstArgAdapter( BiFilterListener<T,U> listener)
		{
			this.listener = listener;
		}
		
		@Override
		public void filter(FilterEvent<T> e)
		{
			switch(e.getType())
			{
			case FilterEvent.SET_PANEL:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.createFirstArgAdapter(e.getSource()),
						FilterPanelAdapter.createFirstArgAdapter(e.getNewPanel()), e.getType()));
				break;
			case FilterEvent.SET_FILTER:
			case FilterEvent.SET_ACTIVE:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.createFirstArgAdapter(e.getSource()),
						FilterAdapterFaytory.createFirstArgAdapter(e.getFilter()), e.getType()));
				break;
			}
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == null)
				return false;
			if(obj == this)
				return true;
			return obj instanceof FirstArgAdapter && ((FirstArgAdapter<?,?>)obj).listener.equals(listener);
		}
	}
	
	private static class SecondArgAdapter<T,U> implements FilterListener<U>
	{
		private final BiFilterListener<T,U> listener;
		
		public SecondArgAdapter( BiFilterListener<T,U> listener)
		{
			this.listener = listener;
		}

		@Override
		public void filter(FilterEvent<U> e)
		{
			switch(e.getType())
			{
			case FilterEvent.SET_PANEL:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.createSecondArgAdapter(e.getSource()),
						FilterPanelAdapter.createSecondArgAdapter(e.getNewPanel()), e.getType()));
				break;
			case FilterEvent.SET_FILTER:
			case FilterEvent.SET_ACTIVE:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.createSecondArgAdapter(e.getSource()),
						FilterAdapterFaytory.createSecondArgAdapter(e.getFilter()), e.getType()));
				break;
			}
		}

		@Override
		public boolean equals(Object obj)
		{
			if(obj == null)
				return false;
			if(obj == this)
				return true;
			return obj instanceof SecondArgAdapter && ((SecondArgAdapter<?,?>)obj).listener.equals(listener);
		}
	}

}
