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
	
	private static String getFileName(League league, int year)
	{
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, league.getName(),year,FILE_EXTENSION);
	}
	
	public static void saveSeason(League league, int year) throws BLException
	{
		if(league == null)
			return;
		Season season = league.getSeason(year);
		if(season == null)
			return;
		String filename = getFileName(league,year);
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
	
	public static boolean loadSeason(League league, File file) throws BLException
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
				season = new Season(year,league);
				league.addSeason(season);
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

	public static boolean loadFromFile(League league, int year) throws BLException
	{
		File file = new File(getFileName(league,year));
		return loadSeason(league, file);
	}
}
