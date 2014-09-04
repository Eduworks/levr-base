package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherObjectPivot extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(parameters, dataStreams);
		String key = getAsString("key",parameters,dataStreams);
		JSONObject result = new JSONObject();
		Object object = obj.get(key);
		obj.remove(key);
		result.put(object.toString(), obj);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Wraps an object, taking a variable from the object defined 'key' and placing it as the key to the value, which becomes the object.";
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
		return jo("obj","JSONObject","key","String");
	}

}
