package bn.blaszczyk.fussballstats.tools;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private final Map<String,String> aliases = new HashMap<>();
	
	public TeamAlias()
	{
		Path aliasDataPath;
		try
		{
			aliasDataPath = Paths.get(FussballStats.class.getResource(ALIAS_FILE).toURI());
			Files.lines(aliasDataPath)
				.map(l -> l.split(";"))
				.filter(s -> s.length > 1)
				.forEach(s -> aliases.put(s[0].trim(), s[1].trim()));
		}
		catch (URISyntaxException | IOException e)
		{
			throw new FussballException("error loading alias data", e);
		}
	}

	public String getTeamName(final String team)
	{
		if(aliases.containsKey(team))
			return aliases.get(team);
		return team;
	}
}
