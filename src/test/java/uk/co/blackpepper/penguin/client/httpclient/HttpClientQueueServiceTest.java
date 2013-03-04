package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    
    private static HttpResponse jsonResponse(String json)
    {
        return response(new StringEntity(json, ContentType.APPLICATION_JSON));
    }
    
    private static HttpResponse response(HttpEntity entity)
    {
        return response(HttpStatus.SC_OK, entity);
    }
    
    private static HttpResponse response(int statusCode, HttpEntity entity)
    {
        ProtocolVersion version = new ProtocolVersion("HTTP", 1, 0);
		StatusLine statusLine = new BasicStatusLine(version, statusCode, null);
		
        BasicHttpResponse response = new BasicHttpResponse(statusLine);
        response.setEntity(entity);
        return response;
    }
    
    private static HttpResponse notFound()
    {
        return response(HttpStatus.SC_NOT_FOUND, null);
    }
    
}
