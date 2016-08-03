package bn.blaszczyk.blstatistics.tools;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.gui.corefilters.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class FilterIO
{
	private static final String FOLDER = "filters";
	private static final String EXTENSION = "flt";

	private StringBuilder outerBuilder;
	private int panelCount;
	private Map<String, BiFilterPanel<Season, Game>> filters;
	private FilterPanelManager<Season, Game> manager = null;

	private List<String> teams;

	public FilterIO()
	{
	}

	public void setManager(GameFilterPanelManager manager)
	{
		this.manager = manager;
		teams = manager.getTeams();
	}

	public void saveFilter(BiFilterPanel<Season, Game> filter)
	{
		if (filter == null || filter instanceof BlankFilterPanel)
		{
			JOptionPane.showMessageDialog(null, "No Filter to save.", "Save Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		outerBuilder = new StringBuilder();
		panelCount = 0;
		saveSubFilter(filter);
		String name = null;
		while (name == null || name == "")
			name = JOptionPane.showInputDialog(null, "Namen für den Filter eingeben:", "Filter Speichern", JOptionPane.QUESTION_MESSAGE);
		String path = String.format("%s/%s.%s", FOLDER, name, EXTENSION);
		try
		{
			FileWriter file = new FileWriter(path);
			file.write(outerBuilder.toString());
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private int saveSubFilter(BiFilterPanel<Season, Game> filter)
	{
		StringBuilder innerBuilder = new StringBuilder();
		innerBuilder.append("F" + (++panelCount) + ";");
		if (filter instanceof MultiOperatorFilterPanel)
		{
			MultiOperatorFilterPanel<Season, Game> mFilter = (MultiOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append("MultiOperator;" + mFilter.getOperator());
			for (BiFilterPanel<Season, Game> p : mFilter)
				innerBuilder.append(";F" + saveSubFilter(p));
		}
		else if (filter instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> iteFilter = (IfThenElseFilterPanel<Season, Game>) filter;
			int ifInt = saveSubFilter(iteFilter.getIfFilter());
			int thenInt = saveSubFilter(iteFilter.getThenFilter());
			int elseInt = saveSubFilter(iteFilter.getElseFilter());
			innerBuilder.append("IfThenElse;F" + ifInt + ";F" + thenInt + ";F" + elseInt);
		}
		else if (filter instanceof UnaryOperatorFilterPanel)
		{
			UnaryOperatorFilterPanel<Season, Game> uFilter = (UnaryOperatorFilterPanel<Season, Game>) filter;
			innerBuilder.append("UnaryOperator;F" + saveSubFilter(uFilter.getInnerPanel()));
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
		else if (filter instanceof FilterPanelAdapter.FirstArgAdapter)
		{
			FilterPanel<Season> sFilter = ((FilterPanelAdapter.FirstArgAdapter<Season, Game>) filter).getInnerPanel();
			if (sFilter instanceof IntegerValueFilterPanel)
			{
				IntegerValueFilterPanel<Season> iPanel = (IntegerValueFilterPanel<Season>) sFilter;
				innerBuilder.append(iPanel.getLabel() + ";" + iPanel.getSelectedOperator() + ";" + iPanel.getReferenceInt());
			}
			else
			{
				System.err.println("Unknown Filter" + sFilter);
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
			else if (gFilter instanceof DuelFilterPanel)
			{
				DuelFilterPanel dFilter = (DuelFilterPanel) gFilter;
				innerBuilder.append("Duell;" + dFilter.getTeam1() + ";" + dFilter.getTeam2());
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
				System.err.println("Unknown Filter" + gFilter);
			}
		}
		else
		{
			System.err.println("Unknown Filter" + filter);
		}
		innerBuilder.append("\n");
		outerBuilder.append(innerBuilder.toString());
		return panelCount;
	}

	public BiFilterPanel<Season, Game> loadFilter()
	{
		File workingDirectory = new File(System.getProperty("user.dir") + "/" + FOLDER);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Filter (*.flt)", "flt"));
		fileChooser.setDialogTitle("Filter laden");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(workingDirectory);
		
		switch (fileChooser.showOpenDialog(null))
		{
		case JFileChooser.APPROVE_OPTION:
			return loadFilter(fileChooser.getSelectedFile());
		case JFileChooser.ERROR_OPTION:
			JOptionPane.showMessageDialog(fileChooser, "Kein Filter geladen.", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private BiFilterPanel<Season, Game> loadFilter(File file)
	{
		filters = new HashMap<>();
		BiFilterPanel<Season, Game> lastPanel = new BlankFilterPanel<>(manager);
		try
		{
			Scanner scanner = new Scanner(new FileInputStream(file));
			while(scanner.hasNextLine())
				lastPanel = loadSubFilter(scanner.nextLine());
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return lastPanel;
	}

	private BiFilterPanel<Season, Game> loadSubFilter(String in)
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
		case "Saison":
			panel = FilterPanelAdapter.getFirstArgAdapter(new SeasonFilterPanel(split[2], Integer.parseInt(split[3])), manager);
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
		case "Duell":
			panel = FilterPanelAdapter.getSecondArgAdapter(new DuelFilterPanel(teams, split[2], split[3]), manager);
			break;
		case "DirekterVergleich":
			panel = FilterPanelAdapter.getSecondArgAdapter(new SubLeagueFilterPanel(teams, Arrays.asList(split).subList(2, split.length)), manager);
			break;
		}
		filters.put(split[0], panel);
		panel.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}

}
