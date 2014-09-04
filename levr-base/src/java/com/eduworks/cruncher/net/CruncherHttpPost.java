package com.eduworks.cruncher.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Cruncher;

public class CruncherHttpPost extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final Object o = getObj(parameters, dataStreams);
		String url = getAsString("url", parameters, dataStreams);
		String name = getAsString("name", parameters, dataStreams);
		String contentType = getAsString("contentType", parameters, dataStreams);
//		String accept = getAsString("accept", parameters, dataStreams);
		boolean multiPart = optAsBoolean("multipart", true, parameters, dataStreams);
		String authToken = getAsString("authToken", parameters, dataStreams);
		HttpPost post = new HttpPost(url);
		
		HttpEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
		 
		if (multiPart)
		try
		{
			((MultipartEntity) entity).addPart( name, new StringBody(o.toString(), contentType,Charset.defaultCharset()));
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		else
		{
			try
			{
				entity = new StringEntity(o.toString());
				post.setHeader("Content-Type", contentType);
//				if (accept != null)
//				post.setHeader("Accept", accept);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		
		if (authToken != null && !authToken.trim().isEmpty()) {
		   post.setHeader("Authorization", "Basic " + authToken);
		}
		
		post.setEntity(entity);
		
		HttpClient hc = new DefaultHttpClient();
		
		HttpResponse execute;
		try
		{
			execute = hc.execute(post);
			String string = EntityUtils.toString(execute.getEntity());
			if (EwJson.isJson(string))
				return EwJson.tryParseJson(string, false);
			return string;
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getDescription()
	{
		return "Performs an HTTP Post. The payload is provided by obj.\n" +
				"Will attach one file as a payload or multi-part mime message. (use multipart=true, name, and contentType)\n" +
				"Results will come back as JSON or a string.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject|JSONArray|String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String","contentType","String","?multipart","Boolean","?name","String", "?authToken", "String");
	}

}
