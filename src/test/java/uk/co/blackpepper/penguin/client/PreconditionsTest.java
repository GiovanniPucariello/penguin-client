package uk.co.blackpepper.penguin.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreconditionsTest
{
	@Test
	public void checkNotNullWithNotNull()
	{
		Preconditions.checkNotNull("x", "y");
	}
	
	@Test(expected = NullPointerException.class)
	public void checkNotNullWithNull()
	{
		try
		{
			Preconditions.checkNotNull(null, "y");
		}
		catch (NullPointerException exception)
		{
			assertEquals("y", exception.getMessage());
			throw exception;
		}
	}
}
