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

	public HttpClientStoryService(HttpClient client, String apiUrl)
	{
		super(client, apiUrl);
	}

	@Override
	public List<Story> getAll(String queueId) throws ServiceException
	{
		HttpGet get = new HttpGet(String.format(STORIES_URL, getApiUrl(), queueId));

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
		List<Story> allStories = getAll(queueId);
		List<Story> mergedStories = new ArrayList<Story>();
		
		for (Story story : allStories)
		{
			if (story.isMerged())
			{
				mergedStories.add(story);
			}
		}
		
		return mergedStories;
	}

	@Override
	public List<Story> getUnMerged(String queueId) throws ServiceException
	{
		List<Story> allStories = getAll(queueId);
		List<Story> unMergedStories = new ArrayList<Story>();
		
		for (Story story : allStories)
		{
			if (!story.isMerged())
			{
				unMergedStories.add(story);
			}
		}
		
		return unMergedStories;
	}
}
