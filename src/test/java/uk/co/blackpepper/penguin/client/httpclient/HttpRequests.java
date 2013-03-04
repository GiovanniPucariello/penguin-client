package uk.co.blackpepper.penguin.client.httpclient;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class HttpRequests
{
	private HttpRequests()
	{
		throw new AssertionError();
	}
	
	public static Matcher<HttpUriRequest> matchesGet(String uri) throws URISyntaxException
	{
		return matchesRequest("GET", uri);
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
}
