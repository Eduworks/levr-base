package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherToBoolean extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(parameters, dataStreams);
		if (obj == null) return null;
		return Boolean.parseBoolean(obj.toString());
	}

	@Override
	public String getDescription()
	{
		return "Converts a String to a Boolean";
	}

	@Override
	public String getReturn()
	{
		return "Boolean";
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
