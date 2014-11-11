package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherCall extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
		for (String key : keySet())
		{
			if (key.equals("obj"))
				continue;
			if (isSetting(key))
				continue;
			Object value;
				value = get(key,c,parameters, dataStreams);
			if (value != null)
			{
				String valueString = value.toString();
				EwCache.getCache("callCache").put(valueString, value);
				newParams.put(key, new String[] { valueString });
			}
			else
			{
			}
		}
		Object result = resolveAChild("obj", c,newParams, dataStreams);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Moves parameters of this function into the @ parameters";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Resolvable","<any>","Object");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
}
