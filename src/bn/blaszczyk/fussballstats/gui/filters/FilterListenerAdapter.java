package bn.blaszczyk.fussballstats.gui.filters;

public class FilterListenerAdapter 
{
	public static <T,U> FilterListener<T> getFirstArgAdapter(BiFilterListener<T,U> listener )
	{
		return new FirstArgAdapter<>(listener);
	}
	
	public static <T,U> FilterListener<U> getSecondArgAdapter(BiFilterListener<T,U> listener )
	{
		return new SecondArgAdapter<>(listener);
	}
	

	private static class FirstArgAdapter<T,U> implements FilterListener<T>
	{
		BiFilterListener<T,U> listener;
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
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.getFirstArgAdapter(e.getSource()),
						FilterPanelAdapter.getFirstArgAdapter(e.getNewPanel())));
				break;
			case FilterEvent.SET_FILTER:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.getFirstArgAdapter(e.getSource()),
						e.getOldSourceName()));
				break;
			case FilterEvent.SET_ACTIVE:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.getFirstArgAdapter(e.getSource())));
				break;
			}
		}
	}
	
	private static class SecondArgAdapter<T,U> implements FilterListener<U>
	{
		BiFilterListener<T,U> listener;
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
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.getSecondArgAdapter(e.getSource()),
						FilterPanelAdapter.getSecondArgAdapter(e.getNewPanel())));
				break;
			case FilterEvent.SET_FILTER:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.getSecondArgAdapter(e.getSource()),
						e.getOldSourceName()));
				break;
			case FilterEvent.SET_ACTIVE:
				listener.filter(new BiFilterEvent<T,U>(FilterPanelAdapter.getSecondArgAdapter(e.getSource())));
				break;
			}
		}
	}

}
