package uk.co.blackpepper.penguin.client;

/**
 * Simple variant of Guava's Preconditions class.
 */
final class Preconditions
{
	private Preconditions()
	{
		throw new AssertionError();
	}
	
	public static <T> T checkNotNull(T object, String message)
	{
		if (object == null)
		{
			throw new NullPointerException(message);
		}
		
		return object;
	}
}
