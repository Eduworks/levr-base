package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherSplit extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String asString = getAsString("obj",parameters, dataStreams);
		if (asString == null) return null;
		if (asString.isEmpty()) return new JSONArray();
		if (optAsBoolean("newline",false,parameters,dataStreams))
			return new JSONArray(asString.split("\r?\n"));
		String[] split = asString.toString().split(Pattern.quote(getAsString("split",parameters,dataStreams)));
		return new JSONArray(split);
	}

	@Override
	public String getDescription()
	{
		return "Splits a string by some other string.";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String","split","String");
	}

}
