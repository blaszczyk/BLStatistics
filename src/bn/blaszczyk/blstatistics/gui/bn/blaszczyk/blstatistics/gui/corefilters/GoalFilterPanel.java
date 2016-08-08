package bn.blaszczyk.blstatistics.gui.corefilters;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.gui.filters.FilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.IntegerValueFilterPanel;

public abstract class GoalFilterPanel
{

	@SuppressWarnings("serial")
	public static FilterPanel<Game> getGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Tore",operator,refInt)
		{
			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case EQ:
					return GameFilter.getGoalFilter(goals);
				case NEQ:
					return LogicalFilter.getNOTFilter(GameFilter.getGoalFilter(goals));
				case GEQ:
					return GameFilter.getGoalMinFilter(goals);
				case LL:
					return LogicalFilter.getNOTFilter(GameFilter.getGoalMinFilter(goals));
				case LEQ:
					return GameFilter.getGoalMaxFilter(goals);
				case GG:
					return LogicalFilter.getNOTFilter(GameFilter.getGoalMaxFilter(goals));
				}
				return LogicalFilter.getTRUEFilter();
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> getHomeGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Heimtore",operator,refInt)
		{
			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case EQ:
					return GameFilter.getGoal1Filter(goals);
				case NEQ:
					return LogicalFilter.getNOTFilter(GameFilter.getGoal1Filter(goals));
				case GEQ:
					return GameFilter.getGoal1MinFilter(goals);
				case LL:
					return LogicalFilter.getNOTFilter(GameFilter.getGoal1MinFilter(goals));
				case LEQ:
					return GameFilter.getGoal1MaxFilter(goals);
				case GG:
					return LogicalFilter.getNOTFilter(GameFilter.getGoal1MaxFilter(goals));
				}
				return LogicalFilter.getTRUEFilter();
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> getAwayGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Auswärtstore",operator,refInt)
		{

			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case EQ:
					return GameFilter.getGoal2Filter(goals);
				case NEQ:
					return LogicalFilter.getNOTFilter(GameFilter.getGoal2Filter(goals));
				case GEQ:
					return GameFilter.getGoal2MinFilter(goals);
				case LL:
					return LogicalFilter.getNOTFilter(GameFilter.getGoal2MinFilter(goals));
				case LEQ:
					return GameFilter.getGoal2MaxFilter(goals);
				case GG:
					return LogicalFilter.getNOTFilter(GameFilter.getGoal2MaxFilter(goals));
				}
				return LogicalFilter.getTRUEFilter();
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> getGoalDiffFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>("Tordifferenz",operator,refInt)
		{
			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getSelectedOperator())
				{
				case EQ:
					return GameFilter.getGoalDiffFilter(goals);
				case NEQ:
					return LogicalFilter.getNOTFilter(GameFilter.getGoalDiffFilter(goals));
				case GEQ:
					return GameFilter.getGoalDiffMinFilter(goals);
				case LL:
					return LogicalFilter.getNOTFilter(GameFilter.getGoalDiffMinFilter(goals));
				case LEQ:
					return GameFilter.getGoalDiffMaxFilter(goals);
				case GG:
					return LogicalFilter.getNOTFilter(GameFilter.getGoalDiffMaxFilter(goals));
				}
				return LogicalFilter.getTRUEFilter();
			}
		};
	}
	

}
