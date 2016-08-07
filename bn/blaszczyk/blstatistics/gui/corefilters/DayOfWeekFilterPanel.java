package bn.blaszczyk.blstatistics.gui.corefilters;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;

@SuppressWarnings("serial")
public class DayOfWeekFilterPanel extends AbstractFilterPanel<Game> 
{
	
	private final static String[] DAYS_OF_WEEK = {"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
	
	private JComboBox<String> dowBox;
	
	
	public DayOfWeekFilterPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(DAYS_OF_WEEK);
		dowBox = cbf.createComboBox();
		dowBox.addActionListener(e -> setDayOfWeek());
		setDayOfWeek();
	}
	

	public DayOfWeekFilterPanel(String dayOfWeek)
	{
		this();
		dowBox.setSelectedItem(dayOfWeek);
		setDayOfWeek();
	}

	
	
	private void setDayOfWeek()
	{
		setFilter(GameFilter.getDayOfWeekFilter( dowBox.getSelectedIndex()));
		notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
	}
	

	@Override
	protected void addComponents()
	{
		add(dowBox);
	}
	
	@Override
	public String toString()
	{
		return dowBox.getSelectedItem().toString();
	}
	
}
