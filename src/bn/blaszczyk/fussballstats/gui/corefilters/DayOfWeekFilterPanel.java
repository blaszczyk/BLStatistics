package bn.blaszczyk.fussballstats.gui.corefilters;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.filters.GameFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class DayOfWeekFilterPanel extends AbstractFilterPanel<Game> 
{
	/*
	 * Constants
	 */
	public static final String NAME = "Wochentag";
	private static final String[] DAYS_OF_WEEK = {"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
	
	/*
	 * Components
	 */
	private final JComboBox<String> boxDayOfWeek = new MyComboBox<>(DAYS_OF_WEEK,250,false);;
	
	/*
	 * Constructors
	 */
	public DayOfWeekFilterPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		boxDayOfWeek.addActionListener(setFilterListener);
		setFilter();
	}

	public DayOfWeekFilterPanel(String dayOfWeek)
	{
		this();
		boxDayOfWeek.setSelectedItem(dayOfWeek);
		setFilter();
	}

	/*
	 * Getter
	 */
	public String getDayOfWeek()
	{
		return boxDayOfWeek.getSelectedItem().toString();
	}
	
	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void setFilter()
	{
		setFilter(GameFilterFactory.createDayOfWeekFilter( boxDayOfWeek.getSelectedIndex()));
	}
	

	@Override
	protected void addComponents()
	{
		add(boxDayOfWeek);
	}

	/*
	 * Object Methods
	 */
	@Override
	public String toString()
	{
		return boxDayOfWeek.getSelectedItem().toString();
	}
	
}
