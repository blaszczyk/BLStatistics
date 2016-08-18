package bn.blaszczyk.fussballstats.tools;

@SuppressWarnings("serial")
public class FussballException extends Exception
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
