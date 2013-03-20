package uk.co.blackpepper.penguin.client;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class QueueTest
{
	@Test
	public void construct()
	{
		List<Story> stories = createStories();
		Queue queue = new Queue("1", "A", stories);
		
		assertQueueEquals("1", "A", stories, queue);
	}
	
	@Test(expected = NullPointerException.class)
	public void constructWithNullId()
	{
		new Queue(null, "A", createStories());
	}
	
	@Test(expected = NullPointerException.class)
	public void constructWithNullName()
	{
		new Queue("1", null, createStories());
	}
	
	@Test
	public void constructWithNullStories()
	{
		Queue queue = new Queue("1", "A", null);
		
		assertQueueEquals("1", "A", Collections.<Story>emptyList(), queue);
	}
	
	@Test
	public void constructClonesStories()
	{
		List<Story> stories = createStories();
		Queue queue = new Queue("1", "A", stories);
		
		assertNotSame("stories", stories, queue.getStories());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getStoriesIsImmutable()
	{
		createQueue().getStories().clear();
	}
	
	@Test
	public void hashCodeWhenEqual()
	{
		Queue queue1 = createQueue();
		Queue queue2 = createQueue();
		
		assertEquals("hashCode", queue1.hashCode(), queue2.hashCode());
	}
	
	@Test
	public void equalsWhenEqual()
	{
		Queue queue1 = createQueue();
		Queue queue2 = createQueue();
		
		assertTrue("equals", queue1.equals(queue2));
	}
	
	@Test
	public void equalsWhenDifferentId()
	{
		Queue queue1 = new Queue("1", "A", createStories());
		Queue queue2 = new Queue("2", "A", createStories());
		
		assertFalse("equals", queue1.equals(queue2));
	}
	
	@Test
	public void equalsWhenDifferentName()
	{
		Queue queue1 = new Queue("1", "A", createStories());
		Queue queue2 = new Queue("1", "B", createStories());
		
		assertFalse("equals", queue1.equals(queue2));
	}
	
	@Test
	public void equalsWhenDifferentStories()
	{
		Queue queue1 = new Queue("1", "A", singletonList(new Story("2", "X", "Y", "Z", false)));
		Queue queue2 = new Queue("1", "A", singletonList(new Story("3", "X", "Y", "Z", false)));
		
		assertFalse("equals", queue1.equals(queue2));
	}
	
	@Test
	public void toStringTest()
	{
		List<Story> stories = createStories();
		Queue queue = new Queue("1", "A", stories);
		
		String expected = String.format("uk.co.blackpepper.penguin.client.Queue[_id=1, name=A, stories=%s]", stories);
		assertEquals(expected, queue.toString());
	}
	
	private static void assertQueueEquals(String expectedId, String expectedName, List<Story> expectedStories,
		Queue actual)
	{
		assertEquals("id", expectedId, actual.getId());
		assertEquals("name", expectedName, actual.getName());
		assertEquals("stories", expectedStories, actual.getStories());
	}
	
	private static Queue createQueue()
	{
		return new Queue("1", "A", createStories());
	}
	
	private static List<Story> createStories()
	{
		return singletonList(createStory());
	}
	
	private static Story createStory()
	{
		return new Story("1", "X", "Y", "Z", false);
	}
}
