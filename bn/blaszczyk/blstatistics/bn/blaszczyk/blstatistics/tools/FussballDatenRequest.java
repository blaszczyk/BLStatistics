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

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;

public class FussballDatenRequest {
	
	private static final String	BASE_URL = "http://www.fussballdaten.de";

	private static HtmlTable gamesTable;
	
	// request html table from www.fussballdaten.de
	public static void requestData(Season season) throws BLException
	{
		String url = String.format("%s/%s/%4d/", BASE_URL, season.getLeague().getPathName(), season.getYear());
		try
		{
			WebClient webClient = new WebClient();
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setPopupBlockerEnabled(true);
			webClient.getOptions().setRedirectEnabled(false);
			HtmlPage page = webClient.getPage(url);
			HtmlDivision div = (HtmlDivision) page.getElementById("rt_Kreuztabelle");
			gamesTable = (HtmlTable) div.getFirstElementChild();
			webClient.close();
		}
		catch (FailingHttpStatusCodeException | IOException e)
		{
			setMutedErrStream(false);
			throw new BLException("Fehler beim Download von Saison " + season.getLeague() + " - "+ season.getYear(),e);
		}
	}
	
	// Creates Stack of Games from Table
	public static Stack<Game> getGames() throws BLException
	{
		Stack<Game> games = new Stack<>();
		if (gamesTable != null)
			for (HtmlTableRow row : gamesTable.getRows())
				for (HtmlTableCell cell : row.getCells())
					if (cell.getAttribute("class").equals("Gegner")
							&& cell.getFirstElementChild() instanceof HtmlAnchor)
					{
						HtmlAnchor anchor = (HtmlAnchor) cell.getFirstElementChild();
						String gameString = anchor.getAttribute("title");
						games.push(new Game(gameString));
					}
		return games;
	}
	
	public static void clearTable()
	{
		gamesTable = null;
		// Garbage Collector does the rest
	}
	
	public static void setMutedErrStream(boolean flag)
	{
		if (flag)
			System.setErr(new PrintStream(new OutputStream() {
				@Override
				public void write(int b) throws IOException
				{}
			}));
		else
			System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
	}
	
	public static void requestTableMuted(Season season) throws BLException
	{
		setMutedErrStream(true);
		requestData(season);
		setMutedErrStream(false);
	}
}
