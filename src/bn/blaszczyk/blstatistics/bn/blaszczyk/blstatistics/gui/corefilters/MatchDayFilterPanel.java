package bn.blaszczyk.blstatistics.gui.corefilters;

import java.util.Arrays;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;
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
	protected void setOperator()
	{
		int matchDay = getReferenceInt();
		switch(getSelectedOperator())
		{
		case NEQ:
		case EQ:
			setFilter(GameFilter.getMatchDayFilter(matchDay));
			break;
		case LL:
		case GEQ:
			setFilter(GameFilter.getMatchDayMinFilter(matchDay));
			break;
		case GG:
		case LEQ:
			setFilter(GameFilter.getMatchDayMaxFilter(matchDay));
			break;
		}
		if(Arrays.asList(NEQ,LL,GG).contains(getSelectedOperator()))
			setFilter(LogicalFilter.getNOTFilter(getFilter()));
		notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
	}
	
	
}
