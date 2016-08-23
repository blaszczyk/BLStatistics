package bn.blaszczyk.fussballstats.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import bn.blaszczyk.fussballstats.core.*;

public class FileIO
{
	private static final String BASE_FOLDER = "leagues";
	private static final String FILE_EXTENSION = "bls";

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
	
	public static void loadLeagues(Iterable<League> leagues) throws FussballException
	{
		for(League league : leagues)
			loadSeasons(league);
	}
	
	public static void saveSeason(Season season) throws FussballException
	{
		if(season == null)
			return;
		File directory = new File(BASE_FOLDER + "/" + season.getLeague().getPathName() + "/");
		if(!directory.exists())
			directory.mkdirs();
		String filename = getFileName(season);
		try(FileWriter file = new FileWriter(filename))
		{
			for(Game game : season)
			{
				String gameString = String.format("%d;%s;%s;%s;%d;%d\n", game.getMatchDay(), DATE_FORMAT.format(game.getDate()),
						game.getTeamH(), game.getTeamA(), game.getGoalsH(), game.getGoalsA() );
				file.write(gameString);
			}
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
	

	private static void loadSeasons(League league) throws FussballException
	{
		File directory = new File(BASE_FOLDER + "/" + league.getPathName() + "/");
		if(!directory.exists())
			return;
		for(Season season : league)
			loadSeason(season);
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
			{
				String line = scanner.nextLine();
				String[] split = line.split(";");
				try
				{
					int matchDay = Integer.parseInt(split[0].trim());
					Date date = DATE_FORMAT.parse(split[1].trim());
					String teamH = split[2].trim();
					String teamA = split[3].trim();
					int goalsH = Integer.parseInt(split[4].trim());
					int goalsA = Integer.parseInt(split[5].trim());
					games.add(new Game(matchDay, date, teamH, teamA, goalsH, goalsA));
				}
				catch(NumberFormatException | IndexOutOfBoundsException | ParseException e)
				{
					System.err.println("Falsches Spiel Format: " + line);
					e.printStackTrace();
				}
			}
			season.setGames( games );
			return true;
		}
		catch (FileNotFoundException e)
		{
			throw new FussballException("Fehler beim Laden der Ligen", e);
		}
	}
	
	private static String getFileName(Season season)
	{
		return String.format("%s/%s/%4d.%s", BASE_FOLDER, season.getLeague().getPathName(),season.getYear(),FILE_EXTENSION);
	}
}
