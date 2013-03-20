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

import static uk.co.blackpepper.penguin.client.httpclient.HttpRequests.matchesGet;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.json;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.notFound;

public class HttpClientStoryServiceTest
{
	private HttpClient client;

	private HttpClientStoryService service;
	
	@Before
	public void setUp()
	{
		client = mock(HttpClient.class);
		service = new HttpClientStoryService(client, "http://localhost/api");
	}

	@Test
	public void getAllWhenNoStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: []}"));

		assertEquals(emptyList(), service.getAll("1"));
	}

	@Test
	public void getAllWhenStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false}"
				+ "]}"));

		assertEquals(new Story("2", "P", "Q", "R", false), service.getAll("1").get(0));
	}

	@Test
	public void getAllWhenStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
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
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("x"));

		service.getAll("1");
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(notFound());

		service.getAll("1");
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenThrow(new IOException());

		service.getAll("1");
	}

	@Test
	public void getMergedWhenMergedStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: true}"
				+ "]}"));

		assertEquals(singletonList(new Story("2", "P", "Q", "R", true)), service.getMerged("1"));
	}

	@Test(expected = ServiceException.class)
	public void getMergedWhenInvalid() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("x"));

		service.getMerged("1");
	}

	@Test
	public void getUnmergedWhenUnmergedStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A, stories: ["
				+ "{_id: 2, reference: P, title: Q, author: R, merged: false}"
				+ "]}"));

		assertEquals(singletonList(new Story("2", "P", "Q", "R", false)), service.getUnmerged("1"));
	}

	@Test(expected = ServiceException.class)
	public void getUnmergedWhenInvalid() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("x"));

		service.getUnmerged("1");
	}
}
