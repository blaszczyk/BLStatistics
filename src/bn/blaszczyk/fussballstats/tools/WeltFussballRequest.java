package bn.blaszczyk.fussballstats.tools;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;

public class WeltFussballRequest implements SeasonRequest
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
	
	@Override
	public void requestData(Season season) throws FussballException
	{
		String innerPath = String.format(season.getLeague().getURLFormat(), season.getYear()-1, season.getYear());
		String url = String.format("%s/%s/", BASE_URL, innerPath);
		try
		{
			HtmlPage page = webClient.getPage(url);
			DomElement domElement = page.getBody();
			domElement = getSubElement(domElement, 3);
			domElement = getSubElement(domElement, 2);
			domElement = getSubElement(domElement, 3);
			domElement = getSubElement(domElement, 2);
			domElement = getSubElement(domElement, 0);
			domElement = getSubElement(domElement, 0);
			domElement = getSubElement(domElement, 2);
			domElement = getSubElement(domElement, 0);
			domElement = getSubElement(domElement, 0);
			tableBody = (HtmlTableBody) getSubElement(domElement, 0);
			webClient.close();
		}
		catch (FailingHttpStatusCodeException | IOException | NullPointerException e)
		{
			throw new FussballException("Fehler beim Download von Saison " + season.getLeague() + " - "+ season.getYear(),e);
		}
	}
	
	
	@Override
	public Stack<Game> getGames() throws FussballException
	{
		Stack<Game> games = new Stack<>();
		for(HtmlTableRow row :  tableBody.getRows())
		{
			List<HtmlTableCell> cells = row.getCells();
			if(cells.size() == 1)
				matchDay = Integer.parseInt( ((HtmlAnchor)cells.get(0).getFirstElementChild()).getHrefAttribute().split("/")[3].trim() );
			else if(cells.size() == 8)
			{
				String team1 = null;
				String team2 = null;
				String result = null;				
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
						throw new FussballException("Wrong day format in " + temp,e);
					}
				}
				tempElement = cells.get(2).getFirstElementChild();
				if(tempElement instanceof HtmlAnchor)
					team1 =  ((HtmlAnchor)tempElement).getAttribute("title");
				else
					throw new FussballException("Wrong team1 format in " + tempElement);
				tempElement = cells.get(4).getFirstElementChild();
				if(tempElement instanceof HtmlAnchor)
					team2 =  ((HtmlAnchor)tempElement).getAttribute("title");
				else
					throw new FussballException("Wrong team2 format in " + tempElement);
				HtmlTableDataCell dataCell = (HtmlTableDataCell) cells.get(5);
				result = dataCell.getTextContent().trim();
				String gameString =  String.format( "%2d. Spieltag  %s: %15s - %15s %s" , matchDay, DATE_FORMAT.format(date), team1, team2, result);
				if(gameString.lastIndexOf("(") >= 0)
					gameString = gameString.substring(0, gameString.lastIndexOf("("));
				if(gameString.lastIndexOf("Wert.") >= 0)
					gameString = gameString.substring(0, gameString.lastIndexOf("Wert."));
				try
				{
					games.push(new Game(gameString));
				}
				catch(FussballException e)
				{
				}
			}
		}
		return games;
	}
	

	@Override
	protected void finalize() throws Throwable
	{
		webClient.close();
	}
	

	private static DomElement getSubElement(DomElement e, int i) throws NullPointerException
	{
		for(DomElement ee : e.getChildElements() )
		{
			i--;
			if(i < 0)
				return ee;
		}
		return null;
	}

}
