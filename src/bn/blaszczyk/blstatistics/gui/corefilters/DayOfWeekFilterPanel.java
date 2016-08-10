package bn.blaszczyk.blstatistics.gui.corefilters;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.tools.ComboBoxFactory;

@SuppressWarnings("serial")
public class DayOfWeekFilterPanel extends AbstractFilterPanel<Game> 
{
	public static final String NAME = "Wochentag";
	
	private static final String[] DAYS_OF_WEEK = {"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
	
	private JComboBox<String> dowBox;
	
	
	public DayOfWeekFilterPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(DAYS_OF_WEEK);
		dowBox = cbf.createComboBox();
		dowBox.addActionListener(setFilterListener);
		setFilter();
	}
	

	public DayOfWeekFilterPanel(String dayOfWeek)
	{
		this();
		dowBox.setSelectedItem(dayOfWeek);
		setFilter();
	}

	
	
	protected void setFilter()
	{
		setFilter(GameFilter.getDayOfWeekFilter( dowBox.getSelectedIndex()));
	}
	
	public String getDayOfWeek()
	{
		return dowBox.getSelectedItem().toString();
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
