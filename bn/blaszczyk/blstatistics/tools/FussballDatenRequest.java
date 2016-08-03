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

public class FussballDatenRequest {
	
	private static final String	BASE_URL = "http://www.fussballdaten.de";
											
	private static HtmlTable gamesTable;
								
	public FussballDatenRequest()
	{}
	
	// request table from www.fussballdaten.de
	public static void requestTable(int year, String league) throws BLException
	{
		// Bundesliga Seasons only from 1964 - now
		Calendar now = new GregorianCalendar();
		int latestSeason = now.get(Calendar.YEAR);
		if (now.get(Calendar.MONTH) > 6)
			latestSeason++;
		if (year < 1964 || year > latestSeason)
			return;
			
		// Request Table online
		String url = String.format("%s/%s/%4d/", BASE_URL, league, year);
		try
		{
			WebClient webClient = new WebClient();
			HtmlPage page = webClient.getPage(url);
			HtmlDivision div = (HtmlDivision) page.getElementById("rt_Kreuztabelle");
			gamesTable = (HtmlTable) div.getFirstElementChild();
			webClient.close();
		}
		catch (FailingHttpStatusCodeException | IOException e)
		{
			setMutedErrStream(false);
			throw new BLException("Error requesting Season " + year + " of League " + league,e);
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
	
	public static void requestTableMuted(int year, String league) throws BLException
	{
		setMutedErrStream(true);
		requestTable(year,league);
		setMutedErrStream(false);
	}
}
