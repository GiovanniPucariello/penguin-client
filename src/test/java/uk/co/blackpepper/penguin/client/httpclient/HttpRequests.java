package uk.co.blackpepper.penguin.client.httpclient;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class HttpRequests
{
	private static final String APPLICATION_JSON = "application/json";
	
	private HttpRequests()
	{
		throw new AssertionError();
	}

	public static Matcher<HttpUriRequest> matchesGetJson(String uri) throws URISyntaxException
	{
		return matchesRequest("GET", new URI(uri), APPLICATION_JSON);
	}

	private static Matcher<HttpUriRequest> matchesRequest(final String method, final URI uri, final String accept)
	{
		return new TypeSafeMatcher<HttpUriRequest>()
		{
			@Override
			protected boolean matchesSafely(HttpUriRequest request)
			{
				return method.equals(request.getMethod())
					&& uri.equals(request.getURI())
					&& accept.equals(getFirstHeaderValue(request, HttpHeaders.ACCEPT));
			}

			@Override
			public void describeTo(Description description)
			{
				description.appendText("request ")
					.appendValue(method)
					.appendText(" ")
					.appendValue(uri)
					.appendText(" (Accept: ")
					.appendValue(accept)
					.appendText(")");
			}
		};
	}
	
	private static String getFirstHeaderValue(HttpUriRequest request, String name)
	{
		Header header = request.getFirstHeader(name);
		
		return (header != null) ? header.getValue() : null;
	}

	public static Matcher<HttpUriRequest> matchesPostRequest(String uri, String body) throws URISyntaxException
	{
	    	return matchesPostRequest(new URI(uri), body);
	}
	
	private static Matcher<HttpUriRequest> matchesPostRequest(final URI uri, final String body)
	{
		return new TypeSafeMatcher<HttpUriRequest>()
		{
			@Override
			protected boolean matchesSafely(HttpUriRequest request)
			{
			    	try
			    	{
				    	HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase)request;
				    	StringWriter writer = new StringWriter();
				    	IOUtils.copy(entityRequest.getEntity().getContent(), writer, "UTF-8");
				    	String requestBody = writer.toString();
				    	
					return "POST".equals(request.getMethod())
						&& uri.equals(request.getURI())
						&& body.equals(requestBody);
			    	}
			    	catch (Exception e) 
			    	{
			    	    return false;
				}
			}

			@Override
			public void describeTo(Description description)
			{
				description.appendText("request ")
					.appendValue("POST")
					.appendText(" ")
					.appendValue(uri)
					.appendText(" (Body: ")
					.appendValue(body)
					.appendText(")");
			}
		};
	}
}
