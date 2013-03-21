package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.QueueService;
import uk.co.blackpepper.penguin.client.ServiceException;

public class HttpClientQueueService extends AbstractHttpClientService implements QueueService
{
	private static final String QUEUES_URL = "%s/queues";

	private static final String QUEUE_URL = "%s/queue/%s";

	public HttpClientQueueService(HttpClient client, String serviceUrl)
	{
		super(client, serviceUrl);
	}

	@Override
	public List<Queue> getAll() throws ServiceException
	{
		HttpGet get = getJson(String.format(QUEUES_URL, getServiceUrl()));

		try
		{
			HttpResponse response = getClient().execute(get);
			checkOk(response, "Error getting queues");

			return Arrays.asList(fromJson(response, Queue[].class));
		}
		catch (IOException exception)
		{
			throw new ServiceException("Error getting queues", exception);
		}
	}

	@Override
	public Queue get(String id) throws ServiceException
	{
		HttpGet get = getJson(String.format(QUEUE_URL, getServiceUrl(), id));

		try
		{
			HttpResponse response = getClient().execute(get);
			checkOk(response, "Error getting queue " + id);

			return fromJson(response, Queue.class);
		}
		catch (IOException exception)
		{
			throw new ServiceException("Error getting queue " + id, exception);
		}
	}
}
