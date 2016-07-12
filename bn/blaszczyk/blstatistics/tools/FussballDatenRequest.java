package bn.blaszczyk.blstatistics.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class FussballDatenRequest {

	private static final String BASE_URL = "http://www.fussballdaten.de/bundesliga";

	private static WebClient webClient;
	private static HtmlTable crossTable;
	private static PrintStream muteStream = new PrintStream(new OutputStream() {
		@Override
		public void write(int b) throws IOException {
		}
	});

	public FussballDatenRequest() {
	}

	public static void loadPage(int year) {
		//Mute Error OutpotStream
		System.setErr(muteStream);	
		
		try {
			webClient = new WebClient();
			HtmlPage page;
			page = webClient.getPage(String.format("%s/%4d/", BASE_URL, year));
			HtmlDivision div = (HtmlDivision) page.getElementById("rt_Kreuztabelle");
			crossTable = (HtmlTable) div.getFirstElementChild();
			webClient.close();

		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	public static Stack<String> getGames(){
		Stack<String> games = new Stack<>();
		if(!(crossTable == null))
			for (HtmlTableRow row : crossTable.getRows())
				for (HtmlTableCell cell : row.getCells())
					if (cell.getAttribute("class").equals("Gegner") && cell.getFirstElementChild() instanceof HtmlAnchor) {
						HtmlAnchor anchor = (HtmlAnchor) cell.getFirstElementChild();
						games.push(anchor.getAttribute("title"));
				}
		return games;
	}
	
	public static List<String> getTeams(){
		List<String> teams = new ArrayList<>();
		if(!(crossTable == null))
			for(HtmlTableCell cell : crossTable.getRow(0).getCells() )
				if(cell.getAttribute("class").equals("Gegner"))
					teams.add(cell.getAttribute("title"));
		return teams;
	}
}
