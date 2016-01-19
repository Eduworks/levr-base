package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRemove extends Cruncher
{
	static boolean notified = false;

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (!notified)
		{
			Logger.getLogger(getClass())
					.info("This class has recently been converted to a Cruncher by Fritz. It was rewritten. Please ensure it is still meeting your purpose, and extend it with the necessary functionality if not.");
			notified = true;
		}

		Object o = getObj(c, parameters, dataStreams);
		if (o instanceof JSONObject)
		{
			JSONObject jo = (JSONObject) o;
			for (String key : keySet())
				if (key.equals("item"))
					jo.remove(jo.getString(key));
				else
					jo.remove(key);
			return jo;
		}
		if (o instanceof JSONArray)
		{
			// JSONArray ja = new JSONArray();
			EwList<Object> arr = new EwList<Object>(o);

			for (String key : keySet())
				if (!isSetting(key))
					arr.remove(key);
				else
				{
					Object o2 = get(key, c, parameters, dataStreams);
					try
					{
						int index = Integer.parseInt(o2.toString());
						arr.remove(index);
					}
					catch (NumberFormatException ex)
					{
						arr.remove(o2);
					}
				}
			return new JSONArray(arr);
		}
		return o;
	}

	@Override
	public String getDescription()
	{
		return "Removes a variable from an object defined by any parameters contained.";
	}

	@Override
	public String[] getResolverNames()
	{
		return new String[] { getResolverName(), "remove" };
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
		return jo("obj", "JSONObject", "?item", "JSONObject", "<any>", "Object");
	}

}
