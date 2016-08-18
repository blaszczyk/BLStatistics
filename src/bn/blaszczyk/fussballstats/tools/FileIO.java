package bn.blaszczyk.fussballstats.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bn.blaszczyk.fussballstats.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";
	
	public static void loadLeagues(Iterable<League> leagues)
	{
		for(League league : leagues)
			loadSeasons(league);
	}
	
	public static void saveSeason(Season season) throws FussballException
	{
		if(season == null)
			return;
		String filename = getFileName(season);
		try(FileWriter file = new FileWriter(filename))
		{
			for(Game game : season)
				file.write(game.toString() + "\n");
		}
		catch (IOException e)
		{
			throw new FussballException("Schreibfehler: " + filename,e);
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
			catch (FussballException e)
			{
				//TODO: NotifyUser ?
				e.printStackTrace();
			}
	}
	
	private static boolean loadSeason(Season season) throws FussballException
	{
		if(season == null || !isSeasonSaved(season))
			return false;
		String file = getFileName(season);
		try(Scanner scanner = new Scanner(new FileInputStream(file)))
		{
			List<Game> games = new ArrayList<>();
			while (scanner.hasNextLine())
				games.add(new Game( scanner.nextLine() ));
			season.setGames( games );
			return true;
		}
		catch (FileNotFoundException e)
		{
			throw new FussballException("Ladefehler " + file, e );
		}
	}
	
	private static String getFileName(Season season)
	{
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, season.getLeague().getPathName(),season.getYear(),FILE_EXTENSION);
	}
}
