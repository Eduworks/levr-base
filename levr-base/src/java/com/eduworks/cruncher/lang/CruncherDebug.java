package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherDebug extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(parameters,dataStreams);
		if (obj instanceof JSONArray)
			System.out.println(((JSONArray)obj).toString(5));
		else if (obj instanceof JSONObject)
			System.out.println(((JSONObject)obj).toString(5));
		else
			System.out.println(obj.toString());
		return obj;
	}

	@Override
	public String getDescription()
	{
		return "Prints out the object and then returns it. Inject to see things happening in the console log.";
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
		return jo("obj","Object");
	}

}
