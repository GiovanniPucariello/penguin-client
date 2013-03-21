package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;
import uk.co.blackpepper.penguin.client.StoryService;

public class HttpClientStoryService extends AbstractHttpClientService implements StoryService
{
	// TODO: use /queue/:id/stories when implemented
	private static final String STORIES_URL = "%s/queue/%s";

	public HttpClientStoryService(HttpClient client, String serviceUrl)
	{
		super(client, serviceUrl);
	}

	@Override
	public List<Story> getAll(String queueId) throws ServiceException
	{
		HttpGet get = getJson(String.format(STORIES_URL, getServiceUrl(), queueId));

		try
		{
			HttpResponse response = getClient().execute(get);
			checkOk(response, "Error getting stories");

			return fromJson(response, Queue.class).getStories();
		}
		catch (IOException exception)
		{
			throw new ServiceException("Error getting stories", exception);
		}
	}

	@Override
	public List<Story> getMerged(String queueId) throws ServiceException
	{
		return filterByMerged(getAll(queueId), true);
	}

	@Override
	public List<Story> getUnmerged(String queueId) throws ServiceException
	{
		return filterByMerged(getAll(queueId), false);
	}
	
	private static List<Story> filterByMerged(List<Story> stories, boolean merged)
	{
		List<Story> filteredStories = new ArrayList<Story>();
		
		for (Story story : stories)
		{
			if (story.isMerged() == merged)
			{
				filteredStories.add(story);
			}
		}
		
		return filteredStories;
	}
}
