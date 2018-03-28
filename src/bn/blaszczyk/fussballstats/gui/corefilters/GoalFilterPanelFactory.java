package bn.blaszczyk.fussballstats.gui.corefilters;

import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.GameFilterFactory;
import bn.blaszczyk.fussballstats.filters.LogicalFilterFactory;
import bn.blaszczyk.fussballstats.gui.filters.FilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.IntegerValueFilterPanel;

public class GoalFilterPanelFactory
{
	/*
	 * Constants
	 */
	public static final String NAME_GOAL = "Tore";
	public static final String NAME_HOME_GOAL = "Heimtore";
	public static final String NAME_AWAY_GOAL = "Auswärtsore";
	public static final String NAME_GOAL_DIFF = "Tordifferenz";
	
	/*
	 * Factory Methods
	 */
	@SuppressWarnings("serial")
	public static FilterPanel<Game> createGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>(NAME_GOAL,operator,refInt,0,20)
		{
			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getOperator())
				{
				case EQ:
					return GameFilterFactory.createGoalFilter(goals);
				case NEQ:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalFilter(goals));
				case GEQ:
					return GameFilterFactory.createGoalMinFilter(goals);
				case LL:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalMinFilter(goals));
				case LEQ:
					return GameFilterFactory.createGoalMaxFilter(goals);
				case GG:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalMaxFilter(goals));
				}
				return LogicalFilterFactory.createTRUEFilter();
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> createHomeGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>(NAME_HOME_GOAL,operator,refInt,0,20)
		{
			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getOperator())
				{
				case EQ:
					return GameFilterFactory.createGoalHFilter(goals);
				case NEQ:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalHFilter(goals));
				case GEQ:
					return GameFilterFactory.createGoalHMinFilter(goals);
				case LL:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalHMinFilter(goals));
				case LEQ:
					return GameFilterFactory.createGoalHMaxFilter(goals);
				case GG:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalHMaxFilter(goals));
				}
				return LogicalFilterFactory.createTRUEFilter();
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> createAwayGoalFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>(NAME_AWAY_GOAL,operator,refInt,0,20)
		{

			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getOperator())
				{
				case EQ:
					return GameFilterFactory.createGoalAFilter(goals);
				case NEQ:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalAFilter(goals));
				case GEQ:
					return GameFilterFactory.createGoalAMinFilter(goals);
				case LL:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalAMinFilter(goals));
				case LEQ:
					return GameFilterFactory.createGoalAMaxFilter(goals);
				case GG:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalAMaxFilter(goals));
				}
				return LogicalFilterFactory.createTRUEFilter();
			}
		};
	}
	@SuppressWarnings("serial")
	public static FilterPanel<Game> createGoalDiffFilterPanel(String operator, int refInt)
	{
		return new IntegerValueFilterPanel<Game>(NAME_GOAL_DIFF,operator,refInt,-20,20)
		{
			@Override
			protected Filter<Game> getFilter()
			{
				int goals = getReferenceInt();
				switch(getOperator())
				{
				case EQ:
					return GameFilterFactory.createGoalDiffFilter(goals);
				case NEQ:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalDiffFilter(goals));
				case GEQ:
					return GameFilterFactory.createGoalDiffMinFilter(goals);
				case LL:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalDiffMinFilter(goals));
				case LEQ:
					return GameFilterFactory.createGoalDiffMaxFilter(goals);
				case GG:
					return LogicalFilterFactory.createNOTFilter(GameFilterFactory.createGoalDiffMaxFilter(goals));
				}
				return LogicalFilterFactory.createTRUEFilter();
			}
		};
	}
	

}
