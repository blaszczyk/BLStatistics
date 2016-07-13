package bn.blaszczyk.blstatistics.controller;

import java.io.File;
import java.util.Stack;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;

public class BasicController {
	
	private League league;
	public BasicController(League league)
	{
		this.league = league;
	}
	
	public void requestSeason(int year)
	{		
		FussballDatenRequest.requestTableMuted(year,league.getName());
		Stack<String> gameStack = FussballDatenRequest.getGames();
		FussballDatenRequest.clearTable();
		
		Season season = new Season(year);
		season.consumeGameStack(gameStack);
		league.addSeason(season);
	}

	public void downloadAllSeasons()
	{
		for(int year = 1969; year < 2017; year++)
		{
			requestSeason(year);
			league.saveToFile(year);
		}		
	}

	public void loadAllSeasons()
	{
		File directory = new File("leagues/" + league.getName() + "/");
		for(File file : directory.listFiles())
			league.loadFromFile(file);
	}
	
	public void saveAllSeasons()
	{
		for(Season season : league)
			league.saveToFile(season.getYear());
	}
	
}
