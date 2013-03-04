package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import uk.co.blackpepper.penguin.client.Queue;
import uk.co.blackpepper.penguin.client.ServiceException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void getAllWhenNoQueues() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(createJsonResponse("[]"));
        
        assertEquals(Collections.emptyList(), service.getAll());
    }
    
    @Test
    public void getAllWhenQueue() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(createJsonResponse("[{_id: 1, name: A}]"));
        
        List<Queue> expecteds = Collections.singletonList(new Queue("1", "A"));
        
        assertEquals(expecteds, service.getAll());
    }
    
    @Test
    public void getAllWhenQueues() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(createJsonResponse("[{_id: 1, name: A}, {_id: 2, name: B}]"));
        
        List<Queue> expecteds = Arrays.asList(new Queue("1", "A"), new Queue("2", "B"));
        
        assertEquals(expecteds, service.getAll());
    }
    
    @Test(expected = ServiceException.class)
    public void getAllWhenNotFound() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenReturn(createNotFound());
        
        service.getAll();
    }
    
    @Test(expected = ServiceException.class)
    public void getAllWhenIOException() throws ServiceException, IOException, URISyntaxException
    {
        when(client.execute(argThat(matchesRequest("GET", "http://localhost:8080/api/queues"))))
            .thenThrow(new IOException());
        
        service.getAll();
    }
    
    private static Matcher<HttpUriRequest> matchesRequest(String method, String uri) throws URISyntaxException
    {
    	return matchesRequest(method, new URI(uri));
    }
    
    private static Matcher<HttpUriRequest> matchesRequest(final String method, final URI uri)
    {
    	return new TypeSafeMatcher<HttpUriRequest>()
		{
    		@Override
    		protected boolean matchesSafely(HttpUriRequest request)
    		{
    			return method.equals(request.getMethod())
    				&& uri.equals(request.getURI());
    		}
    		
    		@Override
    		public void describeTo(Description description)
    		{
    			description.appendText("request ")
    				.appendValue(method)
    				.appendText(" ")
    				.appendValue(uri);
    		}
		};
    }
    
    private static HttpResponse createJsonResponse(String json)
    {
        return createResponse(new StringEntity(json, ContentType.APPLICATION_JSON));
    }
    
    private static HttpResponse createResponse(HttpEntity entity)
    {
        return createResponse(HttpStatus.SC_OK, entity);
    }
    
    private static HttpResponse createResponse(int statusCode, HttpEntity entity)
    {
        ProtocolVersion version = new ProtocolVersion("HTTP", 1, 0);
		StatusLine statusLine = new BasicStatusLine(version, statusCode, null);
		
        BasicHttpResponse response = new BasicHttpResponse(statusLine);
        response.setEntity(entity);
        return response;
    }
    
    private static HttpResponse createNotFound()
    {
        return createResponse(HttpStatus.SC_NOT_FOUND, null);
    }
    
}
