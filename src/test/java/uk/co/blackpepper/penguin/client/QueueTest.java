package uk.co.blackpepper.penguin.client;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static java.util.Arrays.asList;
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
	
	@Test
	public void constructDefault()
	{
		Queue queue = new Queue();
		
		assertQueueEquals("", "", Collections.<Story>emptyList(), queue);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getStoriesIsImmutable()
	{
		createQueueWithStories().getStories().clear();
	}
	
	@Test
	public void getStoryCountMergedWithNoStories()
	{
		Queue queue = createQueue();
		
		assertEquals(0, queue.getStoryCount(true));
	}
	
	@Test
	public void getStoryCountMergedWithMergedStory()
	{
		Story story = new Story("1", "P", "Q", "R", true);
		Queue queue = createQueue(story);
		
		assertEquals(1, queue.getStoryCount(true));
	}
	
	@Test
	public void getStoryCountMergedWithMergedStories()
	{
		Story story1 = new Story("1", "P", "Q", "R", true);
		Story story2 = new Story("2", "S", "T", "U", true);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(2, queue.getStoryCount(true));
	}
	
	@Test
	public void getStoryCountMergedWithUnmergedStory()
	{
		Story story = new Story("1", "P", "Q", "R", false);
		Queue queue = createQueue(story);
		
		assertEquals(0, queue.getStoryCount(true));
	}
	
	@Test
	public void getStoryCountMergedWithUnmergedStories()
	{
		Story story1 = new Story("1", "P", "Q", "R", false);
		Story story2 = new Story("2", "S", "T", "U", false);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(0, queue.getStoryCount(true));
	}
	
	@Test
	public void getStoryCountMergedWithMergedAndUnmergedStories()
	{
		Story story1 = new Story("1", "P", "Q", "R", true);
		Story story2 = new Story("2", "S", "T", "U", false);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(1, queue.getStoryCount(true));
	}
	
	@Test
	public void getStoryCountUnmergedWithNoStories()
	{
		Queue queue = createQueue();
		
		assertEquals(0, queue.getStoryCount(false));
	}
	
	@Test
	public void getStoryCountUnmergedWithMergedStory()
	{
		Story story = new Story("1", "P", "Q", "R", true);
		Queue queue = createQueue(story);
		
		assertEquals(0, queue.getStoryCount(false));
	}
	
	@Test
	public void getStoryCountUnmergedWithMergedStories()
	{
		Story story1 = new Story("1", "P", "Q", "R", true);
		Story story2 = new Story("2", "S", "T", "U", true);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(0, queue.getStoryCount(false));
	}
	
	@Test
	public void getStoryCountUnmergedWithUnmergedStory()
	{
		Story story = new Story("1", "P", "Q", "R", false);
		Queue queue = createQueue(story);
		
		assertEquals(1, queue.getStoryCount(false));
	}
	
	@Test
	public void getStoryCountUnmergedWithUnmergedStories()
	{
		Story story1 = new Story("1", "P", "Q", "R", false);
		Story story2 = new Story("2", "S", "T", "U", false);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(2, queue.getStoryCount(false));
	}
	
	@Test
	public void getStoryCountUnmergedWithMergedAndUnmergedStories()
	{
		Story story1 = new Story("1", "P", "Q", "R", true);
		Story story2 = new Story("2", "S", "T", "U", false);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(1, queue.getStoryCount(false));
	}
	
	@Test
	public void hashCodeWhenEqual()
	{
		Queue queue1 = createQueueWithStories();
		Queue queue2 = createQueueWithStories();
		
		assertEquals("hashCode", queue1.hashCode(), queue2.hashCode());
	}
	
	@Test
	public void equalsWhenEqual()
	{
		Queue queue1 = createQueueWithStories();
		Queue queue2 = createQueueWithStories();
		
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
		Queue queue1 = new Queue("1", "A", singletonList(new Story("2", "X", "Y", "Z", true)));
		Queue queue2 = new Queue("1", "A", singletonList(new Story("3", "X", "Y", "Z", true)));
		
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
	
	@Test
	public void getPendingCountWhenEmpty()
	{
		Queue queue = createQueue();
		
		assertEquals(0, queue.getPendingCount());
	}
	
	@Test
	public void getPendingCountWhenNonePending()
	{
		Story story1 = new Story("1", "P", "Q", "R", true);
		Story story2 = new Story("2", "S", "T", "U", true);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(0, queue.getPendingCount());
	}
	
	@Test
	public void getPendingCountWhenPendingPending()
	{
		Story story1 = new Story("1", "P", "Q", "R", false);
		Story story2 = new Story("2", "S", "T", "U", false);
		Queue queue = createQueue(story1, story2);
		
		assertEquals(2, queue.getPendingCount());
	}
	
	private static void assertQueueEquals(String expectedId, String expectedName, List<Story> expectedStories,
		Queue actual)
	{
		assertEquals("id", expectedId, actual.getId());
		assertEquals("name", expectedName, actual.getName());
		assertEquals("stories", expectedStories, actual.getStories());
	}
	
	private static Queue createQueueWithStories()
	{
		return createQueue(createStory());
	}
	
	private static Queue createQueue(Story... stories)
	{
		return new Queue("1", "A", asList(stories));
	}
	
	private static List<Story> createStories()
	{
		return singletonList(createStory());
	}
	
	private static Story createStory()
	{
		return new Story("1", "X", "Y", "Z", true);
	}
}
