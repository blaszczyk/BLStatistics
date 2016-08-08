package bn.blaszczyk.blstatistics.tools;

import java.util.*;

import bn.blaszczyk.blstatistics.BLStatistics;


public class TeamAlias
{
	private static final String ALIAS_FILE = "data/teamAliases.dat";
	
	private static Map<String,String> aliasMap = new HashMap<>();
	
//	private static  List<String> aliasList = new ArrayList<>();
	
	public static void loadAliases()
	{
		Scanner scanner = new Scanner( BLStatistics.class.getResourceAsStream(ALIAS_FILE) );
		while(scanner.hasNextLine())
		{
			String team[] = scanner.nextLine().split(";");
			if(team[0].startsWith("/") || team.length < 2)
				continue;
			aliasMap.put(team[0].trim(), team[1].trim());
//			aliasList.add(team[1].trim());
		}
		scanner.close();
	}

	public static String getAlias(String team)
	{
		if(aliasMap.containsKey(team))
			return aliasMap.get(team);
//		if(aliasList.contains(team))
//			return team;
//		aliasList.add(team);
		return team;
	}
}
