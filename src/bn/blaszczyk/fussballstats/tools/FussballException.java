package bn.blaszczyk.fussballstats.tools;

@SuppressWarnings("serial")
public class FussballException extends Exception
{
	private String errorMessage;


	public FussballException(String errorMessage)
	{
		this.errorMessage=errorMessage;
	}


	public FussballException(String errorMessage, Throwable cause)
	{
		super(cause);
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	@Override
	public void printStackTrace()
	{
		super.printStackTrace();
		System.err.println(getErrorMessage());
	}


}
