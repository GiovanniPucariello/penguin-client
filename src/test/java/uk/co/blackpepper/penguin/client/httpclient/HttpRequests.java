package uk.co.blackpepper.penguin.client.httpclient;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
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
}
