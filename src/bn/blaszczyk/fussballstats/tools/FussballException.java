package bn.blaszczyk.fussballstats.tools;

import bn.blaszczyk.rose.RoseException;

@SuppressWarnings("serial")
public class FussballException extends RoseException
{
	public FussballException(String errorMessage)
	{
		super(errorMessage);
	}

	public FussballException(String errorMessage, Throwable cause)
	{
		super(errorMessage,cause);
	}
}
