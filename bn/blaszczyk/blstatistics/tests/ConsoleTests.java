package bn.blaszczyk.blstatistics.tests;

import java.util.*;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.*;
import bn.blaszczyk.blstatistics.tools.BLException;

public class ConsoleTests
{
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
		printTable(table);
	}
	

	public static League generateTestLeague()
	{	
		Stack<String> strings = new Stack<>();
		
		strings.push("1. Spieltag 31.08.63: Duisburg - Frankfurt 3:1");
		strings.push("3. Spieltag 23.11.63: Duisburg - Dortmund 3:3");
		strings.push("5. Spieltag 09.11.63: Duisburg - Hamburg 4:0");
		
		strings.push("4. Spieltag 08.02.64: Frankfurt - Duisburg 2:2");
		strings.push("2. Spieltag 22.02.64: Frankfurt - Dortmund 2:1");
		strings.push("6. Spieltag 30.03.64: Frankfurt - Hamburg 2:2");
		
		strings.push("6. Spieltag 11.04.64: Dortmund - Duisburg 0:0");
		strings.push("5. Spieltag 05.10.63: Dortmund - Frankfurt 3:0");
		strings.push("1. Spieltag 05.05.64: Dortmund - Hamburg 5:2");
		
		strings.push("2. Spieltag 21.03.64: Hamburg - Duisburg 3:3");
		strings.push("3. Spieltag 14.09.63: Hamburg - Frankfurt 3:0");
		strings.push("4. Spieltag 21.12.63: Hamburg - Dortmund 2:1");
		
		Stack<Game> games = new Stack<>();
		while(!strings.isEmpty())
			try
			{
				games.push(new Game(strings.pop()));
			}
			catch (BLException e)
			{
				System.err.println(e.getErrorMessage());
				e.printStackTrace();
			}
		
		Season season = new Season(1964);
		season.addGames(games);
		League league = new League("testliga");
		league.addSeason(season);
		return league;
	}

	public static void printLeagueTable(League league, int year)
	{
		Season season = league.getSeason(year);
		Table table = new Table( season.getAllGames(), gameFilter );
		System.out.println(league.getName() + " Saison " + year);
		printTable(table);
		System.out.println();
	}
	
	public static void printAllTables(League league)
	{
		for(Season season: league)
			printLeagueTable(league, season.getYear());
	}

	
	public static void printLeague( League league)
	{
		System.out.println("Ausdruck " + league.getName());
		for(Season s : league)
		{
			System.out.println( "  Saison: " + s.getYear() );
			System.out.println( "  " + s.getTeamCount() + " Teams");
			for(String team : s.getTeams())
				System.out.println("    '" + team + "'");
			System.out.println("  " + s.getMatchDayCount() + " Spieltage");
			for(int i = 0; i < s.getMatchDayCount(); i++)
			{
				System.out.println("    " + (i+1) + ". Spieltag");
				for(Game g : s.getMatchDay(i))
					System.out.println("      " + g);
			}
		}
	}
	
	public static void printTable( Table table )
	{
		table.sort();
		for(int i = 0; i < table.getTeamCount(); i++)
			System.out.println( table.getTeamResult(i).toString() );
	}
	
	public static void printGames( Iterable<Game> games )
	{
		for(Game game : games)
			if(gameFilter.check(game))
				System.out.println(game);
	}
	
	

	public static void setGameFilter(Filter<Game> gameFilter)
	{
		ConsoleTests.gameFilter = gameFilter;
	}
	
	public static void setTeamResultFilter(BiFilter<TeamResult,Game> teamResultFilter)
	{
		ConsoleTests.teamResultFilter = teamResultFilter;
	}
	
	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
		printAllTables(bundesliga);
		printTotalTable(bundesliga);
	}

}
