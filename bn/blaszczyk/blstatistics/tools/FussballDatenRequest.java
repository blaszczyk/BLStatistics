/*
 * Page Format is:
 * 
 * <html> ... <body> ...
 * <div id="rt_Kreuztabelle" ... >
 * <table ...>
 * <tr>
 *     <th title="FC Köln" class = "Gegner">...</th>
 * </tr>
 * <tr> ...
 *     <td class = "Gegner"><a title="9. Spieltag 26.10.63: FC Köln - Duisburg 3:3" ...></a></td>
 * </tr>
 * ...
 * </div> ... </body></html>
 */

package bn.blaszczyk.blstatistics.tools;

import java.io.IOException;
import java.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;

public class FussballDatenRequest implements SeasonRequest {
	
	private static final String	BASE_URL = "http://www.fussballdaten.de";

	private WebClient webClient = new WebClient();
	private HtmlTable gamesTable;
	private int year;
	
	
	public FussballDatenRequest()
	{
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setRedirectEnabled(false);
	}
	
	@Override
	public void requestData(Season season) throws BLException
	{
		String url = String.format("%s/%s/%4d/", BASE_URL, season.getLeague().getURLFormat(), season.getYear());
		year = season.getYear();
		try
		{
			HtmlPage page = webClient.getPage(url);
			HtmlDivision div = (HtmlDivision) page.getElementById("rt_Kreuztabelle");
			gamesTable = (HtmlTable) div.getFirstElementChild();
		}
		catch (FailingHttpStatusCodeException | IOException | NullPointerException e)
		{
//			setMutedErrStream(false);
			throw new BLException("Fehler beim Download von Saison " + season.getLeague() + " - "+ season.getYear(),e);
		}
	}
	
	@Override
	public Iterable<Game> getGames() throws BLException
	{
		Stack<Game> games = new Stack<>();
		if (gamesTable != null)
			for (int i = 1; i < gamesTable.getRowCount(); i++)
				for (HtmlTableCell cell : gamesTable.getRow(i).getCells())
					if (cell.getAttribute("class").startsWith("Gegner"))
					{
						if(cell.getFirstElementChild() instanceof HtmlAnchor)
						{
							pushGame(games, cell.getFirstElementChild().getAttribute("title"));
							if( cell.getChildElementCount() > 1)
								pushGame(games, cell.getLastElementChild().getAttribute("title"));
						}
						else if(cell.hasAttribute("title"))
							pushGame(games, cell.getAttribute("title") );
					}
		return games;
	}
	
	@SuppressWarnings("deprecation")
	private void pushGame(Stack<Game> games, String gameString)
	{
		try
		{
			games.push(new Game(gameString));
		}
		catch (BLException e)
		{
			gameString = "1.Spieltag "+ Game.DATE_FORMAT.format(new Date(year, 4, 30)) + gameString.substring(1) ;
			try
			{
				games.push(new Game(gameString));
			}
			catch (BLException e1)
			{
				System.err.println(e1.getErrorMessage());
			}
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		webClient.close();
	}
	
	
	
//	public static void setMutedErrStream(boolean flag)
//	{
//		if (flag)
//			System.setErr(new PrintStream(new OutputStream() {
//				@Override
//				public void write(int b) throws IOException
//				{}
//			}));
//		else
//			System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
//	}
//	
//	public void requestTableMuted(Season season) throws BLException
//	{
//		setMutedErrStream(true);
//		requestData(season);
//		setMutedErrStream(false);
//	}
}
