package bn.blaszczyk.blstatistics.controller;

import java.io.File;
import java.util.Stack;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.tools.BLException;
import bn.blaszczyk.blstatistics.tools.FileIO;
import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;

public class BasicController {
	
	private League league;
	public BasicController(League league)
	{
		this.league = league;
	}
	
	public void requestSeason(int year)
	{		
		try
		{
			FussballDatenRequest.requestTableMuted(year,league.getName());
			Stack<Game> gameStack = FussballDatenRequest.getGames();
			FussballDatenRequest.clearTable();
			Season season = new Season(year,league);
			season.addGames(gameStack);
			league.addSeason(season);
		}
		catch (BLException e)
		{
			e.printStackTrace();
		}
	}

	public void downloadAllSeasons()
	{
		for(int year = 1964; year < 2017; year++)
		{
			requestSeason(year);
			try
			{
				FileIO.saveSeason(league, year);
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
		}		
	}

	public void loadAllSeasons()
	{
		File directory = new File("leagues/" + league.getName() + "/");
		for(File file : directory.listFiles())
			try
			{
				FileIO.loadSeason(league, file);
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
	}
	
	public void saveAllSeasons()
	{
		for(Season season : league)
			try
			{
				FileIO.saveSeason(league, season.getYear());
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
	}
	
}
