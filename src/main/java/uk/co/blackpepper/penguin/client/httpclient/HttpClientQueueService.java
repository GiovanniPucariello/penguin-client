package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.QueueService;
import uk.co.blackpepper.penguin.client.ServiceException;

import com.google.gson.Gson;

public class HttpClientQueueService implements QueueService
{
    private final HttpClient client;
    
    private final String apiUrl;
    
    private final Gson gson;

    public HttpClientQueueService(HttpClient client, String apiUrl)
    {
        this.client = client;
        this.apiUrl = apiUrl;
        
        gson = new Gson();
    }
    
    @Override
    public List<Queue> getAll() throws ServiceException
    {
        HttpGet get = new HttpGet(apiUrl + "/queues");
        
        try
        {
            HttpResponse response = client.execute(get);
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
        HttpGet get = new HttpGet(String.format("%s/queue/%s", apiUrl, id));
        
        try
        {
            HttpResponse response = client.execute(get);
            checkOk(response, "Error getting queue " + id);
            
            return fromJson(response, Queue.class);
        }
        catch (IOException exception)
        {
            throw new ServiceException("Error getting queue " + id, exception);
        }
    }
    
    private void checkOk(HttpResponse response, String message) throws ServiceException
    {
        int statusCode = response.getStatusLine().getStatusCode();
        
        if (statusCode != HttpStatus.SC_OK)
        {
            throw new ServiceException(message + ": " + statusCode);
        }
    }
    
    private <T> T fromJson(HttpResponse response, Class<T> type) throws IOException
    {
        HttpEntity entity = response.getEntity();
        InputStreamReader content = new InputStreamReader(entity.getContent());
        
        return gson.fromJson(content, type);
    }
}
