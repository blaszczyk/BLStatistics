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

import bn.blaszczyk.blstatistics.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	private static final String LEAGUES_FILE = "leagues";
	
	public static List<League> loadLeagues()
	{
		String path = String.format("%s/%s.%s", BASE_FOLDER, LEAGUES_FILE, FILE_EXTENSION);
		List<League> leagues = new ArrayList<>();
		try
		{
			Scanner scanner = new Scanner( new FileInputStream(path) );
			while(scanner.hasNextLine())
			{
				String props[] = scanner.nextLine().split(";");
				if(props.length < 3)
					break;
				int[] yearBounds = new int[props.length - 2];
				for(int i = 0; i < yearBounds.length; i++)
					yearBounds[i] = Integer.parseInt( props[i+2].trim() );
				
				
				League league = new League(props[0].trim(), props[1].trim(), yearBounds);
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
		File directory = new File("leagues/" + league.getPathName() + "/");
		if(!directory.exists())
			directory.mkdirs();
		for(File file : directory.listFiles())
			try
			{
				loadSeason(league, file);
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
	}
	
	private static boolean loadSeason(League league, File file) throws BLException
	{
		if(league == null || file == null)
			return false;
		try
		{
			int year = Integer.parseInt(file.getName().substring(0,4));			
			Season season;
			try
			{
				season = league.getSeason(year);
			}
			catch(BLException e)
			{
				e.printStackTrace();
				return false;
			}
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
			throw new BLException("Error loading " + file.getPath(), e );
		}
		catch(NumberFormatException e)
		{
			throw new BLException("Wrong Filename " + file.getPath(), e );
		}
	}

//	private static boolean loadFromFile(League league, int year) throws BLException
//	{
//		File file = new File(getFileName(league,year));
//		return loadSeason(league, file);
//	}
	

	private static String getFileName(Season season)
	{
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, season.getLeague().getPathName(),season.getYear(),FILE_EXTENSION);
	}
}
