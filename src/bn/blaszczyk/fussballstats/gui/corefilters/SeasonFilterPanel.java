package bn.blaszczyk.fussballstats.gui.corefilters;

import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.LogicalFilter;
import bn.blaszczyk.fussballstats.filters.SeasonFilter;
import bn.blaszczyk.fussballstats.gui.filters.IntegerValueFilterPanel;

@SuppressWarnings("serial")
public class SeasonFilterPanel extends IntegerValueFilterPanel<Season>
{
	public static final String NAME = "Saison";
	
	public SeasonFilterPanel()
	{
		this(EQ,League.THIS_SEASON);
	}
	
	public SeasonFilterPanel( String operator, int refValue)
	{
		super(NAME,operator,refValue);
	}


	@Override
	protected Filter<Season> getFilter()
	{
		int year = getReferenceInt();
		switch(getOperator())
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
