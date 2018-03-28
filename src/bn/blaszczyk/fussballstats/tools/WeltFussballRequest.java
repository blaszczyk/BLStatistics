package bn.blaszczyk.fussballstats.tools;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.League;
import bn.blaszczyk.fussballstats.model.Matchday;
import bn.blaszczyk.fussballstats.model.Season;
import bn.blaszczyk.fussballstats.model.Team;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rosecommon.controller.ModelController;


public class WeltFussballRequest
{

	/*
	 * Constatns
	 */
	private static final String	BASE_URL = "http://www.weltfussball.de/alle_spiele";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");


	private final WebClient webClient = new WebClient();
	private HtmlTableBody tableBody;
	
	private final ModelController controller;

	private final Map<String,Team> teams;
	/*
	 * Constructor
	 */
	public WeltFussballRequest(final ModelController controller)
	{
		this.controller = controller;
		teams = controller.getEntities(Team.class).stream()
				.collect(Collectors.toMap(Team::getName, UnaryOperator.identity()));
		
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
	}
	
	/*
	 * Request Data From Webpage
	 */
	public void requestData(final Season season) throws RoseException
	{
		String urlPath = getPathForSeason(season);
		String url = String.format("%s/%s/", BASE_URL, urlPath);
		try
		{
			HtmlPage page = webClient.getPage(url);
			DomElement domElement = page.getBody();
			domElement = getChildElement(domElement, 3);
			domElement = getChildElement(domElement, 2);
			domElement = getChildElement(domElement, 3);
			domElement = getChildElement(domElement, 2);
			domElement = getChildElement(domElement, 0);
			domElement = getChildElement(domElement, 0);
			domElement = getChildElement(domElement, 2);
			domElement = getChildElement(domElement, 0);
			domElement = getChildElement(domElement, 0);
			tableBody = (HtmlTableBody) getChildElement(domElement, 0);
			update(season);
				
		}
		catch (FailingHttpStatusCodeException | IOException | NullPointerException | ClassCastException e)
		{
			throw new FussballException("Fehler beim Download von Saison " + season,e);
		}
	}

	private String getPathForSeason(Season season) throws FussballException
	{
		final League league = season.getLeague();
		final int year = season.getYear();
		try
		{
			final URI leaguesUri = FussballStats.class.getResource(Initiator.LEAGUES_FILE).toURI();
			final String urlFormat = Files.lines(Paths.get(leaguesUri))
					.filter(s -> !s.startsWith("////"))
					.map(s -> s.split(";"))
					.filter(s -> contains(s,league))
					.filter(s -> contains(s,year))
					.map(s -> s[1].trim())
					.findFirst()
					.orElse(""); // TODO expection
			return String.format(urlFormat, year-1, year);
		}
		catch (IOException | URISyntaxException e)
		{
			throw new FussballException("Cannot construct url path for season " + season, e);
		}
			
	}
	
	private static boolean contains(final String[] row, final League league)
	{
		if(row.length < 3)
			return false;
		final String fullLeagueId = "/" + row[2].trim();
		return fullLeagueId.equals(fullLeagueId(league).toString());
	}
	
	private static StringBuilder fullLeagueId(final League league)
	{
		if(league.getParent() == null)
			return new StringBuilder(league.getLeagueId());
		else
		{
			return fullLeagueId(league.getParent()).append("/").append(league.getLeagueId());
		}
	}
	
	private static boolean contains(final String[] row, final int year)
	{
		boolean result = false;
		int bound = Integer.MIN_VALUE;
		int i = 3;
		while(i < row.length && bound < year)
		{
			bound = Integer.parseInt(row[i].trim());
			result = !result;
			i++;
		}
		return result;
	}
	/*
	 * Extract GameList From Requested Data
	 */
	public void update(final Season season) throws FussballException, RoseException
	{
		Matchday matchday = null;
		Date date = null;
		for(HtmlTableRow row :  tableBody.getRows())
		{
			List<HtmlTableCell> cells = row.getCells();
			
			/*
			 * MatchDay
			 */
			if(cells.size() == 1)
			{
				final int matchDay = Integer.parseInt( ((HtmlAnchor)cells.get(0).getFirstElementChild()).getHrefAttribute().split("/")[3].trim() );
				matchday = season.getMatchdays().stream()
						.filter(m -> m.getCount().intValue() == matchDay)
						.findFirst().orElse(null);
				if(matchday == null)
					matchday = newMatchday(matchDay,season);
			}
			else if(cells.size() == 8)
			{		
				
				/*
				 * Date
				 */
				DomElement tempElement = cells.get(0).getFirstElementChild();
				if(tempElement instanceof HtmlAnchor)
				{
					String temp =  ((HtmlAnchor)tempElement).getAttribute("title");
					try
					{
						date = DATE_FORMAT.parse(temp.substring(temp.lastIndexOf(' ')).trim());
					}
					catch (ParseException e)
					{
						throw new FussballException("Falsches Datum Format in " + temp,e);
					}
				}
				
				/*
				 * Teams
				 */
				String teamH = null;
				tempElement = cells.get(2).getFirstElementChild();
				if(tempElement instanceof HtmlAnchor)
					teamH =  ((HtmlAnchor)tempElement).getAttribute("title");
				else
					throw new FussballException("Falsches Verein Format in " + tempElement);

				String teamA = null;
				tempElement = cells.get(4).getFirstElementChild();
				if(tempElement instanceof HtmlAnchor)
					teamA =  ((HtmlAnchor)tempElement).getAttribute("title");
				else
					throw new FussballException("Falsches Verein Format in " + tempElement);
				
				/*
				 * Result
				 */	
				HtmlTableDataCell dataCell = (HtmlTableDataCell) cells.get(5);
				String result = dataCell.getTextContent().trim();
				if(result.lastIndexOf("(") >= 0)
					result = result.substring(0, result.lastIndexOf("("));
				if(result.lastIndexOf("Wert.") >= 0)
					result = result.substring(0, result.lastIndexOf("Wert."));
				if(result.equals("-:-") || result.equals("annull.") || result.equals("n.gesp.") || result.equals( "verl.") || result.equals("abgebr."))
					continue;
				String[] split = result.split(":");
				int goalsH, goalsA;
				try
				{
					goalsH = Integer.parseInt(split[0].trim());
					goalsA = Integer.parseInt(split[1].trim());
				}
				catch(NumberFormatException | IndexOutOfBoundsException e)
				{
					throw new FussballException("Falsches Ergebnis Format in " + result, e);
				}
				createGame(date, getTeam(teamH), getTeam(teamA), goalsH, goalsA, matchday);
			}
		}
		season.getMatchdays().stream()
			.map(Matchday::getGames)
			.flatMap(Collection::stream)
			.map(Game::getTeamHome)
			.forEach(season.getTeams()::add);
		season.getMatchdays().stream()
			.map(Matchday::getGames)
			.flatMap(Collection::stream)
			.map(Game::getTeamAway)
			.forEach(season.getTeams()::add);
		controller.update(season);
	}
	
	private Team getTeam(final String teamName) throws RoseException
	{
		if(teams.containsKey(teamName))
			return teams.get(teamName);
		final Team team = controller.createNew(Team.class);
		team.setName(teamName);
		controller.update(team);
		teams.put(team.getName(), team);
		return team;
	}
	
	private void createGame(Date date, final Team teamHome, final Team teamAway, int goalsHome, int goalsAway, Matchday matchday) throws RoseException
	{
		final Game game = controller.createNew(Game.class);
		game.setDate(date);
		game.setGoalsHome(goalsHome);
		game.setGoalsAway(goalsAway);
		game.setTeamHome(teamHome);
		game.setTeamAway(teamAway);
		game.setEntity(Game.MATCHDAY, matchday);
		controller.update(game);
	}

	private Matchday newMatchday(int count, Season season) throws RoseException
	{
		final Matchday matchday = controller.createNew(Matchday.class);
		matchday.setCount(count);
		matchday.setEntity(Matchday.SEASON, season);
		controller.update(matchday);
		return matchday;
	}

	/*
	 * Close Browser on Destruction
	 */
	@Override
	protected void finalize() throws Throwable
	{
		webClient.close();
	}
	
	/*
	 * Internal Methods
	 */
	private static DomElement getChildElement(DomElement parent, int i) throws NullPointerException
	{
		for(DomElement child : parent.getChildElements() )
		{
			i--;
			if(i < 0)
				return child;
		}
		return null;
	}

}
