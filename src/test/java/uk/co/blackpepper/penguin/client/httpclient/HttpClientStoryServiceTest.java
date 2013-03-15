package uk.co.blackpepper.penguin.client.httpclient;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.co.blackpepper.penguin.client.httpclient.HttpRequests.matchesGet;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.json;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.notFound;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;

import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;

public class HttpClientStoryServiceTest
{
    	private String BASE_URL = "http://localhost/api";
	private String QUEUE_ID = "513c58da4df5b49e3d000001";
    	private String GET_ALL_REQUEST_URL = BASE_URL + "/queue/" + QUEUE_ID;
	private String STORY_ID_1 = "513c5eef4df5b49e3d000002";
	private String STORY_ID_2 = "513c5eef4df5b49e3d000003";
	
	private String EMPTY_QUEUE = 
		"{ " +
		" \"name\": \"Queue\", " +
		"   \"stories\": [], " +
		"   \"_id\": \"513c58da4df5b49e3d000001\" " +
		" }";
	
	private String QUEUE_WITH_ONE_STORY =
		"{ " +
		" \"_id\": \"513c58da4df5b49e3d000001\", " +
		"  \"name\": \"Queue\", " +
		"  \"stories\": [ " +
		"    { " +
		"      \"reference\": \"S-1\", " +
		"      \"title\": \"T1\", " +
		"      \"author\": \"A1\", " +
		"      \"merged\": false, " +
		"      \"_id\": \"513c5eef4df5b49e3d000002\" " +
		"    } " +
		"  ] " +
		"}";

	private String QUEUE_WITH_MULTIPLE_STORIES =
		"{ " +
		" \"_id\": \"513c58da4df5b49e3d000001\", " +
		"  \"name\": \"Queue\", " +
		"  \"stories\": [ " +
		"    { " +
		"      \"reference\": \"S-1\", " +
		"      \"title\": \"T1\", " +
		"      \"author\": \"A1\", " +
		"      \"merged\": false, " +
		"      \"_id\": \"513c5eef4df5b49e3d000002\" " +
		"    }, " +
		"    { " +
		"      \"reference\": \"S-2\", " +
		"      \"title\": \"T2\", " +
		"      \"author\": \"A2\", " +
		"      \"merged\": true, " +
		"      \"_id\": \"513c5eef4df5b49e3d000003\" " +
		"    } " +
		"  ] " +
		"}";

	private HttpClient client;

	private HttpClientStoryService service;
	
	@Before
	public void setUp()
	{
		client = mock(HttpClient.class);
		service = new HttpClientStoryService(client, BASE_URL);
	}

	@Test
	public void getAllWhenNoStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenReturn(json(EMPTY_QUEUE));

		assertEquals(emptyList(), service.getAll(QUEUE_ID));
	}

	@Test
	public void getAllWhenStory() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenReturn(json(QUEUE_WITH_ONE_STORY));

		assertEquals(new Story(STORY_ID_1, "S-1", "T1", "A1", false), service.getAll(QUEUE_ID).get(0));
	}

	@Test
	public void getAllWhenStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenReturn(json(QUEUE_WITH_MULTIPLE_STORIES));

		assertEquals(new Story(STORY_ID_1, "S-1", "T1", "A1", false), service.getAll(QUEUE_ID).get(0));
		assertEquals(new Story(STORY_ID_2, "S-2", "T2", "A2", true), service.getAll(QUEUE_ID).get(1));
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenReturn(notFound());

		service.getAll(QUEUE_ID);
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenThrow(new IOException());

		service.getAll(QUEUE_ID);
	}

	@Test
	public void getMergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenReturn(json(QUEUE_WITH_MULTIPLE_STORIES));

		List<Story> results = service.getMerged(QUEUE_ID);
		assertEquals(1, results.size());
		assertEquals(new Story(STORY_ID_2, "S-2", "T2", "A2", true), results.get(0));
	}

	@Test
	public void getUnmergedStories() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet(GET_ALL_REQUEST_URL))))
			.thenReturn(json(QUEUE_WITH_MULTIPLE_STORIES));

		List<Story> results = service.getUnmerged(QUEUE_ID);
		assertEquals(1, results.size());
		assertEquals(new Story(STORY_ID_1, "S-1", "T1", "A1", false), results.get(0));
	}

}
