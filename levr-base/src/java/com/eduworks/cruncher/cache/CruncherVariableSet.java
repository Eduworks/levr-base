package com.eduworks.cruncher.cache;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;
import com.eduworks.resolver.Cruncher;

public class CruncherVariableSet extends Cruncher
{
	public static Map<String,Object> cache = new EwMap<String,Object>();
	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(parameters, dataStreams);
		cache.put(getAsString("key",parameters,dataStreams),obj);
		return obj;
	}
	@Override
	public String getDescription()
	{
		return "Places a variable into a local state referenced by 'key'. Use when in-memory state is absolutely required.";
	}
	@Override
	public String getReturn()
	{
		return "Object";
	}
	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Object","key","String");
	}

}
