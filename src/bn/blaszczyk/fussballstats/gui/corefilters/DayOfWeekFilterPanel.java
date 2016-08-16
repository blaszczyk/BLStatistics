package bn.blaszczyk.fussballstats.gui.corefilters;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.filters.GameFilter;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class DayOfWeekFilterPanel extends AbstractFilterPanel<Game> 
{
	public static final String NAME = "Wochentag";
	
	private static final String[] DAYS_OF_WEEK = {"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
	
	private JComboBox<String> dowBox;
	
	
	public DayOfWeekFilterPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		dowBox = new MyComboBox<>(DAYS_OF_WEEK,250,false);
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
