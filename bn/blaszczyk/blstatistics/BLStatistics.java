package bn.blaszczyk.blstatistics;

import java.util.Arrays;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.LeagueManager;
import bn.blaszczyk.blstatistics.gui.MainFrame;
import bn.blaszczyk.blstatistics.tools.FileIO;

public class BLStatistics
{

	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga",1964,2016);
		FileIO.loadAllSeasons(bundesliga);

		League zweiteliga = new League("zweiteliga",1975,2016);
		FileIO.loadAllSeasons(zweiteliga);
		
		League dritteliga = new League("dritteliga",2009,2016);
		FileIO.loadAllSeasons(dritteliga);
		
		League[] leagues = {bundesliga,zweiteliga,dritteliga};
		
		MainFrame mf = new MainFrame(Arrays.asList(leagues));
		mf.showFrame();
		
		LeagueManager lm = new LeagueManager(mf, leagues);
		lm.showDialog();
	}

}
