package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;

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

public class HttpClientQueueServiceTest
{
	private HttpClient client;

	private HttpClientQueueService service;

	@Before
	public void setUp()
	{
		client = mock(HttpClient.class);
		service = new HttpClientQueueService(client, "http://localhost/api");
	}

	@Test
	public void getAllWhenNoQueues() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queues"))))
			.thenReturn(json("[]"));

		assertEquals(emptyList(), service.getAll());
	}

	@Test
	public void getAllWhenQueue() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queues"))))
			.thenReturn(json("[{_id: 1, name: A}]"));

		assertEquals(singletonList(new Queue("1", "A")), service.getAll());
	}

	@Test
	public void getAllWhenQueues() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queues"))))
			.thenReturn(json("[{_id: 1, name: A}, {_id: 2, name: B}]"));

		assertEquals(asList(new Queue("1", "A"), new Queue("2", "B")), service.getAll());
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queues"))))
			.thenReturn(notFound());

		service.getAll();
	}

	@Test(expected = ServiceException.class)
	public void getAllWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queues"))))
			.thenThrow(new IOException());

		service.getAll();
	}

	@Test
	public void getWhenQueue() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(json("{_id: 1, name: A}"));

		assertEquals(new Queue("1", "A"), service.get("1"));
	}

	@Test(expected = ServiceException.class)
	public void getWhenNotFound() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenReturn(notFound());

		service.get("1");
	}

	@Test(expected = ServiceException.class)
	public void getWhenIOException() throws ServiceException, IOException, URISyntaxException
	{
		when(client.execute(argThat(matchesGet("http://localhost/api/queue/1"))))
			.thenThrow(new IOException());

		service.get("1");
	}
}
