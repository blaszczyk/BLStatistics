package bn.blaszczyk.fussballstats.gui.corefilters;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.GameFilterFactory;
import bn.blaszczyk.fussballstats.filters.LogicalFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.IntegerValueFilterPanel;

@SuppressWarnings("serial")
public class MatchDayFilterPanel extends IntegerValueFilterPanel<Game>
{
	
	/*
	 * Constants
	 */
	public static final String NAME = "Spieltag";
	
	/*
	 * Constructors
	 */
	public MatchDayFilterPanel()
	{
		super(NAME,EQ,1,0,40);
	}
	
	public MatchDayFilterPanel(String operator, int refInt)
	{
		super(NAME,operator,refInt,1,50);
	}


	/*
	 * IntegerValueFilterPanel Method
	 */
	@Override
	protected Filter<Game> getFilter()
	{
		int matchDay = getReferenceInt();
		switch(getOperator())
		{
		case EQ:
			return GameFilterFactory.createMatchDayFilter(matchDay);
		case NEQ:
			return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createMatchDayFilter(matchDay));
		case GEQ:
			return GameFilterFactory.createMatchDayMinFilter(matchDay);
		case LL:
			return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createMatchDayMinFilter(matchDay));
		case LEQ:
			return GameFilterFactory.createMatchDayMaxFilter(matchDay);
		case GG:
			return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createMatchDayMaxFilter(matchDay));
		}
		return LogicalFilterFactory.createTRUEFilter();
	}
	
}
