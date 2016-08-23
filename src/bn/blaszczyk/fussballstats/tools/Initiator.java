package bn.blaszczyk.fussballstats.tools;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.gui.LeagueManager;
import bn.blaszczyk.fussballstats.gui.PrefsDialog;
import bn.blaszczyk.fussballstats.gui.corefilters.SingleLeagueFilterPanel;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.ProgressDialog;

public class Initiator {
	private static final String	LEAGUES_FILE	= "data/leagues.dat";
	private static final String	ICON_FILE		= "data/icon.png";
	
	private static ProgressDialog progressDialog;
	
	public static boolean initAll(List<League> leagues)
	{
		List<String> uniqueLeagueNames = new ArrayList<>();

		initUIManager();
		
		initLeagues(leagues);
		int seasonCount = 0;
		for (League league : leagues)
			seasonCount += league.getSeasonCount();
		progressDialog = new ProgressDialog(null, seasonCount, "Initiiere FussballStats",
				Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)), true);
		progressDialog.showDialog();
		
		progressDialog.appendInfo("Lade Einstellungen");
		initPrefs();		
		
		boolean databaseExists = false;
		while(LeagueManager.isDbMode() && ! databaseExists)
		{
			try
			{
				progressDialog.appendInfo("\nVerbinde mit Datenbank");
				databaseExists = checkDB();
			}
			catch(FussballException e)
			{
				progressDialog.appendException(e);
				JOptionPane.showMessageDialog(progressDialog, e.getMessage(), "Keine Verbindung zur Datenbank", JOptionPane.ERROR_MESSAGE);
			}
			if(!databaseExists)
				new PrefsDialog(progressDialog).showDialog();
		}
		
		try
		{	
			if (LeagueManager.isDbMode())
			{
				DBTools.openMySQLDatabase();		
				for (League league : leagues)
				{
					
					if (!uniqueLeagueNames.contains(league.getName()))
					{
						uniqueLeagueNames.add(league.getName());
						progressDialog.appendInfo("\nLade Liga: " + league.getName());
					}
					if (DBTools.tableExists(league))
						for (Season season : league)
						{
							progressDialog.incrementValue();
							DBTools.loadSeason(season);
						}
				}
				
				progressDialog.appendInfo("\nSchliesse Verbindung zu Datenbank");
				DBConnection.closeConnection();
			}
			else
			{
				progressDialog.appendInfo("\nLade Ligen");
				FileIO.loadLeagues(leagues);
			}
		}
		catch (FussballException e)
		{
			progressDialog.appendException(e);
			progressDialog.setFinished();
			return false;
		}
		
		progressDialog.appendInfo("\nInitialisiere TeamAlias");
		TeamAlias.loadAliases();
				
		progressDialog.appendInfo("\nLade Listen");
		if(!initLists(leagues))
		{
			int reply = JOptionPane.showConfirmDialog(progressDialog, "Liga Manager öffnen?", 
					"Keine Spiele vorhanden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(reply == JOptionPane.YES_OPTION)
			{
				new LeagueManager(progressDialog, leagues).showDialog();
				initLists(leagues);
			}
		}
		
		progressDialog.disposeDialog();
		
		return true;
	}
	
	private static List<League> initLeagues(List<League> leagues)
	{
		Scanner scanner = new Scanner(FussballStats.class.getResourceAsStream(LEAGUES_FILE));
		while (scanner.hasNextLine())
		{
			String props[] = scanner.nextLine().split(";");
			if (props[0].startsWith("/"))
				continue;
			if (props.length < 3)
				break;
			int[] yearBounds = new int[props.length - 3];
			for (int i = 0; i < yearBounds.length; i++)
				yearBounds[i] = Integer.parseInt(props[i + 3].trim());
			
			League league = new League(props[0].trim(), props[1].trim(), props[2].trim(), yearBounds);
			leagues.add(league);
		}
		scanner.close();
		return leagues;
	}
	
	private static void initUIManager()
	{	
		Font plainFont = new Font("Arial", Font.PLAIN, 16);
		Font boldFont = new Font("Arial", Font.BOLD, 16);
		Font tableFont = new Font("Arial", Font.PLAIN, 14);
		
		UIManager.put("Table.font", tableFont);
		
		UIManager.put("TableHeader.font", boldFont);
		UIManager.put("Label.font", boldFont);
		UIManager.put("Button.font", boldFont);
		UIManager.put("ProgressBar.font", boldFont);
		UIManager.put("ObtionPane.buttonFont", boldFont);
		UIManager.put("CheckBox.font", boldFont);
		UIManager.put("MenuBar.font", boldFont);
		UIManager.put("Menu.font", boldFont);
		UIManager.put("MenuItem.font", boldFont);
		UIManager.put("ComboBox.font", boldFont);
		UIManager.put("PopupMenu.font", boldFont);
		
		UIManager.put("TextPane.font", plainFont);
		UIManager.put("OptionPane.messageFont", plainFont);
		UIManager.put("List.font", plainFont);
		UIManager.put("PopupMenu.font", plainFont);
		UIManager.put("TextField.font", plainFont);
		UIManager.put("RadioButton.font", boldFont);
		UIManager.put("RadioButtonMenuItem.font", boldFont);
		
	}
	
	private static boolean checkDB() throws FussballException
	{
		DBTools.connectToSQLServer();
		boolean exists = DBTools.databaseExists();
		if(!exists)
		{
			int reply = JOptionPane.showConfirmDialog(progressDialog, "Soll die Datenbank '" + DBTools.getDbName() + "' erstellt werden?", 
					"Datenbank nicht vorhanden.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(reply == JOptionPane.YES_OPTION)
			{
				DBTools.createDatabase();
				exists = true;
			}
		}
		DBConnection.closeConnection();
		return exists;
	}
	
	private static void initPrefs()
	{
		while(!PrefsDialog.initPrefs())
			new PrefsDialog(progressDialog).showDialog();;
	}
	
	private static boolean initLists(Iterable<League> leagues)
	{
		List<String> teams = new ArrayList<>();
		List<String> leagueNames = new ArrayList<>();
		for (League league : leagues)
		{
			for (String team : league.getTeams())
				if (!teams.contains(team))
					teams.add(team);
			if (!leagueNames.contains(league.getName()))
				leagueNames.add(league.getName());
		}
		Collections.sort(teams);
		SingleLeagueFilterPanel.setLeagueList(leagueNames);
		TeamFilterPanel.setTeamList(teams);
		return teams.size() != 0;
	}
	
	
	/*
	 * Writes Games from Harddisk to DB
	 */
	
//	public static void main(String[] args) throws FussballException
//	{
//		List<League> leagues = new ArrayList<>();
//		initLeagues(leagues);
//		FileIO.loadLeagues(leagues);
//		checkDB();
//		DBTools.openMySQLDatabase();
//		for(League league : leagues)
//		{
//			if(!DBTools.tableExists(league))
//				DBTools.createTable(league);
//			for(Season season : league)
//			{
//				DBTools.insertSeason(season);
//				System.out.println("Season " + season + " inserted");
//			}		
//		}
//		DBConnection.closeConnection();
//	}
	
}
