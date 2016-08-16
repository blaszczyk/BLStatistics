package bn.blaszczyk.fussballstats.gui.corefilters;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.GameFilter;
import bn.blaszczyk.fussballstats.filters.LogicalFilter;
import bn.blaszczyk.fussballstats.gui.filters.IntegerValueFilterPanel;

@SuppressWarnings("serial")
public class MatchDayFilterPanel extends IntegerValueFilterPanel<Game>
{

	public static final String NAME = "Spieltag";
	
	public MatchDayFilterPanel()
	{
		super(NAME,"=",1);
	}
	
	public MatchDayFilterPanel(String operator, int refInt)
	{
		super(NAME,operator,refInt);
	}


	@Override
	protected Filter<Game> getFilter()
	{
		int matchDay = getReferenceInt();
		switch(getOperator())
		{
		case EQ:
			return GameFilter.getMatchDayFilter(matchDay);
		case NEQ:
			return LogicalFilter.getNOTFilter(GameFilter.getMatchDayFilter(matchDay));
		case GEQ:
			return GameFilter.getMatchDayMinFilter(matchDay);
		case LL:
			return LogicalFilter.getNOTFilter(GameFilter.getMatchDayMinFilter(matchDay));
		case LEQ:
			return GameFilter.getMatchDayMaxFilter(matchDay);
		case GG:
			return LogicalFilter.getNOTFilter(GameFilter.getMatchDayMaxFilter(matchDay));
		}
		return LogicalFilter.getTRUEFilter();
	}
	
	
}
