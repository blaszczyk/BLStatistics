package bn.blaszczyk.fussballstats;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;

import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rosecommon.tools.CommonPreference;
import bn.blaszczyk.rosecommon.tools.LoggerConfigurator;
import bn.blaszczyk.rosecommon.tools.Preferences;
import bn.blaszczyk.rosecommon.tools.TypeManager;
import bn.blaszczyk.roseservice.Launcher;

public class FussballStatsService extends Launcher
{
	public static final int THIS_SEASON = Calendar.getInstance().get(Calendar.YEAR) + ( (Calendar.getInstance().get(Calendar.MONTH)  > Calendar.JUNE) ? 1 : 0  );
	

	
	public static void main(String[] args)
	{
		try
		{
			TypeManager.parseRoseFile("bn/blaszczyk/fussballstats/resources/fstats.rose");
			Preferences.setMainClass(FussballStatsService.class);
			LoggerConfigurator.configureLogger(CommonPreference.BASE_DIRECTORY, CommonPreference.LOG_LEVEL);
			new FussballStatsService().launch();
		}
		catch (RoseException e)
		{
			LogManager.getLogger(FussballStatsService.class).error("Fehler beim starten des Service", e);
		}
	}
}
