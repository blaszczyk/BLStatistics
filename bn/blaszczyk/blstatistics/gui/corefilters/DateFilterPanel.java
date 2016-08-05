package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;


/*
 * TODO: 
 * 	-moussewheel / keys in ComboBoxes
 * 	-FileIO
 */

@SuppressWarnings({ "deprecation", "serial" })
public class DateFilterPanel extends AbstractFilterPanel<Game>
{
	private static final String EQ = "=";
	private static final String NEQ = "!=";
	private static final String GEQ = ">=";
	private static final String LEQ = "<=";
	private static final String GG = ">";
	private static final String LL = "<";
	
	private static final Date TODAY = new Date();
	private static final String[] MONTH_NAME =
		{"Januar","Februar","März","April","Mai","Juni","Juli","August","September","Oktober","November","Dezember"};

	private JLabel label = new JLabel("Datum");
	private JComboBox<String> operatorBox;
	private JComboBox<Integer> dateBox;
	private JComboBox<String> monthBox;
	private JComboBox<Integer> yearBox;
	
	public DateFilterPanel()
	{
		this("=",TODAY);
	}
	
	public DateFilterPanel(String operator, Date referenceDate)
	{
		String[] operators = {EQ,NEQ,GG,GEQ,LL,LEQ};
		operatorBox = new JComboBox<>(operators);
		operatorBox.setMaximumSize(new Dimension(50,30));
		operatorBox.setInheritsPopupMenu(true);
		operatorBox.addActionListener(e -> setOperator());

		yearBox = new JComboBox<Integer>( intSequence( TODAY.getYear()+1900, 1964 ) );
		yearBox.setMaximumSize(new Dimension(80,30));
		yearBox.setInheritsPopupMenu(true);
		monthBox = new JComboBox<String>( MONTH_NAME );
		monthBox.setMaximumSize(new Dimension(150,30));
		monthBox.setInheritsPopupMenu(true);
		dateBox = new JComboBox<Integer>( intSequence( 1, getNrOfDays(referenceDate.getMonth(), referenceDate.getYear()) ) );
		dateBox.setMaximumSize(new Dimension(50,30));
		dateBox.setInheritsPopupMenu(true);
		
		setDate( referenceDate );
		
		ActionListener refreshDateBox = e -> {
			int year = TODAY.getYear() - yearBox.getSelectedIndex();
			int month = monthBox.getSelectedIndex();
			int date = dateBox.getSelectedIndex() + 1;
			dateBox.removeAllItems();
			for( int i = 1; i <= getNrOfDays(month, year); i++ )
				dateBox.addItem(i);
			dateBox.setSelectedIndex( date - 1);
		};
		yearBox.addActionListener(refreshDateBox);
		monthBox.addActionListener(refreshDateBox);
		dateBox.addActionListener( e -> setOperator());
		
		setLayout( new BoxLayout(this, BoxLayout.LINE_AXIS));
	}

	protected void setOperator()
	{
		Date date = getDate();
		String operator = operatorBox.getSelectedItem().toString();
		switch(operator)
		{
		case NEQ:
		case EQ:
			setFilter(GameFilter.getDateFilter(date));
			break;
		case LL:
		case GEQ:
			setFilter(GameFilter.getDateMinFilter(date));
			break;
		case GG:
		case LEQ:
			setFilter(GameFilter.getDateMaxFilter(date));
			break;
		}
		if(Arrays.asList(NEQ,LL,GG).contains(operator))
			setFilter(LogicalFilter.getNOTFilter(getFilter()));
		notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(operatorBox);
		add(dateBox);
		add(monthBox);
		add(yearBox);
	}

	public Date getDate() 
	{
		int year = TODAY.getYear() - yearBox.getSelectedIndex();
		int month = monthBox.getSelectedIndex();
		int date = dateBox.getSelectedIndex() + 1;
		return new Date(year, month, date);
	}
	
	private void setDate( Date date )
	{
		yearBox.setSelectedIndex( TODAY.getYear() - date.getYear() );
		monthBox.setSelectedIndex( date.getMonth() );
		dateBox.setSelectedIndex( date.getDate() - 1 );
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
	
	private static int getNrOfDays( int month, int year )
	{
		switch(month)
		{
		case 1:
			if( year % 4 == 0) // Works until 28.02.2100 :)
				return 29;
			return 28;
		case 3: case 5: case 8: case 10:
			return 30;
		default:
			return 31;	
		}
	}
	
}
