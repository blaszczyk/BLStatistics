package bn.blaszczyk.fussballstats.gui.corefilters;

import java.awt.Component;
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

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.filters.GameFilter;
import bn.blaszczyk.fussballstats.filters.LogicalFilter;
import bn.blaszczyk.fussballstats.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.CompareToFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings( "serial" )
public class DateFilterPanel extends AbstractFilterPanel<Game> implements CompareToFilterPanel<Game>
{
	public static final String NAME = "Datum";

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
	private static final Calendar TODAY = new GregorianCalendar();
	
	private static final String[] MONTH_NAMES =
		{"Januar","Februar","März","April","Mai","Juni","Juli","August","September","Oktober","November","Dezember"};

	private JLabel label = new JLabel(NAME);
	private JComboBox<String> boxOperator = new MyComboBox<>(OPERATORS,50,false);
	private MyComboBox<Integer> boxDate;
	private MyComboBox<String> boxMonth;
	private MyComboBox<Integer> boxYear;

	private Calendar calendar = new GregorianCalendar();
	
	private ActionListener refreshDateBox = e -> {
		int year = TODAY.get(Calendar.YEAR) - boxYear.getSelectedIndex() + 1;
		int month = boxMonth.getSelectedIndex();
		int nrOfDays = getNrOfDays(year, month);
		boxDate.repopulateBox(intSequence(1, nrOfDays));
		((Component)e.getSource()).requestFocusInWindow();
	};

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

		boxYear = new MyComboBox<>( intSequence( League.THIS_SEASON, 1963 ),80,false );
		boxYear.addActionListener(refreshDateBox);
		
		boxMonth =  new MyComboBox<>(MONTH_NAMES,150,false);
		boxMonth.addActionListener(refreshDateBox);
		boxMonth.setCycleListener(e -> boxYear.moveSelection(e.getDirection()));
		
		boxDate = new MyComboBox<>( intSequence( 1, getNrOfDays(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) ) ),50,false );
		boxDate.addActionListener(setFilterListener);
		boxDate.setCycleListener(e -> boxMonth.moveSelection(-e.getDirection()));
		
		setDate( referenceDate );
		setFilter();
	}

	protected void setFilter()
	{
		Date date = getDate();
		switch(getOperator())
		{
		case EQ:
			setFilter(GameFilter.getDateFilter(date));
			break;
		case NEQ:
			setFilter(LogicalFilter.getNOTFilter(GameFilter.getDateFilter(date)));
			break;
		case GEQ:
			setFilter(GameFilter.getDateMinFilter(date));
			break;
		case LL:
			setFilter(LogicalFilter.getNOTFilter(GameFilter.getDateMinFilter(date)));
			break;
		case LEQ:
			setFilter(GameFilter.getDateMaxFilter(date));
			break;
		case GG:
			setFilter(LogicalFilter.getNOTFilter(GameFilter.getDateMaxFilter(date)));
			break;
		}
	}

	
	public Date getDate() 
	{
		int year = TODAY.get(Calendar.YEAR) - boxYear.getSelectedIndex() + 1;
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
	
	@Override
	public String toString()
	{
		return getLabel() + " " + getReferenceValString();
	}


}
