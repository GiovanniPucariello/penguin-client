package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;

import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static uk.co.blackpepper.penguin.client.httpclient.HttpRequests.matchesGetJson;
import static uk.co.blackpepper.penguin.client.httpclient.HttpRequests.matchesPostRequest;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.json;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.noContent;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.notFound;

public class HttpClientStoryServiceTest
{
	private HttpClient client;

	private HttpClientStoryService service;
	
	@Before
	public void setUp()
	{
		client = mock(HttpClient.class);
		service = new HttpClientStoryService(client, "api");
	}

	@Test
	public void getAllWhenNoStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: []}"));

		assertEquals(emptyList(), service.getAll("1"));
	}

	@Test
	public void getAllWhenStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false}"
				+ "]}"));

		assertEquals(new Story("2", "P", "Q", "R", false), service.getAll("1").get(0));
	}

	@Test
	public void getAllWhenStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: false}"
				+ "]}"));

		List<Story> expecteds = asList(
			new Story("2", "P", "Q", "R", false),
			new Story("3", "S", "T", "U", false)
		);
		assertEquals(expecteds, service.getAll("1"));
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenInvalid() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("x"));

		service.getAll("1");
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(notFound());

		service.getAll("1");
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenThrow(new IOException());

		service.getAll("1");
	}

	@Test
	public void getMergedWhenMergedStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true}"
				+ "]}"));

		assertEquals(singletonList(new Story("2", "P", "Q", "R", true)), service.getMerged("1"));
	}

	@Test
	public void getMergedWhenMergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: true}"
				+ "]}"));

		List<Story> expecteds = asList(
			new Story("2", "P", "Q", "R", true),
			new Story("3", "S", "T", "U", true)
		);
		assertEquals(expecteds, service.getMerged("1"));
	}

	@Test
	public void getMergedWhenUnmergedStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false}"
				+ "]}"));

		assertEquals(emptyList(), service.getMerged("1"));
	}

	@Test
	public void getMergedWhenUnmergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: false}"
				+ "]}"));

		assertEquals(emptyList(), service.getMerged("1"));
	}

	@Test
	public void getMergedWhenMergedAndUnmergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: false}"
				+ "]}"));

		assertEquals(singletonList(new Story("2", "P", "Q", "R", true)), service.getMerged("1"));
	}

	@Test(expected = ServiceException.class)
	public void getMergedWhenInvalid() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("x"));

		service.getMerged("1");
	}

	@Test(expected = ServiceException.class)
	public void getMergedWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(notFound());

		service.getMerged("1");
	}

	@Test(expected = ServiceException.class)
	public void getMergedWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenThrow(new IOException());
		
		service.getMerged("1");
	}
	
	@Test
	public void getUnmergedWhenMergedStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true}"
				+ "]}"));

		assertEquals(emptyList(), service.getUnmerged("1"));
	}

	@Test
	public void getUnmergedWhenMergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: true}"
				+ "]}"));

		assertEquals(emptyList(), service.getUnmerged("1"));
	}

	@Test
	public void getUnmergedWhenUnmergedStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false}"
				+ "]}"));

		assertEquals(singletonList(new Story("2", "P", "Q", "R", false)), service.getUnmerged("1"));
	}

	@Test
	public void getUnmergedWhenUnmergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: false}"
				+ "]}"));

		List<Story> expecteds = asList(
			new Story("2", "P", "Q", "R", false),
			new Story("3", "S", "T", "U", false)
		);
		assertEquals(expecteds, service.getUnmerged("1"));
	}

	@Test
	public void getUnmergedWhenMergedAndUnmergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true},"
				+ "{_id: 3, reference: S, title: T, author: U, merged: false}"
				+ "]}"));

		assertEquals(singletonList(new Story("3", "S", "T", "U", false)), service.getUnmerged("1"));
	}

	@Test(expected = ServiceException.class)
	public void getUnmergedWhenInvalid() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(json("x"));

		service.getUnmerged("1");
	}
	
	@Test(expected = ServiceException.class)
	public void getUnmergedWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenReturn(notFound());
		
		service.getUnmerged("1");
	}
	
	@Test(expected = ServiceException.class)
	public void getUnmergedWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGetJson("api/queue/1"))))
			.thenThrow(new IOException());
		
		service.getUnmerged("1");
	}

	@Test
	public void mergeStoryPostsRequest() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesPostRequest("api/queue/q1/story/s1/merge", "merge"))))
			.thenReturn(noContent());
		
		service.merge("q1", "s1");
	}

	@Test(expected = ServiceException.class)
	public void mergeStoryWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesPostRequest("api/queue/q1/story/s1/merge", "merge"))))
			.thenThrow(new IOException());
		
		service.merge("q1", "s1");
	}

	@Test
	public void unmergeStoryPostsRequest() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesPostRequest("api/queue/q1/story/s1/unmerge", "unmerge"))))
			.thenReturn(noContent());
		
		service.unmerge("q1", "s1");
	}

	@Test(expected = ServiceException.class)
	public void unmergeStoryWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesPostRequest("api/queue/q1/story/s1/unmerge", "unmerge"))))
			.thenThrow(new IOException());
		
		service.unmerge("q1", "s1");
	}
}
