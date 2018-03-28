package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.filters.GameFilterFactory;
import bn.blaszczyk.fussballstats.filters.LogicalFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.CompareToFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings( "serial" )
public class DateFilterPanel extends AbstractFilterPanel<Game> implements CompareToFilterPanel<Game>
{
	/*
	 * Constatns
	 */
	public static final String NAME = "Datum";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
	private static final Calendar TODAY = new GregorianCalendar();
	private static final String[] MONTH_NAMES =
		{"Januar","Februar","März","April","Mai","Juni","Juli","August","September","Oktober","November","Dezember"};

	/*
	 * To deal with Dates
	 */
	private final Calendar calendar = new GregorianCalendar();
	
	/*
	 * Components
	 */
	private final JLabel label = new JLabel(NAME);
	private final JComboBox<String> boxOperator = new MyComboBox<>(OPERATORS,50,false);
	private final MyComboBox<Integer> boxDate = new MyComboBox<>( intSequence( 1, getNrOfDays(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) ) ),50,false );
	private final MyComboBox<String> 	boxMonth =  new MyComboBox<>(MONTH_NAMES,150,false);
	private final MyComboBox<Integer> boxYear = new MyComboBox<>( intSequence( TODAY.get(Calendar.YEAR), 1945 ),80,false );
	
	/*
	 * Fills boxDate according to Month and Year
	 */
	private ActionListener refreshDateBox = e -> {
		int year = TODAY.get(Calendar.YEAR) - boxYear.getSelectedIndex();
		int month = boxMonth.getSelectedIndex();
		int nrOfDays = getNrOfDays(year, month);
		boxDate.repopulateBox(intSequence(1, nrOfDays));
		setFilterListener.actionPerformed(e);
	};

	/*
	 * Constructors
	 */
	public DateFilterPanel()
	{
		this(EQ,(Date)null);
	}
	
	public DateFilterPanel(String operator, String referenceDate)
	{
		this(operator, parseDate(referenceDate));
	}
	
	public DateFilterPanel(String operator, Date referenceDate)
	{
		super(false);
		setLayout( new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		if(referenceDate == null)
			referenceDate = parseDate(DATE_FORMAT.format(new Date()));
		calendar.setTime(referenceDate);
		
		boxOperator.addActionListener(setFilterListener);
		boxOperator.setSelectedItem(operator);

		boxYear.addActionListener(refreshDateBox);
		
		boxMonth.addActionListener(refreshDateBox);
		boxMonth.setCycleListener(e -> boxYear.moveSelection(e.getDirection()));
		
		boxDate.addActionListener(setFilterListener);
		boxDate.setCycleListener(e -> boxMonth.moveSelection(-e.getDirection()));
		
		setDate( referenceDate );
		setFilter();
	}


	/*
	 * Internal Methods
	 */
	private Date getDate() 
	{
		int year = TODAY.get(Calendar.YEAR) - boxYear.getSelectedIndex();
		int month = boxMonth.getSelectedIndex();
		int date = boxDate.getSelectedIndex() + 1;
		calendar.set(year, month, date);
		return calendar.getTime();
	}
	
	private void setDate( Date date )
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		boxYear.setSelectedIndex( TODAY.get(Calendar.YEAR) - calendar.get(Calendar.YEAR) + 1);
		boxMonth.setSelectedIndex( calendar.get(Calendar.MONTH) );
		boxDate.setSelectedIndex( calendar.get(Calendar.DAY_OF_MONTH) - 1 );
	}
	
	private static Date parseDate(String in)
	{
		try
		{
			return DATE_FORMAT.parse(in);
		}
		catch (ParseException e)
		{
			return TODAY.getTime();
		}
	}
	
	private static Integer[] intSequence( int firstValue, int lastValue)
	{
		if( firstValue < lastValue )
		{
			int length = lastValue - firstValue + 1;
			Integer[] result = new Integer[length];
			for( int i = 0; i < result.length; i++ )
				result[i] = firstValue + i;
			return result;
		}
		else
		{
			int length = firstValue - lastValue + 1;
			Integer[] result = new Integer[length];
			for( int i = 0; i < result.length; i++ )
				result[i] = firstValue - i;
			return result;
		}
	}
	
	private static int getNrOfDays( int year, int month )
	{
		switch(month)
		{
		case 1:
			if( year % 4 == 0 ) // Works until 28.02.2100 :)
				return 29;
			return 28;
		case 3: case 5: case 8: case 10:
			return 30;
		default:
			return 31;	
		}
	}
	
	/*
	 * AbstractFilterPanel Methods
	 */
	@Override
	protected void addComponents()
	{
		add(label);
		add(boxOperator);
		
		add(boxDate);
		add(boxMonth);
		add(boxYear);
	}

	@Override
	protected void setFilter()
	{
		Date date = getDate();
		switch(getOperator())
		{
		case EQ:
			setFilter(GameFilterFactory.createDateFilter(date));
			break;
		case NEQ:
			setFilter(LogicalFilterFactory.createNOTFilter(GameFilterFactory.createDateFilter(date)));
			break;
		case GEQ:
			setFilter(GameFilterFactory.createDateMinFilter(date));
			break;
		case LL:
			setFilter(LogicalFilterFactory.createNOTFilter(GameFilterFactory.createDateMinFilter(date)));
			break;
		case LEQ:
			setFilter(GameFilterFactory.createDateMaxFilter(date));
			break;
		case GG:
			setFilter(LogicalFilterFactory.createNOTFilter(GameFilterFactory.createDateMaxFilter(date)));
			break;
		}
	}

	/*
	 * CompareToFilterPanel Methods
	 */
	@Override
	public void invertOperator()
	{
		switch(getOperator())
		{
		case EQ:
			boxOperator.setSelectedItem(NEQ);
			break;
		case NEQ:
			boxOperator.setSelectedItem(EQ);
			break;
		case LEQ:
			boxOperator.setSelectedItem(GG);
			break;
		case GG:
			boxOperator.setSelectedItem(LEQ);
			break;
		case GEQ:
			boxOperator.setSelectedItem(LL);
			break;
		case LL:
			boxOperator.setSelectedItem(GEQ);
			break;
		}
	}

	@Override
	public String getOperator()
	{
		return boxOperator.getSelectedItem().toString();
	}

	@Override
	public String getLabel()
	{
		return NAME;
	}

	@Override
	public String getReferenceValString()
	{
		return DATE_FORMAT.format(getDate());
	}
	
	/*
	 * Object Method
	 */
	@Override
	public String toString()
	{
		return getLabel() + " " + getReferenceValString();
	}


}
