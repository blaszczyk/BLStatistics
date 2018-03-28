package bn.blaszczyk.fussballstats.gui.corefilters;

import bn.blaszczyk.fussballstats.model.Season;
import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.LogicalFilterFactory;
import bn.blaszczyk.fussballstats.filters.SeasonFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.IntegerValueFilterPanel;

@SuppressWarnings("serial")
public class SeasonFilterPanel extends IntegerValueFilterPanel<Season>
{
	/*
	 * Constants
	 */
	public static final String NAME = "Saison";
	
	/*
	 * Constructors
	 */
	public SeasonFilterPanel()
	{
		this(EQ, FussballStats.THIS_SEASON);
	}
	
	public SeasonFilterPanel( String operator, int refValue)
	{
		super(NAME,operator,refValue,1945, FussballStats.THIS_SEASON);
	}

	/*
	 * IntegerValueFilterPanel Methods
	 */
	@Override
	protected Filter<Season> getFilter()
	{
		int year = getReferenceInt();
		switch(getOperator())
		{
		case EQ:
			return SeasonFilterFactory.createSeasonFilter(year);
		case NEQ:
			return LogicalFilterFactory.createNOTFilter(SeasonFilterFactory.createSeasonFilter(year));
		case GEQ:
			return SeasonFilterFactory.createSeasonMinFilter(year);
		case LL:
			return LogicalFilterFactory.createNOTFilter(SeasonFilterFactory.createSeasonMinFilter(year));
		case LEQ:
			return SeasonFilterFactory.createSeasonMaxFilter(year);
		case GG:
			return LogicalFilterFactory.createNOTFilter(SeasonFilterFactory.createSeasonMaxFilter(year));
		}
		return LogicalFilterFactory.createTRUEFilter();
	}
	
}
