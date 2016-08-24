package bn.blaszczyk.fussballstats.tools;

import java.util.*;

import bn.blaszczyk.fussballstats.FussballStats;


public class TeamAlias
{
	/*
	 * Resource
	 */
	private static final String ALIAS_FILE = "data/teamAliases.dat";
	
	/*
	 * Variables
	 */
	private static Map<String,String> aliasMap = new HashMap<>();
	private static boolean useAliases;
	
	/*
	 * Init
	 */
	public static void loadAliases()
	{
		Scanner scanner = new Scanner( FussballStats.class.getResourceAsStream(ALIAS_FILE) );
		while(scanner.hasNextLine())
		{
			String team[] = scanner.nextLine().split(";");
			if(team[0].startsWith("/") || team.length < 2)
				continue;
			aliasMap.put(team[0].trim(), team[1].trim());
		}
		scanner.close();
	}

	/*
	 * Global Getters, Setters
	 */
	public static String getAlias(String team)
	{
		if(useAliases && aliasMap.containsKey(team))
			return aliasMap.get(team);
		return team;
	}

	public static boolean isUseAliases()
	{
		return useAliases;
	}

	public static void setUseAliases(boolean useAliases)
	{
		TeamAlias.useAliases = useAliases;
	}
}
