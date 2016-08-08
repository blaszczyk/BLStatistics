package bn.blaszczyk.blstatistics.gui.corefilters;

import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
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
	protected Filter<Season> getFilter()
	{
		int year = getReferenceInt();
		switch(getSelectedOperator())
		{
		case EQ:
			return SeasonFilter.getSeasonFilter(year);
		case NEQ:
			return LogicalFilter.getNOTFilter(SeasonFilter.getSeasonFilter(year));
		case GEQ:
			return SeasonFilter.getSeasonMinFilter(year);
		case LL:
			return LogicalFilter.getNOTFilter(SeasonFilter.getSeasonMinFilter(year));
		case LEQ:
			return SeasonFilter.getSeasonMaxFilter(year);
		case GG:
			return LogicalFilter.getNOTFilter(SeasonFilter.getSeasonMaxFilter(year));
		}
		return LogicalFilter.getTRUEFilter();
	}
	

}
