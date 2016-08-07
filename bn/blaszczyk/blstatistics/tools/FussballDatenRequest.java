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
			for (HtmlTableRow row : gamesTable.getRows())
				for (HtmlTableCell cell : row.getCells())
					if (cell.getAttribute("class").startsWith("Gegner")
							&& cell.getFirstElementChild() instanceof HtmlAnchor)
					{
						String gameString;
						gameString = cell.getFirstElementChild().getAttribute("title");
						games.push(new Game(gameString));
						if( cell.getChildElementCount() > 1)
						{
							gameString = cell.getLastElementChild().getAttribute("title");
							games.push(new Game(gameString));
						}
					}
		return games;
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
