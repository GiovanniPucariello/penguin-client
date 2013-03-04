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

import static uk.co.blackpepper.penguin.client.httpclient.HttpRequests.matchesRequest;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.jsonResponse;
import static uk.co.blackpepper.penguin.client.httpclient.HttpResponses.notFound;

public class HttpClientQueueServiceTest
{
    private HttpClient client;
    
    private HttpClientQueueService service;
    
    @Before
    public void setUp()
    {
        client = mock(HttpClient.class);
        service = new HttpClientQueueService(client, "http://localhost:8080/api");
    }
    
    @Test
    public void getAllWhenNoQueues() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(jsonResponse("[]"));
        
        assertEquals(emptyList(), service.getAll());
    }
    
    @Test
    public void getAllWhenQueue() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(jsonResponse("[{_id: 1, name: A}]"));
        
        assertEquals(singletonList(new Queue("1", "A")), service.getAll());
    }
    
    @Test
    public void getAllWhenQueues() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(jsonResponse("[{_id: 1, name: A}, {_id: 2, name: B}]"));
        
        assertEquals(asList(new Queue("1", "A"), new Queue("2", "B")), service.getAll());
    }
    
    @Test(expected = ServiceException.class)
    public void getAllWhenNotFound() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(notFound());
        
        service.getAll();
    }
    
    @Test(expected = ServiceException.class)
    public void getAllWhenIOException() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenThrow(new IOException());
        
        service.getAll();
    }
}
