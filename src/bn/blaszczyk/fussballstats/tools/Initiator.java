package bn.blaszczyk.fussballstats.tools;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.model.League;
import bn.blaszczyk.fussballstats.model.Season;
import bn.blaszczyk.fussballstats.model.Team;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rosecommon.controller.ModelController;
import bn.blaszczyk.fussballstats.gui.LeagueManager;
import bn.blaszczyk.fussballstats.gui.PrefsDialog;
import bn.blaszczyk.fussballstats.gui.corefilters.LeagueFilterPanel;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamFilterPanel;
import bn.blaszczyk.fussballstats.gui.tools.ProgressDialog;

public class Initiator {
	/*
	 * Constants
	 */
	public static final String	LEAGUES_FILE	= "data/leagues.dat";
	private static final String	ICON_FILE		= "data/icon.png";

	/*
	 * Displaying Component
	 */
//	private ProgressDialog progressDialog;
	
	private final ModelController controller;
	
	
	
	public Initiator(final ModelController controller)
	{
		this.controller = controller;
	}

	/*
	 * Full Initialization
	 */
	public boolean initAll()
	{
		initUIManager();
		TeamAlias.loadAliases();
		
		try
		{
			final List<League> leagues = initLeagues();
//			final int seasonCount = leagues.stream().map(League::getSeasons).mapToInt(Collection::size).sum();

//			progressDialog = new ProgressDialog(null, seasonCount, "Initiiere FussballStats",
//					Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)), true);
//			progressDialog.showDialog();
//			
//			progressDialog.appendInfo("Lade Einstellungen");
//
//			progressDialog.appendInfo("\nErstelle Listen");
//			if(!initLists(leagues))
//			{
//				int reply = JOptionPane.showConfirmDialog(progressDialog, "Liga Manager öffnen und Spiele Downloaden?", 
//						"Keine Spiele vorhanden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//				if(reply == JOptionPane.YES_OPTION)
//				{
//					new LeagueManager(progressDialog, controller).showDialog();
//					initLists(leagues);
//				}
//			}
		}
		catch (RoseException e)
		{
//			if(progressDialog != null)
//			{
//				progressDialog.appendException(e);
//				progressDialog.setFinished();
//			}
			throw e;
		}
		
//		progressDialog.disposeDialog();
		return true;
	}
	
	/*
	 * Init Known Leagues From Resource
	 */
	private List<League> initLeagues() throws RoseException
	{
		try
		{
			final Path leagueData = Paths.get(FussballStats.class.getResource(LEAGUES_FILE).toURI());
			for(final String line : Files.readAllLines(leagueData))
				if(!line.startsWith("//"))
					ensureLeague(line);
			return controller.getEntities(League.class);
		}
		catch(URISyntaxException | IOException e)
		{
			throw new RoseException("Error initializig leagues", e);
		}
	}
	
	private void ensureLeague(final String line) throws RoseException
	{
		final String[] split = line.split(";");
		if(split.length < 3)
			throw new RoseException("Cannot parse league line: " + line);
		final String[] chain = split[2].split("\\/");
		final League root = getRootLeague();
		final League league = ensureRecursive(root,chain);
		final String leagueName = split[0].trim();
		if(!leagueName.equals(league.getName()))
		{
			league.setName(leagueName);
			controller.update(league);
		}
		ensureSeasons(league, Arrays.copyOfRange(split, 3,split.length));
	}
	
	private void ensureSeasons(final League	league, final String[] yearBounds) throws RoseException
	{
		if(yearBounds.length == 0)
			return;
		final List<Integer> bounds = Arrays.stream(yearBounds)
				.map(String::trim)
				.map(Integer::parseInt)
				.collect(Collectors.toList());
		for(int year = bounds.get(0); year <= FussballStats.THIS_SEASON; year++)
			if(contains(bounds, year))
				ensureSeason(league,year);
	}	

	private void ensureSeason(final League league, final int year) throws RoseException
	{
		if(league.getSeasons().stream().mapToInt(Season::getYear).anyMatch(i -> i == year))
			return;
		final Season season = controller.createNew(Season.class);
		season.setYear(year);
		season.setEntity(Season.LEAGUE, league);
		controller.update(season);
	}

	private static boolean contains(final List<Integer> bounds, final int year)
	{
		boolean result = false;
		for(final Integer bound : bounds)
			if(bound.intValue() <= year)
				result = !result;
		return result;
	}
	
	private League ensureRecursive(final League parent, final String[] chain) throws RoseException
	{
		if(chain.length == 0)
			return parent;
		final String leagueId = chain[0].trim();
		final String[] subChain = Arrays.copyOfRange(chain, 1, chain.length);
		
		League league = parent.getChilds().stream()
			.filter(l -> l.getLeagueId().equals(leagueId))
			.findFirst().orElse(null);
		if(league == null)
		{
			league = controller.createNew(League.class);
			league.setLeagueId(leagueId);
			league.setEntity(League.PARENT, parent);
			controller.update(league);
		}
		return ensureRecursive(league, subChain);
	}
	
	private League getRootLeague() throws RoseException
	{
		for(final League league : controller.getEntities(League.class))
			if(league.getLeagueId().equals(""))
				return league;
		final League root = controller.createNew(League.class);
		root.setLeagueId("");
		root.setParent(null);
		root.setName("Root");
		controller.update(root);
		return root;
	}
	
	/*
	 * Inits UI
	 */
	private static void initUIManager()
	{	
		Font plainFont = new Font("Arial", Font.PLAIN, 16);
		Font boldFont = new Font("Arial", Font.BOLD, 16);
		Font tableFont = new Font("Arial", Font.PLAIN, 14);

		Locale.setDefault(Locale.GERMAN);
		
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
	
	/*
	 * Init Lists for ComboBox Users
	 */
	private static boolean initLists(final Collection<League> leagues)
	{
		final List<String> teams = leagues.stream()
								.map(League::getSeasons)
								.flatMap(Collection::stream)
								.map(Season::getTeams)
								.flatMap(Collection::stream)
								.map(Team::getName)
								.distinct()
								.sorted()
								.collect(Collectors.toList());
		final List<String> leagueNames = leagues.stream()
								.map(League::getName)
								.distinct()
								.collect(Collectors.toList());
		LeagueFilterPanel.setLeagueList(leagueNames);
		TeamFilterPanel.setTeamList(teams);
		return teams.size() != 0;
	}
	
}
