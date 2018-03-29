package bn.blaszczyk.fussballstats;

import java.util.Calendar;

import bn.blaszczyk.fussballstats.gui.MainFrame;
import bn.blaszczyk.fussballstats.tools.Initiator;
import bn.blaszczyk.rosecommon.controller.ControllerBuilder;
import bn.blaszczyk.rosecommon.controller.ModelController;
import bn.blaszczyk.rosecommon.tools.Preferences;
import bn.blaszczyk.rosecommon.tools.TypeManager;

public class FussballStats
{
	public static final int THIS_SEASON = Calendar.getInstance().get(Calendar.YEAR) + ( (Calendar.getInstance().get(Calendar.MONTH)  > Calendar.JUNE) ? 1 : 0  );
	
	public static void main(String[] args)
	{
		TypeManager.parseRoseFile("bn/blaszczyk/fussballstats/resources/fstats.rose");
		Preferences.setMainClass(FussballStats.class);
		final ModelController controller = ControllerBuilder.forDataBase().withConsistencyCheck().build();
		if(new Initiator(controller).initAll())
		{
			MainFrame mf = new MainFrame(controller);
			mf.showFrame();
		}
	}
}
