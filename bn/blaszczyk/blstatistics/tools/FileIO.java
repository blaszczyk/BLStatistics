package bn.blaszczyk.blstatistics.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

import bn.blaszczyk.blstatistics.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	
	public static void loadAllSeasons(League league)
	{
		File directory = new File("leagues/" + league.getName() + "/");
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
			season.addGames( gameStack );
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
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, season.getLeague(),season.getYear(),FILE_EXTENSION);
	}
}
