package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Cruncher;

public class CruncherOpt extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final boolean optEmpty = optAsBoolean("optEmpty", false, parameters, dataStreams);
		final Object obj = get("obj", parameters, dataStreams);

		if (has("opt") == false)
		{
			for (String key : keySet())
				if (!key.equals("obj"))
					if (obj instanceof JSONObject)
						if (((JSONObject)obj).has(key))
					return ((JSONObject)obj).get(key);
		}
		else if (obj == null)
		{
			Object object = get("opt",parameters, dataStreams);
			return object;
		}
		else if (optEmpty && EwJson.isJson(obj))
		{
			final EwJsonCollection json = EwJson.tryConvert(obj);
			if (json != null && json.isEmpty())
				return get("opt",parameters, dataStreams);
		}

		return obj;
	}

	@Override
	public String getDescription()
	{
		return "If obj has a parameter defined by opt='parameterName', will return that parameter, otherwise will return obj." +
				"\n If obj is null, will return the result in 'opt'." +
				"\n Probably a little broken. Try not to use it except in the first case.";
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
		return jo("obj","JSONObject|Object","opt","String|Object");
	}

}
