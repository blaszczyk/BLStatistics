package bn.blaszczyk.fussballstats.tools;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.Timer;
import javax.swing.UIManager;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.gui.corefilters.SingleLeagueFilterPanel;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.ProgressDialog;

public class Initiator
{
	private static final String LEAGUES_FILE = "data/leagues.dat";
	private static final String ICON_FILE = "data/icon.png";

	public static boolean initAll(List<League> leagues)
	{
		int seasonCount = 0;
		List<String> uniqueLeagueNames = new ArrayList<>();
		
		initLeagues(leagues);
		for (League league : leagues)
			seasonCount += league.getSeasonCount();
		ProgressDialog progressDialog = new ProgressDialog(null, seasonCount, "Initiiere FussballStats", Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE) ), true);
		progressDialog.showDialog();
		
		progressDialog.appendInfo("Initialisiere Ligen");

		progressDialog.appendInfo("\nInitialisiere UIManager");
		initUIManager();

		progressDialog.appendInfo("\nInitialisiere TeamAlias");
		TeamAlias.loadAliases();

		// Start Local Drive IO
		// progressDialog.appendInfo("\nLade Ligen");
		// FileIO.loadLeagues(leagues);
		// End Local Drive IO

		// Start Database IO
		try
		{
			progressDialog.appendInfo("\nVerbinde mit Datenbank");
			DBTools.openMySQLDatabase();

			for (League league : leagues)
			{
				if (!uniqueLeagueNames.contains(league.getName()))
				{
					uniqueLeagueNames.add(league.getName());
					progressDialog.appendInfo("\nLade Liga: " + league.getName());
				}
				for (Season season : league)
				{
					progressDialog.incrementValue();
					DBTools.loadSeason(season);
				}
			}

			progressDialog.appendInfo("\nSchliesse Verbindung zu Datenbank");
			DBConnection.closeConnection();
		}
		catch (FussballException e)
		{
			progressDialog.appendException(e);
			final Timer timer = new Timer(100, null);
			timer.addActionListener( ev -> {
				if(progressDialog.hasCancelRequest())
				{
					progressDialog.disposeDialog();
					timer.stop();
				}
			});
			timer.start();
			return false;
		}
		// End Database IO

		progressDialog.appendInfo("\nLade Listen");
		initLists(leagues);

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
//			if(leagues.size() > 0)
//				break;
		}
		scanner.close();
		return leagues;
	}

	private static void initUIManager()
	{
//		try
//		{
//			UIManager.setLookAndFeel( new NimbusLookAndFeel());
//		}
//		catch (UnsupportedLookAndFeelException e)
//		{
//			e.printStackTrace();
//		}
		
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

		// UIManager.put("JTree.font", plainFont);
		// UIManager.put("TabbedPane.font", plainFont);
		// UIManager.put("Tree.font", plainFont);
		// UIManager.put("RadioButton.font", plainFont);
		// UIManager.put("RadioButtonMenuItem.font", plainFont);
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

}
