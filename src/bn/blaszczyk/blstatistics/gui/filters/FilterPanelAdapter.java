package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public abstract class FilterPanelAdapter<T,U> implements BiFilterPanel<T, U>
{

	public abstract FilterPanel<?> getInnerPanel();
	
	public static <T,U> BiFilterPanel<T, U> getFirstArgAdapter(FilterPanel<T> panel)
	{
		return new FirstArgAdapter<T,U>(panel);
	}

	public static <T,U> BiFilterPanel<T, U> getSecondArgAdapter(FilterPanel<U> panel)
	{
		return new SecondArgAdapter<T,U>(panel);
	}

	public static class FirstArgAdapter<T, U> extends FilterPanelAdapter<T, U>
	{
		private FilterPanel<T> innerPanel;
		private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
		
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
			listeners.add(listener);
			innerPanel.addFilterListener( FilterListenerAdapter.getFirstArgAdapter(listener));
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			int i = listeners.indexOf(listener);
			if( i >= 0 )
				listeners.remove(i);
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
		public void addPopupMenuLabel(JLabel item)
		{
			innerPanel.addPopupMenuLabel(item);
		}

		@Override
		public void addPopupMenuSeparator()
		{
			innerPanel.addPopupMenuSeparator();
		}

		@Override
		public void replaceMe(BiFilterPanel<T, U> panel)
		{
			BiFilterEvent<T, U> e = new BiFilterEvent<>(this, panel, BiFilterEvent.RESET_PANEL);
			List<BiFilterListener<T,U>> copy = new ArrayList<>(listeners.size());
			for(BiFilterListener<T, U> listener : listeners)
				copy.add(listener);
			for(BiFilterListener<T, U> listener : copy)
				listener.filter(e);		
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
	
	
	public static class SecondArgAdapter<T, U> extends FilterPanelAdapter<T, U>
	{
		private FilterPanel<U> innerPanel;
		private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
		
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
			listeners.add(listener);
			innerPanel.addFilterListener( FilterListenerAdapter.getSecondArgAdapter(listener));
		}

		@Override
		public void removeFilterListener(BiFilterListener<T, U> listener)
		{
			int i = listeners.indexOf(listener);
			if( i >= 0 )
				listeners.remove(i);
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
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!(obj instanceof SecondArgAdapter))
				return false;
			SecondArgAdapter<?,?> that = (SecondArgAdapter<?,?>) obj;
			return innerPanel.equals(that.innerPanel);
		}
		

		@Override
		public void addPopupMenuLabel(JLabel item)
		{
			innerPanel.addPopupMenuLabel(item);
		}

		@Override
		public void addPopupMenuSeparator()
		{
			innerPanel.addPopupMenuSeparator();
		}

		@Override
		public void replaceMe(BiFilterPanel<T, U> panel)
		{
			BiFilterEvent<T, U> e = new BiFilterEvent<>(this, panel, BiFilterEvent.RESET_PANEL);
			List<BiFilterListener<T,U>> copy = new ArrayList<>(listeners.size());
			for(BiFilterListener<T, U> listener : listeners)
				copy.add(listener);
			for(BiFilterListener<T, U> listener : copy)
				listener.filter(e);		
		}
	}
}