package bn.blaszczyk.blstatistics.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.*;
import bn.blaszczyk.blstatistics.tools.BLException;
import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;

public class GUITests {
	
	
	public static void printTotalTable(League league)
	{
		List<Game> allGames = new ArrayList<>();
		for(Season s : league)
			for(MatchDay m: s)
				for( Game g: m)
					allGames.add(g);
		Table table = new Table(allGames);
		openTable(table,"Ewige Tabelle");
	}

	public static void printLeagueTable(League league, int year)
	{
		Season season;
		try
		{
			season = league.getSeason(year);
			Table table = new Table( season.getAllGames() );
			openTable(table, league.getName() + " " + year);
		}
		catch (BLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static ResultTable openTable( Table table, String title )
	{
		JFrame frame = new JFrame(title);
		table.sort();
		ResultTable resultTable = new ResultTable(table);
		frame.add(new JScrollPane(resultTable));
//		frame.setPreferredSize(new Dimension(1000,1000));
		frame.pack();
		frame.setVisible(true);
		return resultTable;
	}



	public static void openFilteredGameTable( List<League> leagues, String title )
	{
		JFrame frame = new JFrame(title);
		frame.add( new FilteredGameTable(leagues));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	
	public static void main(String[] args)
	{
//		FussballDatenRequest.setMutedErrStream(true);
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
		League[] leagues = {bundesliga};
		openFilteredGameTable(Arrays.asList(leagues),"Bundesliga Spiele");
	}
	
}
