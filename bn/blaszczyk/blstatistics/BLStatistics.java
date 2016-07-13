package bn.blaszczyk.blstatistics;

import bn.blaszczyk.blstatistics.controller.BasicController;
import bn.blaszczyk.blstatistics.core.League;
import bn.blaszczyk.blstatistics.tools.ConsoleTests;

public class BLStatistics
{

	public static void main(String[] args)
	{
		League bundesliga = new League("bundesliga");
		BasicController controller = new BasicController(bundesliga);
		controller.loadAllSeasons();
		ConsoleTests.printAllTables(bundesliga);
		controller.saveAllSeasons();
	}

}
