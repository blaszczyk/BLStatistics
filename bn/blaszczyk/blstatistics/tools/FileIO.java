package bn.blaszczyk.blstatistics.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import bn.blaszczyk.blstatistics.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	
	private static String getFileName(League league, int year)
	{
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, league.getName(),year,FILE_EXTENSION);
	}
	
	public static void saveSeason(League league, int year)
	{
		if(league == null)
			return;
		Season season = league.getSeason(year);
		if(season == null)
			return;
		try
		{
			FileWriter file = new FileWriter(getFileName(league,year));
			for(Game game : season.getAllGames())
				file.write(game.toString() + "\n");
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean loadSeason(League league, File file)
	{
		if(league == null || file == null)
			return false;
		try
		{
			int year = Integer.parseInt(file.getName().substring(0,4));
			Season season = league.getSeason(year);
			if(season == null)
			{
				season = new Season(year);
				league.addSeason(season);
			}
			LineIterator iterator = FileUtils.lineIterator(file);
			Stack<String> gameStack = new Stack<>();
			while(iterator.hasNext())
				gameStack.push(iterator.nextLine());
			season.addGames( gameStack );
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean loadFromFile(League league, int year)
	{
		File file = new File(getFileName(league,year));
		return loadSeason(league, file);
	}
}
