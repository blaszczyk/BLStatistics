package bn.blaszczyk.blstatistics.gui.corefilters;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.gui.filters.IntegerValueFilterPanel;

@SuppressWarnings("serial")
public class MatchDayFilterPanel extends IntegerValueFilterPanel<Game>
{

	
	public MatchDayFilterPanel()
	{
		super("Spieltag","=",1);
	}
	
	public MatchDayFilterPanel(String operator, int refInt)
	{
		super("Spieltag",operator,refInt);
	}


	@Override
	protected Filter<Game> getFilter()
	{
		int matchDay = getReferenceInt();
		switch(getSelectedOperator())
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
