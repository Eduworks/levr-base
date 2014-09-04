package com.eduworks.cruncher.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Cruncher;

public class CruncherHttpGet extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final String url = getObj(parameters, dataStreams).toString();
		HttpGet get = new HttpGet(url);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = Integer.parseInt(optAsString("timeout","60000",parameters,dataStreams));
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = Integer.parseInt(optAsString("timeout","60000",parameters,dataStreams));;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient hc = new DefaultHttpClient(httpParameters);
		
		HttpResponse execute;
		try
		{
			execute = hc.execute(get);
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
		return "Fetches a web page using an HTTP Get. URL is to be placed in 'obj'. Will auto convert result to JSON if possible.";
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
		return jo("obj","String");
	}

}
