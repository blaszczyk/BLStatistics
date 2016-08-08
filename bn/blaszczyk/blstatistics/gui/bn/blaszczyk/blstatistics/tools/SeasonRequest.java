package bn.blaszczyk.blstatistics.tools;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;

public interface SeasonRequest 
{
	public void requestData(Season season) throws BLException;
	public Iterable<Game> getGames() throws BLException; 
}
