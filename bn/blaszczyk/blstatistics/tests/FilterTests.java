package bn.blaszczyk.blstatistics.tests;

import java.util.List;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.League;
import bn.blaszczyk.blstatistics.core.TeamResult;
import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.TeamResultFilterFactory;

public class FilterTests
{
	public static void gameFilterTest(League league)
	{
		Filter<Game> koeln = GameFilter.getTeamFilter("FC Köln");
		ConsoleTests.setGameFilter(koeln);
		System.out.println("");
		System.out.println("Bilanz gegen 1.FC Köln");
		ConsoleTests.printTotalTable(league);
		
		Filter<Game> koelnwins = GameFilter.getTeamResultFilter("FC Köln",Game.WIN);
		ConsoleTests.setGameFilter(koelnwins);
		System.out.println();
		System.out.println("Siegesbilanz gegen 1.FC Köln");
		ConsoleTests.printTotalTable(league);
		
	}
	
	public static void teamResultFilterTest(League league)
	{
		BiFilter<TeamResult,Game> home = TeamResultFilterFactory.getHomeGameFilter();
		BiFilter<TeamResult,Game> away = TeamResultFilterFactory.getAwayGameFilter();
		
		System.out.println("\nGesamtTabelle:");
		ConsoleTests.printTotalTable(league);
		
		ConsoleTests.setTeamResultFilter(home);
		System.out.println("\nHeimTabelle:");
		ConsoleTests.printTotalTable(league);
		
		ConsoleTests.setTeamResultFilter(away);
		System.out.println("\nAuswärtsTabelle:");
		ConsoleTests.printTotalTable(league);
	}
	
	public static void duelFilterTest(League league)
	{
		Filter<Game> cgn_lev = GameFilter.getDuelFilter("FC Köln", "Leverkusen");
		ConsoleTests.setGameFilter(cgn_lev);
		List<Game> games = Game.filterGameList( league.getAllGames(), cgn_lev );
		games.sort(Game.DUEL_COMPARATOR);
		ConsoleTests.printGames(games);
		ConsoleTests.printTotalTable(league);
	}
	
	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
//		FilterTests.duelFilterTest(bundesliga);
//		FilterTests.gameFilterTest(bundesliga);
		FilterTests.teamResultFilterTest(bundesliga);
	}


}
