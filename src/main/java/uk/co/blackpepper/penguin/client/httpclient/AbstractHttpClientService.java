package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;

import uk.co.blackpepper.penguin.client.ServiceException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public abstract class AbstractHttpClientService
{
	private final HttpClient client;

	private final String apiUrl;

	private final Gson gson;

	public AbstractHttpClientService(HttpClient client, String apiUrl)
	{
		this.client = client;
		this.apiUrl = apiUrl;
		
		gson = new Gson();
	}
	
	public HttpClient getClient()
	{
		return client;
	}
	
	public String getApiUrl()
	{
		return apiUrl;
	}

	protected void checkOk(HttpResponse response, String message) throws ServiceException
	{
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_OK)
		{
			throw new ServiceException(message + ": " + statusCode);
		}
	}

	protected <T> T fromJson(HttpResponse response, Class<T> type) throws ServiceException, IOException
	{
		HttpEntity entity = response.getEntity();
		InputStreamReader content = new InputStreamReader(entity.getContent());

		try
		{
			return gson.fromJson(content, type);
		}
		catch (JsonParseException exception)
		{
			throw new ServiceException("Error parsing JSON", exception);
		}
	}
}
