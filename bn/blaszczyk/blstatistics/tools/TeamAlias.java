package bn.blaszczyk.blstatistics.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class TeamAlias
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	private static final String LEAGUES_FILE = "teamAliases";
	
	private static Map<String,String> aliasMap = new HashMap<>();
	
	private static  List<String> aliasList = new ArrayList<>();
	
	public static void loadAliases()
	{
		String path = String.format("%s/%s.%s", BASE_FOLDER, LEAGUES_FILE, FILE_EXTENSION);
		try
		{
			Scanner scanner = new Scanner( new FileInputStream(path) );
			while(scanner.hasNextLine())
			{
				String team[] = scanner.nextLine().split(";");
				if(team[0].startsWith("/") || team.length < 2)
					continue;
				aliasMap.put(team[0].trim(), team[1].trim());
				aliasList.add(team[1].trim());
//				System.out.printf("%30s heißt jetzt %30s\n", team[0].trim() ,team[1].trim());
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static String getAlias(String team)
	{
		if(aliasMap.containsKey(team))
			return aliasMap.get(team);
//		if(aliasList.contains(team))
//			return team;
//		aliasList.add(team);
//		System.out.println(team);
		return team;
	}
}
