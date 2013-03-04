package uk.co.blackpepper.penguin.client.httpclient;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;

public class HttpClientQueueServiceTest
{
    private HttpClient client;
    
    private String apiUrl;
    
    private HttpClientQueueService service;
    
    @Before
    public void setUp()
    {
        client = mock(HttpClient.class);
        apiUrl = "http://localhost:8080/api";
        
        service = new HttpClientQueueService(client, apiUrl);
    }
    
    @Test
    public void getAll() throws ServiceException, IOException
    {
        when(client.execute(Mockito.any(HttpUriRequest.class)))
            .thenReturn(createJsonResponse("[{_id: 1, name: A}]"));
        
        List<Queue> expecteds = Collections.singletonList(new Queue("1", "A"));
        
        assertEquals(expecteds, service.getAll());
    }
    
    private static HttpResponse createJsonResponse(String json)
    {
        return createResponse(new StringEntity(json, ContentType.APPLICATION_JSON));
    }
    
    private static HttpResponse createResponse(HttpEntity entity)
    {
        StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 0), HttpStatus.SC_OK, null);
        return createResponse(statusLine, entity);
    }
    
    private static HttpResponse createResponse(StatusLine statusLine, HttpEntity entity)
    {
        BasicHttpResponse response = new BasicHttpResponse(statusLine);
        response.setEntity(entity);
        return response;
    }
}
