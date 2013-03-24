package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;
import uk.co.blackpepper.penguin.client.StoryService;

public class HttpClientStoryService extends AbstractHttpClientService implements StoryService
{
	// TODO: use /queue/:id/stories when implemented
	private static final String STORIES_URL = "%s/queue/%s";
	
	private static final String MERGE_URL = "%s/queue/%s/story/%s/merge";

	private static final String UNMERGE_URL = "%s/queue/%s/story/%s/unmerge";

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

	@Override
	public void merge(String queueId, String storyId) throws ServiceException 
	{
	    try
	    {
		HttpPost post = new HttpPost(String.format(MERGE_URL, getServiceUrl(), queueId, storyId));
		post.setEntity(new StringEntity("merge"));
		HttpResponse response = getClient().execute(post);
		checkNoContent(response, "Error merging stories");
	    }
	    catch (Exception exception)
	    {
		throw new ServiceException("Error merging story", exception);
	    }
	}

	@Override
	public void unmerge(String queueId, String storyId) throws ServiceException 
	{
	    try
	    {
		HttpPost post = new HttpPost(String.format(UNMERGE_URL, getServiceUrl(), queueId, storyId));
		post.setEntity(new StringEntity("unmerge"));
		HttpResponse response = getClient().execute(post);
		checkNoContent(response, "Error unmerging stories");
	    }
	    catch (Exception exception)
	    {
		throw new ServiceException("Error unmerging story", exception);
	    }
	}
}
