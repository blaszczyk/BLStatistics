package bn.blaszczyk.blstatistics.tools;

import java.awt.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.gui.corefilters.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class FilterParser
{
	private static StringBuilder outerBuilder;
	private static int panelCount;
	private static Map<String, BiFilterPanel<Season, Game>> filters;
	

	public static String writeFilter(BiFilterPanel<Season, Game> filter)
	{
		outerBuilder = new StringBuilder();
		panelCount = 0;
		writeSubFilter(filter);
		return outerBuilder.toString();
	}
	
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
		else if (filter instanceof FilterPanelAdapter.FirstArgAdapter)
		{
			FilterPanel<Season> sFilter = ((FilterPanelAdapter.FirstArgAdapter<Season, Game>) filter).getInnerPanel();
			if (sFilter instanceof IntegerValueFilterPanel)
			{
				IntegerValueFilterPanel<Season> iPanel = (IntegerValueFilterPanel<Season>) sFilter;
				innerBuilder.append(iPanel.getLabel() + ";" + iPanel.getSelectedOperator() + ";" + iPanel.getReferenceInt());
			}
			else if(sFilter instanceof SingleLeagueFilterPanel)
			{
				SingleLeagueFilterPanel iPanel =  (SingleLeagueFilterPanel) sFilter;
				innerBuilder.append( SingleLeagueFilterPanel.NAME + ";" + iPanel.getSelectedLeague() + ";" + iPanel.isRecursive());
			}
			else
			{
				System.err.println("Unknown Filter " + sFilter);
			}
		}
		else if (filter instanceof FilterPanelAdapter.SecondArgAdapter)
		{
			FilterPanel<Game> gFilter = ((FilterPanelAdapter.SecondArgAdapter<Season, Game>) filter).getInnerPanel();
			if (gFilter instanceof IntegerValueFilterPanel)
			{
				IntegerValueFilterPanel<Game> iPanel = (IntegerValueFilterPanel<Game>) gFilter;
				innerBuilder.append(iPanel.getLabel() + ";" + iPanel.getSelectedOperator() + ";" + iPanel.getReferenceInt());
			}
			else if (gFilter instanceof TeamFilterPanel)
			{
				TeamFilterPanel tFilter = (TeamFilterPanel) gFilter;
				innerBuilder.append(String.format("%s;%s;%s;%s", TeamFilterPanel.NAME,  tFilter.getTeam(), tFilter.getHome(), tFilter.getAway()));
			}
			else if (gFilter instanceof TeamSearchFilterPanel)
			{
				TeamSearchFilterPanel tFilter = (TeamSearchFilterPanel) gFilter;
				innerBuilder.append(String.format("%s;%s;%s", TeamSearchFilterPanel.NAME,  tFilter.getTeam(), tFilter.isStrict()));
			}
			else if (gFilter instanceof SubLeagueFilterPanel)
			{
				SubLeagueFilterPanel slFilter = (SubLeagueFilterPanel) gFilter;
				innerBuilder.append(SubLeagueFilterPanel.NAME);
				for (int i = 0; i < slFilter.getTeamCount(); i++)
					innerBuilder.append(";" + slFilter.getTeam(i));
			}
			else if(gFilter instanceof DateFilterPanel)
			{
				DateFilterPanel dFilter = (DateFilterPanel) gFilter;
				innerBuilder.append(String.format("%s;%s;%s", DateFilterPanel.NAME, dFilter.getOperator(), Game.DATE_FORMAT.format(dFilter.getDate())));
			}
			else if(gFilter instanceof DayOfWeekFilterPanel)
			{
				DayOfWeekFilterPanel dFilter = (DayOfWeekFilterPanel) gFilter;
				innerBuilder.append(DayOfWeekFilterPanel.NAME + ";" +  dFilter.getDayOfWeek());
			}
			else
			{
				System.err.println("Unknown Filter " + gFilter);
			}
		}
		else
		{
			System.err.println("Unknown Filter " + filter);
		}
		outerBuilder.append(String.format("F%d;%s\n", panelCount, innerBuilder.toString()));
		return panelCount++;
	}


	private static BiFilterPanel<Season, Game> parseSubFilter(String in)
	{
		String[] split = in.split(";");
		BiFilterPanel<Season, Game> panel = null;
		if (split.length < 2)
			return null;
		switch (split[1])
		{
		case MultiOperatorFilterPanel.NAME:
			List<BiFilterPanel<Season, Game>> pList = new ArrayList<>();
			for (int i = 3; i < split.length; i++)
				pList.add(filters.get(split[i]));
			panel = new MultiOperatorFilterPanel<>(pList, split[2]);
			break;
		case IfThenElseFilterPanel.NAME:
			panel = new IfThenElseFilterPanel<>(filters.get(split[2]), filters.get(split[3]), filters.get(split[4]));
			break;
		case UnaryOperatorFilterPanel.NAME:
			panel = new UnaryOperatorFilterPanel<>(filters.get(split[2]));
			break;
		case AbsoluteOperatorFilterPanel.TRUE_NAME:
			panel = new AbsoluteOperatorFilterPanel<>(true );
			break;
		case AbsoluteOperatorFilterPanel.FALSE_NAME:
			panel = new AbsoluteOperatorFilterPanel<>(false );
			break;
		case RoundFilterPanel.NAME:
			panel = new RoundFilterPanel(Boolean.parseBoolean(split[2]), Boolean.parseBoolean(split[3]));
			break;
		case NoFilterPanel.NAME:
			panel = new NoFilterPanel<Season, Game>();
			break;
		case SeasonFilterPanel.NAME:
			panel = FilterPanelAdapter.getFirstArgAdapter(new SeasonFilterPanel(split[2], Integer.parseInt(split[3])) );
			break;
		case SingleLeagueFilterPanel.NAME:
			panel = FilterPanelAdapter.getFirstArgAdapter(new SingleLeagueFilterPanel(split[2], Boolean.parseBoolean(split[3])) );
			break;
		case MatchDayFilterPanel.NAME:
			panel = FilterPanelAdapter.getSecondArgAdapter(new MatchDayFilterPanel(split[2], Integer.parseInt(split[3])) );
			break;
		case GoalFilterPanel.NAME_GOAL:
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getGoalFilterPanel(split[2], Integer.parseInt(split[3])) );
			break;
		case GoalFilterPanel.NAME_HOME_GOAL:
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getHomeGoalFilterPanel(split[2], Integer.parseInt(split[3])) );
			break;
		case GoalFilterPanel.NAME_AWAY_GOAL:
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getAwayGoalFilterPanel(split[2], Integer.parseInt(split[3])) );
			break;
		case GoalFilterPanel.NAME_GOAL_DIFF:
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getGoalDiffFilterPanel(split[2], Integer.parseInt(split[3])) );
			break;
		case TeamFilterPanel.NAME:
			panel = FilterPanelAdapter.getSecondArgAdapter(new TeamFilterPanel(split[2], Boolean.parseBoolean(split[3]), Boolean.parseBoolean(split[4])) );
			break;
		case TeamSearchFilterPanel.NAME:
			panel = FilterPanelAdapter.getSecondArgAdapter(new TeamSearchFilterPanel(split[2], Boolean.parseBoolean(split[3])) );
			break;
		case SubLeagueFilterPanel.NAME:
			panel = FilterPanelAdapter.getSecondArgAdapter(new SubLeagueFilterPanel(Arrays.asList(split).subList(2, split.length)) );
			break;
		case DateFilterPanel.NAME:
			try
			{
				panel = FilterPanelAdapter.getSecondArgAdapter(new DateFilterPanel(split[2], Game.DATE_FORMAT.parse(split[3])) );
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			break;
		case DayOfWeekFilterPanel.NAME:
			panel = FilterPanelAdapter.getSecondArgAdapter(new DayOfWeekFilterPanel(split[2]));
			break;
		default:
			System.out.println("Unbekannter Filter:" + in);
		}
		FilterMenuFactory.createPopupMenu(panel);
		filters.put(split[0], panel);
		panel.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}
}
