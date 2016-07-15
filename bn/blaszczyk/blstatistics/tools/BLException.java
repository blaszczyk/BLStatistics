package bn.blaszczyk.blstatistics.tools;

@SuppressWarnings("serial")
public class BLException extends Exception
{
	private String errorMessage;


	public BLException(String errorMessage)
	{
		this.errorMessage=errorMessage;
	}


	public BLException(String errorMessage, Throwable cause)
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
