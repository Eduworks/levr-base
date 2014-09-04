package com.eduworks.cruncher.io;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherDisplayText extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		StringBuilder sb = new StringBuilder();
		
		Object o = getObj(parameters,dataStreams);
		parse(sb,o);
		
		InMemoryFile f = new InMemoryFile();
		f.data = sb.toString().getBytes();
		f.mime = "text/plain";
		f.name = getAsString("name", parameters,dataStreams);
		return f;
	}

	private void parse(StringBuilder sb, Object o) throws JSONException
	{
		if (o instanceof String)
		{
			sb.append(o);
			sb.append(" ");
		}
		else if (o instanceof JSONArray)
		{
			JSONArray ja = (JSONArray) o;
			for (int i = 0;i < ja.length();i++)
				parse(sb,ja.get(i));
			sb.append("\n");
		}
	}

	@Override
	public String getDescription()
	{
		return "Returns a file of type text/plain using 'obj' as the contents and 'name' as the filename.";
	}

	@Override
	public String getReturn()
	{
		return "ResolverFile";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String","name","String");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}
