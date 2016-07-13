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

public class FussballDatenRequest {
	
	private static final String	BASE_URL	= "http://www.fussballdaten.de/bundesliga";
											
	private static HtmlTable	gamesTable;
								
	public FussballDatenRequest()
	{}
	
	// request table from www.fussballdaten.de
	public static void requestTable(int year) // throws some Exception
	{
		// Bundesliga Seasons only from 1964 - now
		Calendar now = new GregorianCalendar();
		int latestSeason = now.get(Calendar.YEAR);
		if (now.get(Calendar.MONTH) > 6)
			latestSeason++;
		if (year < 1964 || year > latestSeason)
			return;
			
		// Request Table online
		String url = String.format("%s/%4d/", BASE_URL, year);
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
			System.err.println("Error loading " + url);
			e.printStackTrace();
		}
	}
	
	// Creates Stack of Games from Table
	public static Stack<String> getGames()
	{
		Stack<String> games = new Stack<>();
		if (!(gamesTable == null))
			for (HtmlTableRow row : gamesTable.getRows())
				for (HtmlTableCell cell : row.getCells())
					if (cell.getAttribute("class").equals("Gegner")
							&& cell.getFirstElementChild() instanceof HtmlAnchor)
					{
						HtmlAnchor anchor = (HtmlAnchor) cell.getFirstElementChild();
						games.push(anchor.getAttribute("title"));
					}
		return games;
	}
	
	// Creates List of Teams from Table
	public static List<String> getTeams()
	{
		List<String> teams = new ArrayList<>();
		if (!(gamesTable == null) && gamesTable.getRowCount() > 0)
			for (HtmlTableCell cell : gamesTable.getRow(0).getCells())
				if (cell.getAttribute("class").equals("Gegner"))
					teams.add(cell.getAttribute("title"));
		return teams;
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
	
	public static void requestTableMuted(int year)
	{
		setMutedErrStream(true);
		requestTable(year);
		setMutedErrStream(false);
	}
}
