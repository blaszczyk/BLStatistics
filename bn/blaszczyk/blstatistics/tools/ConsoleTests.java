package bn.blaszczyk.blstatistics.tools;

import java.util.*;

import bn.blaszczyk.blstatistics.core.*;

public class ConsoleTests
{
	
	private static League bundesliga = new League("bundesliga");

	public static void requestTest()
	{
		
		FussballDatenRequest.requestTableMuted(1964);
		
		System.out.println("Teams:");
		List<String> teams = FussballDatenRequest.getTeams();
		for (String team : teams)
			System.out.println(team);
			
		System.out.println("Spiele:");
		Stack<String> gamesStack = FussballDatenRequest.getGames();
		while (!gamesStack.isEmpty())
			System.out.println(gamesStack.pop());
		FussballDatenRequest.clearTable();

	}
	
	private static void generateTestLeague()
	{
		List<String> teams = new ArrayList<>();
		teams.add("Duisburg");
		teams.add("Frankfurt");
		teams.add("Dortmund");
		teams.add("Hamburg");
		
		Stack<String> games = new Stack<>();
		games.push("1. Spieltag 31.08.63: Duisburg - Frankfurt 3:1");
		games.push("3. Spieltag 23.11.63: Duisburg - Dortmund 3:3");
		games.push("5. Spieltag 09.11.63: Duisburg - Hamburg 4:0");
		
		games.push("4. Spieltag 08.02.64: Frankfurt - Duisburg 2:2");
		games.push("2. Spieltag 22.02.64: Frankfurt - Dortmund 2:1");
		games.push("6. Spieltag 30.03.64: Frankfurt - Hamburg 2:2");
		
		games.push("6. Spieltag 11.04.64: Dortmund - Duisburg 0:0");
		games.push("5. Spieltag 05.10.63: Dortmund - Frankfurt 3:0");
		games.push("1. Spieltag 05.05.64: Dortmund - Hamburg 5:2");
		
		games.push("2. Spieltag 21.03.64: Hamburg - Duisburg 3:3");
		games.push("3. Spieltag 14.09.63: Hamburg - Frankfurt 3:0");
		games.push("4. Spieltag 21.12.63: Hamburg - Dortmund 2:1");
		Season s1964 = new Season(1964, teams);
		while(!games.isEmpty())
			s1964.addGame(games.pop());
		bundesliga.addSeason(s1964);
	}

	public static void saveTest()
	{
		generateTestLeague();
		bundesliga.saveToFile(1964);
	}
	
	public static void loadTest()
	{
		bundesliga.loadFromFile(1964);
		printLeague(bundesliga);
	}
	
	public static void tableTest()
	{
		generateTestLeague();
		printLeague(bundesliga);
		Season s1964 = bundesliga.getSeason(1964);
		Table table = new Table( s1964.getAllGames() );
		System.out.println();
		printTable(table);
	}
	
	private static void printLeague( League league)
	{
		System.out.println("Ausdruck " + league.getName());
		for(Season s : league.getSeasons())
		{
			System.out.println( "  Saison: " + s.getYear() );
			System.out.println( "  " + s.getTeamCount() + " Teams");
			for(String team : s.getTeams())
				System.out.println("    " + team);
			System.out.println("  " + s.getMatchDayCount() + " Spieltage");
			for(int i = 0; i < s.getMatchDayCount(); i++)
			{
				System.out.println("    " + (i+1) + ". Spieltag");
				for(Game g : s.getMatchDay(i).getGames())
					System.out.println("      " + g);
			}
		}
	}
	
	private static void printTable( Table table )
	{
		System.out.println("Tabelle:");
		table.sort();
		for(int i = 0; i < table.getTeamCount(); i++)
			System.out.printf( "%2d. %s\n" , (i+1) ,table.getTeamResult(i).toString() );
	}

}
