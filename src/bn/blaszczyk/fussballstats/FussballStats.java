package bn.blaszczyk.fussballstats;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.UIManager;

import bn.blaszczyk.fussballstats.core.*;
import bn.blaszczyk.fussballstats.gui.LeagueManager;
import bn.blaszczyk.fussballstats.gui.MainFrame;
import bn.blaszczyk.fussballstats.gui.corefilters.SingleLeagueFilterPanel;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamFilterPanel;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamSearchFilterPanel;
import bn.blaszczyk.fussballstats.tools.DBTools;
import bn.blaszczyk.fussballstats.tools.FileIO;
import bn.blaszczyk.fussballstats.tools.TeamAlias;

public class FussballStats
{
	public static final String  WELT_FUSSBALL= "wfb";
	public static final String  FUSSBALL_DATEN = "fbd";
	
	private static String requestSource = WELT_FUSSBALL;

	public static String getRequestSource()
	{
		return requestSource;
	}

	private static void initUIManager()
	{
		Font plainFont = new Font("Arial",Font.PLAIN,16);
		Font boldFont = new Font("Arial",Font.BOLD,16);
		Font tableFont = new Font("Arial",Font.PLAIN,14);
		
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

//		UIManager.put("JTree.font", plainFont);
//		UIManager.put("TabbedPane.font", plainFont);
//		UIManager.put("Tree.font", plainFont);
//		UIManager.put("RadioButton.font", plainFont);
//		UIManager.put("RadioButtonMenuItem.font", plainFont);
	}	
	

	private static boolean initLists(Iterable<League> leagues)
	{
		List<String> teams = new ArrayList<>();
		List<String> leagueNames = new ArrayList<>();
		for(League league : leagues)
		{
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
			if(!leagueNames.contains(league.getName()))
				leagueNames.add(league.getName());
		}
		Collections.sort(teams);
		SingleLeagueFilterPanel.setLeagueList(leagueNames);
		TeamFilterPanel.setTeamList(teams);
		TeamSearchFilterPanel.setTeamList(teams);
		return teams.size() != 0;
	}
	
	public static void main(String[] args)
	{
		initUIManager();
		TeamAlias.loadAliases();
		List<League> leagues = FileIO.initLeagues();
//		FileIO.loadLeagues(leagues);
		DBTools.openMySQLDatabase();
		for(Season season : leagues.get(0))
			DBTools.loadGames(season);
		if(!initLists(leagues))
		{
			LeagueManager lm = new LeagueManager(null, leagues);
			lm.showDialog();
		}
		MainFrame mf = new MainFrame(leagues);
		mf.showFrame();
	}

}
