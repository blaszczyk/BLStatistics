package bn.blaszczyk.fussballstats;

import java.util.ArrayList;
import java.util.List;

import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.gui.MainFrame;
import bn.blaszczyk.fussballstats.tools.Initiator;

public class FussballStats
{
	public static void main(String[] args)
	{
		List<League> leagues = new ArrayList<>();
		if(Initiator.initAll(leagues))
		{
			MainFrame mf = new MainFrame(leagues);
			mf.showFrame();
		}
	}
}
