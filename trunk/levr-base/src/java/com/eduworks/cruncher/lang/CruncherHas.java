package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherHas extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(parameters, dataStreams);
		if (obj == null)
			return false;
		if (obj instanceof String)
			return obj.toString().indexOf(getAsString("has",parameters,dataStreams)) != -1;
		if (obj instanceof JSONObject)
		{
			JSONObject obje = (JSONObject) obj;
			String has = getAsString("has", parameters, dataStreams);
			if (obje.has(has))
				return true;
			return false;
		}
		{
			JSONArray obje = (JSONArray) obj;
			String has = getAsString("has", parameters, dataStreams);
			for (int i = 0; i < obje.length(); i++)
			{
				if (obje.get(i).equals(has))
					return true;
			}
			return false;
		}
	}

	@Override
	public String getDescription()
	{
		return "Determines if a JSONObject has a key, or a JSONArray has an item at a particular index designated by 'has'";
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
		return jo("obj","JSONObject|JSONArray","has","String|Number");
	}

}
