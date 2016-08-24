package bn.blaszczyk.fussballstats.tools;

import java.awt.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.gui.corefilters.*;
import bn.blaszczyk.fussballstats.gui.filters.*;

public class FilterParser
{
	/*
	 * Variables
	 */
	private static StringBuilder outerBuilder;
	private static int panelCount;
	private static Map<String, BiFilterPanel<Season, Game>> filters;
	
	/*
	 * Writes A Filter to A String
	 */
	public static String writeFilter(BiFilterPanel<Season, Game> filter)
	{
		outerBuilder = new StringBuilder();
		panelCount = 0;
		writeSubFilter(filter);
		return outerBuilder.toString();
	}
	
	/*
	 * Parses Filter From String / InputStream
	 */
	public static BiFilterPanel<Season, Game> parseFilter(String in)
	{
		InputStream stream = new ByteArrayInputStream(in.getBytes(StandardCharsets.ISO_8859_1));
		return parseFilter(stream);
	}

	public static BiFilterPanel<Season, Game> parseFilter(InputStream iStream)
	{
		filters = new HashMap<>();
		BiFilterPanel<Season, Game> lastPanel = new NoFilterPanel<>();
		Scanner scanner = new Scanner(iStream);
		while(scanner.hasNextLine())
			lastPanel = parseSubFilter(scanner.nextLine());
		scanner.close();
		return lastPanel;
	}
	
	/*
	 * Internal Writer
	 */
	private static int writeSubFilter(BiFilterPanel<Season, Game> filter)
	{
		StringBuilder innerBuilder = new StringBuilder();
		if (filter instanceof MultiOperatorFilterPanel)
		{
			MultiOperatorFilterPanel<Season, Game> mFilter = (MultiOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append(MultiOperatorFilterPanel.NAME + ";" + mFilter.getOperator());
			for (BiFilterPanel<Season, Game> p : mFilter)
				innerBuilder.append(";F" + writeSubFilter(p));
		}
		else if (filter instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> iteFilter = (IfThenElseFilterPanel<Season, Game>) filter;
			int ifInt = writeSubFilter(iteFilter.getIfFilter());
			int thenInt = writeSubFilter(iteFilter.getThenFilter());
			int elseInt = writeSubFilter(iteFilter.getElseFilter());
			innerBuilder.append(String.format("%s;F%d;F%d;F%d", IfThenElseFilterPanel.NAME, ifInt, thenInt, elseInt));
		}
		else if (filter instanceof UnaryOperatorFilterPanel)
		{
			UnaryOperatorFilterPanel<Season, Game> uFilter = (UnaryOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append(UnaryOperatorFilterPanel.NAME + ";F" + writeSubFilter(uFilter.getInnerPanel()));
		}
		else if (filter instanceof AbsoluteOperatorFilterPanel)
		{
			AbsoluteOperatorFilterPanel<Season, Game> aFilter = (AbsoluteOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append(aFilter.toString());
		}
		else if (filter instanceof RoundFilterPanel)
		{
			RoundFilterPanel rFilter = (RoundFilterPanel) filter;
			innerBuilder.append( RoundFilterPanel.NAME +  ";" + rFilter.isFirstRound() + ";" + rFilter.isSecondRound());
		}
		else if (filter instanceof NoFilterPanel)
		{
			innerBuilder.append(NoFilterPanel.NAME);
		}
		else if (filter instanceof FilterPanelAdapter)
		{
			FilterPanel<?> iFilter = (FilterPanel<?>) ((FilterPanelAdapter<Season, Game>) filter).getInnerPanel();
			if (iFilter instanceof CompareToFilterPanel)
			{
				CompareToFilterPanel<?> ctFilter = (CompareToFilterPanel<?>) iFilter;
				innerBuilder.append(ctFilter.getLabel() + ";" + ctFilter.getOperator() + ";" + ctFilter.getReferenceValString());
			}
			else if(iFilter instanceof SingleLeagueFilterPanel)
			{
				SingleLeagueFilterPanel lFilter =  (SingleLeagueFilterPanel) iFilter;
				innerBuilder.append( SingleLeagueFilterPanel.NAME + ";" + lFilter.getSelectedLeague() + ";" + lFilter.isContains());
			}
			else if (iFilter instanceof TeamFilterPanel)
			{
				TeamFilterPanel tFilter = (TeamFilterPanel) iFilter;
				innerBuilder.append(String.format("%s;%s;%s;%s", TeamFilterPanel.NAME,  tFilter.getTeam(), tFilter.isHome(), tFilter.isAway()));
			}
			else if (iFilter instanceof SubLeagueFilterPanel)
			{
				SubLeagueFilterPanel slFilter = (SubLeagueFilterPanel) iFilter;
				innerBuilder.append(SubLeagueFilterPanel.NAME);
				for (int i = 0; i < slFilter.getTeamCount(); i++)
					innerBuilder.append(";" + slFilter.getTeam(i));
			}
			else if(iFilter instanceof DayOfWeekFilterPanel)
			{
				DayOfWeekFilterPanel dFilter = (DayOfWeekFilterPanel) iFilter;
				innerBuilder.append(DayOfWeekFilterPanel.NAME + ";" +  dFilter.getDayOfWeek());
			}
		}
		else
		{
			System.err.println("Unbekannter Filter: " + filter);
		}
		outerBuilder.append(String.format("F%d;%s;%s\n", panelCount, filter.isActive(), innerBuilder.toString()));
		return panelCount++;
	}

	/*
	 * Internal Parser
	 */
	private static BiFilterPanel<Season, Game> parseSubFilter(String in)
	{
		String[] split = in.split(";");
		BiFilterPanel<Season, Game> panel = new NoFilterPanel<>();
		if (split.length < 2)
			return panel;
		switch (split[2])
		{
		case MultiOperatorFilterPanel.NAME:
			List<BiFilterPanel<Season, Game>> pList = new ArrayList<>();
			for (int i = 4; i < split.length; i++)
				pList.add(filters.get(split[i]));
			panel = new MultiOperatorFilterPanel<>(pList, split[3]);
			break;
		case IfThenElseFilterPanel.NAME:
			panel = new IfThenElseFilterPanel<>(filters.get(split[3]), filters.get(split[4]), filters.get(split[5]));
			break;
		case UnaryOperatorFilterPanel.NAME:
			panel = new UnaryOperatorFilterPanel<>(filters.get(split[3]));
			break;
		case AbsoluteOperatorFilterPanel.TRUE_NAME:
			panel = new AbsoluteOperatorFilterPanel<>(true );
			break;
		case AbsoluteOperatorFilterPanel.FALSE_NAME:
			panel = new AbsoluteOperatorFilterPanel<>(false );
			break;
		case RoundFilterPanel.NAME:
			panel = new RoundFilterPanel(Boolean.parseBoolean(split[3]), Boolean.parseBoolean(split[4]));
			break;
		case NoFilterPanel.NAME:
			panel = new NoFilterPanel<Season, Game>();
			break;
		case SeasonFilterPanel.NAME:
			panel = FilterPanelAdapter.createFirstArgAdapter(new SeasonFilterPanel(split[3], Integer.parseInt(split[4])) );
			break;
		case SingleLeagueFilterPanel.NAME:
			panel = FilterPanelAdapter.createFirstArgAdapter(new SingleLeagueFilterPanel(split[3], Boolean.parseBoolean(split[4])) );
			break;
		case MatchDayFilterPanel.NAME:
			panel = FilterPanelAdapter.createSecondArgAdapter(new MatchDayFilterPanel(split[3], Integer.parseInt(split[4])) );
			break;
		case GoalFilterPanelFactory.NAME_GOAL:
			panel = FilterPanelAdapter.createSecondArgAdapter(GoalFilterPanelFactory.createGoalFilterPanel(split[3], Integer.parseInt(split[4])) );
			break;
		case GoalFilterPanelFactory.NAME_HOME_GOAL:
			panel = FilterPanelAdapter.createSecondArgAdapter(GoalFilterPanelFactory.createHomeGoalFilterPanel(split[3], Integer.parseInt(split[4])) );
			break;
		case GoalFilterPanelFactory.NAME_AWAY_GOAL:
			panel = FilterPanelAdapter.createSecondArgAdapter(GoalFilterPanelFactory.createAwayGoalFilterPanel(split[3], Integer.parseInt(split[4])) );
			break;
		case GoalFilterPanelFactory.NAME_GOAL_DIFF:
			panel = FilterPanelAdapter.createSecondArgAdapter(GoalFilterPanelFactory.createGoalDiffFilterPanel(split[3], Integer.parseInt(split[4])) );
			break;
		case TeamFilterPanel.NAME:
			panel = FilterPanelAdapter.createSecondArgAdapter(new TeamFilterPanel(split[3], Boolean.parseBoolean(split[4]), Boolean.parseBoolean(split[5])) );
			break;
		case SubLeagueFilterPanel.NAME:
			panel = FilterPanelAdapter.createSecondArgAdapter(new SubLeagueFilterPanel(Arrays.asList(split).subList(3, split.length)) );
			break;
		case DateFilterPanel.NAME:
			panel = FilterPanelAdapter.createSecondArgAdapter(new DateFilterPanel(split[3], split[4] ) );
			break;
		case DayOfWeekFilterPanel.NAME:
			panel = FilterPanelAdapter.createSecondArgAdapter(new DayOfWeekFilterPanel(split[3]));
			break;
		default:
			System.err.println("Unbekannter Filter:" + in);
		}
		panel.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setActive(Boolean.parseBoolean(split[1]));
		FilterMenuFactory.createPopupMenu(panel);
		filters.put(split[0], panel);
		return panel;
	}
}
