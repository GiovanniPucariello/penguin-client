package uk.co.blackpepper.penguin.client.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

final class HttpResponses
{
	private HttpResponses()
	{
		throw new AssertionError();
	}

	public static HttpResponse json(String json)
	{
		return ok(new StringEntity(json, ContentType.APPLICATION_JSON));
	}

	public static HttpResponse notFound()
	{
		return response(HttpStatus.SC_NOT_FOUND, null);
	}

	private static HttpResponse ok(HttpEntity entity)
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
}
