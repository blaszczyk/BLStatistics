package bn.blaszczyk.blstatistics.gui.corefilters;

import java.util.Arrays;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.gui.filters.FilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.FilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.IntegerValueFilterPanel;

public class GoalFilterPanel
{

	@SuppressWarnings("serial")
	public static FilterPanel<Game> getGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Tore",operator,refInt)
		{
			@Override
			protected void setOperator()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case NEQ:
				case EQ:
					setFilter(GameFilter.getGoalFilter(goals));
					break;
				case LL:
				case GEQ:
					setFilter(GameFilter.getGoalMinFilter(goals));
					break;
				case GG:
				case LEQ:
					setFilter(GameFilter.getGoalMaxFilter(goals));
					break;
				}
				if(Arrays.asList(NEQ,LL,GG).contains(getSelectedOperator()))
					setFilter(LogicalFilter.getNOTFilter(getFilter()));
				notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> getHomeGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Heimtore",operator,refInt)
		{
			@Override
			protected void setOperator()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case NEQ:
				case EQ:
					setFilter(GameFilter.getGoal1Filter(goals));
					break;
				case LL:
				case GEQ:
					setFilter(GameFilter.getGoal1MinFilter(goals));
					break;
				case GG:
				case LEQ:
					setFilter(GameFilter.getGoal1MaxFilter(goals));
					break;
				}
				if(Arrays.asList(NEQ,LL,GG).contains(getSelectedOperator()))
					setFilter(LogicalFilter.getNOTFilter(getFilter()));
				notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> getAwayGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Auswärtstore",operator,refInt)
		{
			@Override
			protected void setOperator()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case NEQ:
				case EQ:
					setFilter(GameFilter.getGoal2Filter(goals));
					break;
				case LL:
				case GEQ:
					setFilter(GameFilter.getGoal2MinFilter(goals));
					break;
				case GG:
				case LEQ:
					setFilter(GameFilter.getGoal2MaxFilter(goals));
					break;
				}
				if(Arrays.asList(NEQ,LL,GG).contains(getSelectedOperator()))
					setFilter(LogicalFilter.getNOTFilter(getFilter()));
				notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> getGoalDiffFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Tordifferenz",operator,refInt)
		{
			@Override
			protected void setOperator()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case NEQ:
				case EQ:
					setFilter(GameFilter.getGoalDiffFilter(goals));
					break;
				case LL:
				case GEQ:
					setFilter(GameFilter.getGoalDiffMinFilter(goals));
					break;
				case GG:
				case LEQ:
					setFilter(GameFilter.getGoalDiffMaxFilter(goals));
					break;
				}
				if(Arrays.asList(NEQ,LL,GG).contains(getSelectedOperator()))
					setFilter(LogicalFilter.getNOTFilter(getFilter()));
				notifyListeners(new FilterEvent<Game>(this, getFilter(), FilterEvent.RESET_FILTER));
			}
		};
	}
	

}
