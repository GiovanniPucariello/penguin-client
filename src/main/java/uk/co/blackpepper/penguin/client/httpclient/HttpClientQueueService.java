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
            
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode != HttpStatus.SC_OK)
            {
                throw new ServiceException("Error getting queues: " + statusCode);
            }
            
            HttpEntity entity = response.getEntity();
            InputStreamReader content = new InputStreamReader(entity.getContent());
            
            Queue[] queues = gson.fromJson(content, Queue[].class);
            
            return Arrays.asList(queues);
        }
        catch (IOException exception)
        {
            throw new ServiceException("Error getting queues", exception);
        }
    }
}
