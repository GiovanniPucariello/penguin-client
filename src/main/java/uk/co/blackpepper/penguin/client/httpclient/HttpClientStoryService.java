package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;
import uk.co.blackpepper.penguin.client.Story;
import uk.co.blackpepper.penguin.client.StoryService;

import com.google.gson.Gson;

public class HttpClientStoryService implements StoryService
{
	private static final String STORIES_URL = "%s/queue/%s";

	private static final String STORY_URL = "%s/queue/%s/story/%s";

	private final HttpClient client;

	private final String apiUrl;

	private final Gson gson;

	public HttpClientStoryService(HttpClient client, String apiUrl)
	{
		this.client = client;
		this.apiUrl = apiUrl;

		gson = new Gson();
	}

	@Override
	public List<Story> getAll(String queueId) throws ServiceException
	{
		HttpGet get = new HttpGet(String.format(STORIES_URL, apiUrl, queueId));

		try
		{
			HttpResponse response = client.execute(get);
			checkOk(response, "Error getting stories");

			return fromJson(response);
		}
		catch (IOException exception)
		{
			throw new ServiceException("Error getting stories", exception);
		}
	}

	@Override
	public List<Story> getMerged(String queueId) throws ServiceException {
	    List<Story> allStories = getAll(queueId);
	    List<Story> mergedStories = new ArrayList<Story>();
	    for (Story story : allStories) {
		if (story.isMerged()) {
		    mergedStories.add(story);
		}
	    }
	    return mergedStories;
	}

	@Override
	public List<Story> getUnMerged(String queueId) throws ServiceException {
	    List<Story> allStories = getAll(queueId);
	    List<Story> unMergedStories = new ArrayList<Story>();
	    for (Story story : allStories) {
		if (!story.isMerged()) {
		    unMergedStories.add(story);
		}
	    }
	    return unMergedStories;
	}
	
	private void checkOk(HttpResponse response, String message) throws ServiceException
	{
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_OK)
		{
			throw new ServiceException(message + ": " + statusCode);
		}
	}

	private List<Story> fromJson(HttpResponse response) throws IOException
	{
		HttpEntity entity = response.getEntity();
		InputStreamReader content = new InputStreamReader(entity.getContent());

		// TODO The api does not currently return solely a list of stories for a queue.
		// Until it does we are using the queue api, which returns the queue and all stories in that queue.
		// When the responce is solely a list of stories for the queue we can do:
		//   return gson.fromJson(content, type);
		// Until the we must parse the queue to obtain the stories.
		
		Queue queue = gson.fromJson(content, Queue.class);
		
		return queue.getStories();
	}
}
