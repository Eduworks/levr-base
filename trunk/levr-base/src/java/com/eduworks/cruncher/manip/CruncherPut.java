package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherPut extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = getObjAsJsonObject(parameters, dataStreams);
		if (jo == null)
			jo = new JSONObject();
		for (String key : keySet())
		{
			if (isSetting(key))continue;
			if (key.equals("obj"))continue;
			jo.put(key,get(key,parameters,dataStreams));
		}
		String key = optAsString("_key", null, parameters, dataStreams);
		if (key != null)
			jo.put(key,get("_value",parameters,dataStreams));
		return jo;
	}

	@Override
	public String getDescription()
	{
		return "Puts a value into an object.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject","<any>","Any");
	}

}
