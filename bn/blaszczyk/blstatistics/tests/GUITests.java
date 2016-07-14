package bn.blaszczyk.blstatistics.tests;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.*;
import bn.blaszczyk.blstatistics.gui.ResultTable;
import bn.blaszczyk.blstatistics.gui.ResultTableModel;

public class GUITests {
	
	private static Filter<Game> gameFilter = LogicalFilterFactory.getTRUEFilter();
	private static BiFilter<TeamResult,Game> teamResultFilter = LogicalBiFilterFactory.getTRUEBiFilter();
	
	public static void printTotalTable(League league)
	{
		List<Game> allGames = new ArrayList<>();
		for(Season s : league)
			for(MatchDay m: s)
				for( Game g: m)
					allGames.add(g);
		Table table = new Table(allGames,gameFilter,teamResultFilter);
		printTable(table,"Ewige Tabelle");
	}

	public static void printLeagueTable(League league, int year)
	{
		Season season = league.getSeason(year);
		Table table = new Table( season.getAllGames(), gameFilter );
		printTable(table, league.getName() + " " + year);
	}
	
	public static void printAllTables(League league)
	{
		for(Season season: league)
			printLeagueTable(league, season.getYear());
	}

	
	public static void printTable( Table table, String title )
	{
		JFrame frame = new JFrame(title);
		table.sort();
		JTable jtable = new ResultTable(table);
		frame.add(new JScrollPane(jtable));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setPreferredSize(new Dimension(1000,1000));
		frame.pack();
		frame.setVisible(true);
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
	
	public static void printGames( Iterable<Game> games )
	{
		for(Game game : games)
			if(gameFilter.check(game))
				System.out.println(game);
	}
	
	

	public static void setGameFilter(Filter<Game> gameFilter)
	{
		GUITests.gameFilter = gameFilter;
	}
	
	public static void setTeamResultFilter(BiFilter<TeamResult,Game> teamResultFilter)
	{
		GUITests.teamResultFilter = teamResultFilter;
	}
	
	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
		gameFilterTest(bundesliga);
//		printAllTables(bundesliga);
//  		printTotalTable(bundesliga);
	}
	public GUITests()
	{
		// TODO Auto-generated constructor stub
	}
	
}
