package bn.blaszczyk.fussballstats.tools;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;

public interface SeasonRequest 
{
	public void requestData(Season season) throws BLException;
	public Iterable<Game> getGames() throws BLException; 
}
