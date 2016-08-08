package bn.blaszczyk.blstatistics.gui.corefilters;

import java.util.Arrays;


import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.IntegerValueFilterPanel;

@SuppressWarnings("serial")
public class SeasonFilterPanel extends IntegerValueFilterPanel<Season>
{
	
	public SeasonFilterPanel()
	{
		super("Saison","=",2016);
	}
	
	public SeasonFilterPanel( String operator, int refValue)
	{
		super("Saison",operator,refValue);
	}


	@Override
	protected void setOperator()
	{
		int year = getReferenceInt();
		switch(getSelectedOperator())
		{
		case NEQ:
		case EQ:
			setFilter(SeasonFilter.getSeasonFilter(year));
			break;
		case LL:
		case GEQ:
			setFilter(SeasonFilter.getSeasonMinFilter(year));
			break;
		case GG:
		case LEQ:
			setFilter(SeasonFilter.getSeasonMaxFilter(year));
			break;
		}
		if(Arrays.asList(NEQ,LL,GG).contains(getSelectedOperator()))
			setFilter(LogicalFilter.getNOTFilter(getFilter()));
		notifyListeners(new FilterEvent<Season>(this, getFilter(), FilterEvent.RESET_FILTER));
	}
	

}
