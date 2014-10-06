package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherToLower extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String asString = getAsString("obj",parameters,dataStreams);
		if (asString == null) return null;
		return asString.toLowerCase();
	}

	@Override
	public String getDescription()
	{
		return "Converts all characters in a string to lowercase.";
	}

	@Override
	public String getReturn()
	{
		return "String";
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
