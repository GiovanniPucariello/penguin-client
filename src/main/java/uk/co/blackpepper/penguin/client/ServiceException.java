package uk.co.blackpepper.penguin.client;

public final class ServiceException extends Exception
{
	public ServiceException(String message)
	{
		super(message);
	}

	public ServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
