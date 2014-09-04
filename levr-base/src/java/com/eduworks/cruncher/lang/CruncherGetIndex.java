package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherGetIndex extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (optAsString("soft", "false", parameters, dataStreams).equals("true") && !(getObj(parameters, dataStreams) instanceof JSONArray))
			return null;
		Object objx = getObj(parameters, dataStreams);
		if (objx instanceof List)
		{
			List obj = (List) objx;
			if (obj == null)
				return null;
			int key = -1;
			if (get("index", parameters, dataStreams) instanceof Double)
				key = ((Double) get("index", parameters, dataStreams)).intValue();
			else
				key = Integer.parseInt(getAsString("index", parameters, dataStreams));
			if (obj.size() > key)
				return obj.get(key);
		}
		JSONArray obj = (JSONArray) objx;
		if (obj == null)
			return null;
		int key = -1;
		if (get("index", parameters, dataStreams) instanceof Double)
			key = ((Double) get("index", parameters, dataStreams)).intValue();
		else
			key = Integer.parseInt(getAsString("index", parameters, dataStreams));
		if (obj.length() > key)
			return obj.get(key);
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Indexes into an array and retreives the item at the index designated by 'index'.\nSoft will make it not error out if obj is not a JSONArray";
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
		return jo("obj", "JSONArray", "index", "Number", "?soft", "Boolean");
	}
}
