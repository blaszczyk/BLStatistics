package bn.blaszczyk.blstatistics.tools;

import java.awt.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
	private StringBuilder outerBuilder;
	private int panelCount;
	private Map<String, BiFilterPanel<Season, Game>> filters;

	private List<String> teams;
	private List<String> leagues;
	
	private FilterPanelManager<Season,Game> manager;
	
	public FilterParser(GameFilterPanelManager manager)
	{
		this.manager = manager;
		teams = manager.getTeams();
		leagues = manager.getLeagues();
	}

	public String writeFilter(BiFilterPanel<Season, Game> filter)
	{
		outerBuilder = new StringBuilder();
		panelCount = 0;
		writeSubFilter(filter);
		return outerBuilder.toString();
	}
	
	public BiFilterPanel<Season, Game> parseFilter(String in)
	{
		InputStream stream = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8));
		return parseFilter(stream);
	}

	public BiFilterPanel<Season, Game> parseFilter(InputStream iStream)
	{
		filters = new HashMap<>();
		BiFilterPanel<Season, Game> lastPanel = new NoFilterPanel<>(manager);
		Scanner scanner = new Scanner(iStream);
		while(scanner.hasNextLine())
			lastPanel = parseSubFilter(scanner.nextLine());
		scanner.close();
		return lastPanel;
	}
	
	private int writeSubFilter(BiFilterPanel<Season, Game> filter)
	{
		StringBuilder innerBuilder = new StringBuilder();
		if (filter instanceof MultiOperatorFilterPanel)
		{
			MultiOperatorFilterPanel<Season, Game> mFilter = (MultiOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append("MultiOperator;" + mFilter.getOperator());
			for (BiFilterPanel<Season, Game> p : mFilter)
				innerBuilder.append(";F" + writeSubFilter(p));
		}
		else if (filter instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> iteFilter = (IfThenElseFilterPanel<Season, Game>) filter;
			int ifInt = writeSubFilter(iteFilter.getIfFilter());
			int thenInt = writeSubFilter(iteFilter.getThenFilter());
			int elseInt = writeSubFilter(iteFilter.getElseFilter());
			innerBuilder.append(String.format("IfThenElse;F%d;F%d;F%d",ifInt, thenInt, elseInt));
		}
		else if (filter instanceof UnaryOperatorFilterPanel)
		{
			UnaryOperatorFilterPanel<Season, Game> uFilter = (UnaryOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append("UnaryOperator;F" + writeSubFilter(uFilter.getInnerPanel()));
		}
		else if (filter instanceof AbsoluteOperatorFilterPanel)
		{
			AbsoluteOperatorFilterPanel<Season, Game> aFilter = (AbsoluteOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append(aFilter.toString());
		}
		else if (filter instanceof RoundFilterPanel)
		{
			RoundFilterPanel rFilter = (RoundFilterPanel) filter;
			innerBuilder.append("Runde;" + rFilter.isFirstRound() + ";" + rFilter.isSecondRound());
		}
		else if (filter instanceof NoFilterPanel)
		{
			innerBuilder.append("NoFilter");
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
				innerBuilder.append( "Liga;" + iPanel.getSelectedLeague() + ";" + iPanel.isRecursive());
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
				innerBuilder.append("Team;" + tFilter.getTeam() + ";" + tFilter.getHome() + ";" + tFilter.getAway());
			}
			else if (gFilter instanceof SubLeagueFilterPanel)
			{
				SubLeagueFilterPanel slFilter = (SubLeagueFilterPanel) gFilter;
				innerBuilder.append("DirekterVergleich");
				for (int i = 0; i < slFilter.getTeamCount(); i++)
					innerBuilder.append(";" + slFilter.getTeam(i));
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


	private BiFilterPanel<Season, Game> parseSubFilter(String in)
	{
		String[] split = in.split(";");
		BiFilterPanel<Season, Game> panel = null;
		if (split.length < 2)
			return null;
		switch (split[1])
		{
		case "MultiOperator":
			List<BiFilterPanel<Season, Game>> pList = new ArrayList<>();
			for (int i = 3; i < split.length; i++)
				pList.add(filters.get(split[i]));
			panel = new MultiOperatorFilterPanel<>(manager, pList, split[2]);
			break;
		case "IfThenElse":
			panel = new IfThenElseFilterPanel<>(manager, filters.get(split[2]), filters.get(split[3]), filters.get(split[4]));
			break;
		case "UnaryOperator":
			panel = new UnaryOperatorFilterPanel<>(manager, filters.get(split[2]));
			break;
		case "TRUE":
			panel = new AbsoluteOperatorFilterPanel<>(true, manager);
			break;
		case "FALSE":
			panel = new AbsoluteOperatorFilterPanel<>(false, manager);
			break;
		case "Runde":
			panel = new RoundFilterPanel(manager, Boolean.parseBoolean(split[2]), Boolean.parseBoolean(split[3]));
			break;
		case "NoFilter":
			panel = new NoFilterPanel<Season, Game>(manager);
			break;
		case "Saison":
			panel = FilterPanelAdapter.getFirstArgAdapter(new SeasonFilterPanel(split[2], Integer.parseInt(split[3])), manager);
			break;
		case "Liga":
			panel = FilterPanelAdapter.getFirstArgAdapter(new SingleLeagueFilterPanel(leagues, split[2], Boolean.parseBoolean(split[3])), manager);
			break;
		case "Spieltag":
			panel = FilterPanelAdapter.getSecondArgAdapter(new MatchDayFilterPanel(split[2], Integer.parseInt(split[3])), manager);
			break;
		case "Tore":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getGoalFilterPanel(split[2], Integer.parseInt(split[3])), manager);
			break;
		case "Heimtore":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getHomeGoalFilterPanel(split[2], Integer.parseInt(split[3])), manager);
			break;
		case "Auswärtstore":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getAwayGoalFilterPanel(split[2], Integer.parseInt(split[3])), manager);
			break;
		case "Tordifferenz":
			panel = FilterPanelAdapter.getSecondArgAdapter(GoalFilterPanel.getGoalDiffFilterPanel(split[2], Integer.parseInt(split[3])), manager);
			break;
		case "Team":
			panel = FilterPanelAdapter.getSecondArgAdapter(new TeamFilterPanel(teams, split[2], Boolean.parseBoolean(split[3]), Boolean.parseBoolean(split[4])), manager);
			break;
		case "DirekterVergleich":
			panel = FilterPanelAdapter.getSecondArgAdapter(new SubLeagueFilterPanel(teams, Arrays.asList(split).subList(2, split.length)), manager);
			break;
		default:
			System.out.println("Unbekannter Filter:" + in);
		}
		filters.put(split[0], panel);
		panel.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}
}
