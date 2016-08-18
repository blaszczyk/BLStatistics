package bn.blaszczyk.fussballstats.tools;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;

public class WeltFussballRequest
{

	private static final String	BASE_URL = "http://www.weltfussball.de/alle_spiele";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	private int matchDay;
	private Date date;

	private WebClient webClient = new WebClient();
	private HtmlTableBody tableBody;

	
	public WeltFussballRequest()
	{
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
	}
	
	public void requestData(Season season) throws FussballException
	{
		String urlPath = String.format(season.getLeague().getURLFormat(), season.getYear()-1, season.getYear());
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
		}
		catch (FailingHttpStatusCodeException | IOException | NullPointerException e)
		{
			throw new FussballException("Fehler beim Download von Saison " + season.getLeague() + " - "+ season.getYear(),e);
		}
	}
	
	
	public List<Game> getGames() throws FussballException
	{
		List<Game> games = new ArrayList<>();
		for(HtmlTableRow row :  tableBody.getRows())
		{
			List<HtmlTableCell> cells = row.getCells();
			
			/*
			 * MatchDay
			 */
			if(cells.size() == 1)
				matchDay = Integer.parseInt( ((HtmlAnchor)cells.get(0).getFirstElementChild()).getHrefAttribute().split("/")[3].trim() );
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
				if(result.equals("-:-"))
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
				
				/*
				 * Create Game
				 */
				games.add(new Game(matchDay, date, teamH, teamA, goalsH, goalsA));
			}
		}
		return games;
	}
	

	@Override
	protected void finalize() throws Throwable
	{
		webClient.close();
	}
	
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
