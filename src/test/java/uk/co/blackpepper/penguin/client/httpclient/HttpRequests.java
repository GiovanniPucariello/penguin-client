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
		return matchesRequest(method, uri, APPLICATION_JSON);
	}

	// TODO This could be extended to match against any number of headers,
	// but all we need now is to match against the accept header.
	private static Matcher<HttpUriRequest> matchesRequest(final String method, final URI uri,
		final String acceptHeader)
	{
		return new TypeSafeMatcher<HttpUriRequest>()
		{
			@Override
			protected boolean matchesSafely(HttpUriRequest request)
			{
				if (null != acceptHeader)
				{
					Header[] headers = request.getHeaders(HttpHeaders.ACCEPT);
					if (headers.length != 1 || !headers[0].getValue().equals(acceptHeader))
					{
						return false;
					}
				}

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
