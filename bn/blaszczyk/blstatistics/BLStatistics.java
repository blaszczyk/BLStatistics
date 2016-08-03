package bn.blaszczyk.blstatistics;

import java.util.Arrays;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.MainFrame;

public class BLStatistics
{

	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
		League[] leagues = {bundesliga};
		MainFrame mf = new MainFrame(Arrays.asList(leagues));
		mf.showFrame();
	}

}
