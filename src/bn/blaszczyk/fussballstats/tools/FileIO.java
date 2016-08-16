package bn.blaszczyk.fussballstats.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	private static final String LEAGUES_FILE = "data/leagues.dat";
	
	public static List<League> initLeagues()
	{
		List<League> leagues = new ArrayList<>();
		Scanner scanner = new Scanner( FussballStats.class.getResourceAsStream(LEAGUES_FILE) );
		while(scanner.hasNextLine())
		{
			String props[] = scanner.nextLine().split(";");
			if(props[0].startsWith("//"))
				continue;
			if(props.length < 3)
				break;
			int[] yearBounds = new int[props.length - 3];
			for(int i = 0; i < yearBounds.length; i++)
				yearBounds[i] = Integer.parseInt( props[i+3].trim() );
			
			League league = new League(props[0].trim(), props[1].trim(),props[2].trim(), yearBounds);
			leagues.add( league );
		}
		scanner.close();
		return leagues;
	}
	
	public static void loadLeagues(Iterable<League> leagues)
	{
		for(League league : leagues)
			loadSeasons(league);
	}
	
	public static void saveSeason(Season season) throws BLException
	{
		if(season == null)
			return;
		String filename = getFileName(season);
		try(FileWriter file = new FileWriter(filename))
		{
			for(Game game : season.getAllGames())
				file.write(game.toString() + "\n");
		}
		catch (IOException e)
		{
			throw new BLException("Error writing " + filename,e);
		}
	}
	
	public static boolean isSeasonSaved(Season season)
	{
		return new File(getFileName(season)).exists();
	}
	

	private static void loadSeasons(League league)
	{
		File directory = new File(BASE_FOLDER + "/" + league.getPathName() + "/");
		if(!directory.exists())
			directory.mkdirs();
		for(Season season : league)
 			try
			{
				loadSeason(season);
			}
			catch (BLException e)
			{
				//TODO: NotifyUser
				e.printStackTrace();
			}
	}
	
	private static boolean loadSeason(Season season) throws BLException
	{
		if(season == null)
			return false;
		String file = getFileName(season);
		if(!isSeasonSaved(season))
			return false;
		try(Scanner scanner = new Scanner(new FileInputStream(file)))
		{
			Stack<Game> gameStack = new Stack<>();
			while (scanner.hasNextLine())
				gameStack.push(new Game( scanner.nextLine() ));
			season.consumeGames( gameStack );
			return true;
		}
		catch (FileNotFoundException e)
		{
			throw new BLException("Error loading " + file, e );
		}
		catch(NumberFormatException e)
		{
			throw new BLException("Wrong Filename " + file, e );
		}
	}
	
	private static String getFileName(Season season)
	{
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, season.getLeague().getPathName(),season.getYear(),FILE_EXTENSION);
	}
}
