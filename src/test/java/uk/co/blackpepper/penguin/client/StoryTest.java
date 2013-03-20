package uk.co.blackpepper.penguin.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StoryTest
{
	@Test
	public void construct()
	{
		Story story = new Story("1", "A", "B", "C", true);
		
		assertStoryEquals("1", "A", "B", "C", true, story);
	}
	
	@Test(expected = NullPointerException.class)
	public void constructWithNullId()
	{
		new Story(null, "A", "B", "C", true);
	}
	
	@Test(expected = NullPointerException.class)
	public void constructWithNullReference()
	{
		new Story("1", null, "B", "C", true);
	}
	
	@Test(expected = NullPointerException.class)
	public void constructWithNullTitle()
	{
		new Story("1", "A", null, "C", true);
	}
	
	@Test(expected = NullPointerException.class)
	public void constructWithNullAuthor()
	{
		new Story("1", "A", "B", null, true);
	}
	
	@Test
	public void hashCodeWhenEqual()
	{
		Story story1 = createStory();
		Story story2 = createStory();
		
		assertEquals("hashCode", story1.hashCode(), story2.hashCode());
	}
	
	@Test
	public void equalsWhenEqual()
	{
		Story story1 = createStory();
		Story story2 = createStory();
		
		assertTrue("equals", story1.equals(story2));
	}
	
	@Test
	public void equalsWhenDifferentId()
	{
		Story story1 = new Story("1", "X", "Y", "Z", true);
		Story story2 = new Story("2", "X", "Y", "Z", true);
		
		assertFalse("equals", story1.equals(story2));
	}
	
	@Test
	public void equalsWhenDifferentReference()
	{
		Story story1 = new Story("1", "X", "Y", "Z", true);
		Story story2 = new Story("1", "A", "Y", "Z", true);
		
		assertFalse("equals", story1.equals(story2));
	}
	
	@Test
	public void equalsWhenDifferentTitle()
	{
		Story story1 = new Story("1", "X", "Y", "Z", true);
		Story story2 = new Story("1", "X", "A", "Z", true);
		
		assertFalse("equals", story1.equals(story2));
	}
	
	@Test
	public void equalsWhenDifferentAuthor()
	{
		Story story1 = new Story("1", "X", "Y", "Z", true);
		Story story2 = new Story("1", "X", "Y", "A", true);
		
		assertFalse("equals", story1.equals(story2));
	}
	
	@Test
	public void equalsWhenDifferentMerged()
	{
		Story story1 = new Story("1", "X", "Y", "Z", true);
		Story story2 = new Story("1", "X", "Y", "Z", false);
		
		assertFalse("equals", story1.equals(story2));
	}
	
	@Test
	public void toStringTest()
	{
		Story story = new Story("1", "X", "Y", "Z", true);
		
		String expected = "uk.co.blackpepper.penguin.client.Story[_id=1, reference=X, title=Y, author=Z, merged=true]";
		assertEquals(expected, story.toString());
	}
	
	private static void assertStoryEquals(String expectedId, String expectedReference, String expectedTitle,
		String expectedAuthor, boolean expectedMerged, Story actual)
	{
		assertEquals("id", expectedId, actual.getId());
		assertEquals("reference", expectedReference, actual.getReference());
		assertEquals("title", expectedTitle, actual.getTitle());
		assertEquals("author", expectedAuthor, actual.getAuthor());
		assertEquals("merged", expectedMerged, actual.isMerged());
	}
	
	private static Story createStory()
	{
		return new Story("1", "X", "Y", "Z", true);
	}
}
