package bn.blaszczyk.blstatistics.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import bn.blaszczyk.blstatistics.BLStatistics;
import bn.blaszczyk.blstatistics.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	private static final String LEAGUES_FILE = "leagues";
	
	public static List<League> loadLeagues()
	{
		String path = String.format("%s/%s.%s", getPathName(), LEAGUES_FILE, FILE_EXTENSION);
		List<League> leagues = new ArrayList<>();
		try
		{
			Scanner scanner = new Scanner( new FileInputStream(path) );
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
				loadSeasons(league);
				leagues.add( league );
			}
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return leagues;
	}
	
	public static void saveAllSeasons(League league)
	{
		for(Season season : league)
			try
			{
				saveSeason(season);
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
	}
	
	public static void saveSeason(Season season) throws BLException
	{
		if(season == null)
			return;
		String filename = getFileName(season);
		try
		{
			FileWriter file = new FileWriter(filename);
			for(Game game : season.getAllGames())
				file.write(game.toString() + "\n");
			file.close();
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
		File directory = new File(getPathName() + "/" + league.getPathName() + "/");
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
		try
		{
			Stack<Game> gameStack = new Stack<>();
			Scanner scanner = new Scanner(new FileInputStream(file));
			while (scanner.hasNextLine())
				gameStack.push(new Game( scanner.nextLine() ));
			scanner.close();
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
	

	public static String getPathName()
	{
		return String.format("%s-%s", BASE_FOLDER, BLStatistics.getRequestSource());
	}
	
	private static String getFileName(Season season)
	{
		return String.format("%s/%s/%4d.%s", getPathName(), season.getLeague().getPathName(),season.getYear(),FILE_EXTENSION);
	}
}
