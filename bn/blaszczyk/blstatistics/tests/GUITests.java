package bn.blaszczyk.blstatistics.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.*;
import bn.blaszczyk.blstatistics.gui.*;
import bn.blaszczyk.blstatistics.gui.filters.*;
import bn.blaszczyk.blstatistics.tools.BLException;

public class GUITests {
	
	private static Filter<Game> gameFilter = LogicalFilter.getTRUEFilter();
	private static BiFilter<TeamResult,Game> teamResultFilter = LogicalBiFilter.getTRUEBiFilter();
	
	public static void printTotalTable(League league)
	{
		List<Game> allGames = new ArrayList<>();
		for(Season s : league)
			for(MatchDay m: s)
				for( Game g: m)
					allGames.add(g);
		Table table = new Table(allGames,gameFilter,teamResultFilter);
		openTable(table,"Ewige Tabelle");
	}

	public static void printLeagueTable(League league, int year)
	{
		Season season;
		try
		{
			season = league.getSeason(year);
			Table table = new Table( season.getAllGames(), gameFilter );
			openTable(table, league.getName() + " " + year);
		}
		catch (BLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void printAllTables(League league)
	{
		for(Season season: league)
			printLeagueTable(league, season.getYear());
	}

	
	public static ResultTable openTable( Table table, String title )
	{
		JFrame frame = new JFrame(title);
		table.sort();
		ResultTable resultTable = new ResultTable(table);
		frame.add(new JScrollPane(resultTable));
//		frame.setPreferredSize(new Dimension(1000,1000));
		frame.pack();
		frame.setVisible(true);
		return resultTable;
	}
	
	public static void gameFilterTest(League league)
	{
		Filter<Game> koeln = GameFilter.getTeamFilter("FC Köln");
		setGameFilter(koeln);
		printTotalTable(league);
		
//		Filter<Game> koelnwins = GameFilter.getTeamResultFilter("FC Köln",Game.WIN);
//		setGameFilter(koelnwins);
//		printTotalTable(league);
		
	}

	public static GameTable openGameTable( Iterable<Game> games, String title )
	{
		JFrame frame = new JFrame(title);
		GameTable gameTable = new GameTable(games);
		frame.add(new JScrollPane(gameTable));
//		frame.setPreferredSize(new Dimension(1000,1000));
		frame.pack();
		frame.setVisible(true);
		return gameTable;
	}

	public static DuelFilterPanel openDuelPanel( Iterable<String> teams, String title )
	{
		JFrame frame = new JFrame(title);
		DuelFilterPanel panel = new DuelFilterPanel(teams);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		return panel;
	}

	public static TeamFilterPanel openTeamPanel( Iterable<String> teams, String title )
	{
		JFrame frame = new JFrame(title);
		TeamFilterPanel panel = new TeamFilterPanel(teams);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		return panel;
	}


	public static void openFilteredGameTable( List<League> leagues, String title )
	{
		JFrame frame = new JFrame(title);
		frame.add( new FilteredGameTable(leagues));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	

	public static void setGameFilter(Filter<Game> gameFilter)
	{
		GUITests.gameFilter = gameFilter;
	}
	
	public static void setTeamResultFilter(BiFilter<TeamResult,Game> teamResultFilter)
	{
		GUITests.teamResultFilter = teamResultFilter;
	}

	public static void duelFilterTest(League league)
	{
		Filter<Game> cgn_lev = GameFilter.getDuelFilter("FC Köln", "Leverkusen");
		setGameFilter(cgn_lev);
		List<Game> games = GameFilter.filterGameList( league.getAllGames(), cgn_lev );
		games.sort(Game.DUEL_COMPARATOR);
		openGameTable(games,"Duelle Köln lev");
		printTotalTable(league);
	}
	
	
	
	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
		League[] leagues = {bundesliga};
		openFilteredGameTable(Arrays.asList(leagues),"Bundesliga Spiele");
	}
	
}
