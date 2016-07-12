package bn.blaszczyk.blstatistics;

import java.util.*;

import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;

public class BLStatistics {

	public static void main(String[] args) {
		
		System.out.println("BLStatistics");
		FussballDatenRequest.loadPage(2016);
		
		System.out.println("Teams:");
		List<String> teams = FussballDatenRequest.getTeams();
		for( String team : teams)
			System.out.println(team);
		
		System.out.println("Spiele:");
		Stack<String> gamesStack = FussballDatenRequest.getGames();
		while(!gamesStack.isEmpty())
			System.out.println(gamesStack.pop());
	}

}
