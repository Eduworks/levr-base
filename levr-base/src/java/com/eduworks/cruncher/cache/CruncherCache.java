package com.eduworks.cruncher.cache;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherCache extends Cruncher
{
	public static Map<String, Object> obj = Collections.synchronizedMap(new HashMap<String, Object>());

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String cacheName = getAsString("name", parameters, dataStreams);
		Object result = null;
		Object lock = null;
		if (optAsBoolean("removeAllGlobal", false, parameters, dataStreams))
		{
			Resolver.clearCache();
			return null;
		}
		synchronized (this.getClass())
		{
			lock = obj.get(cacheName);
			if (lock == null)
			{
				obj.put(cacheName, lock = new Object());
			}
		}
		synchronized (lock)
		{
			if (optAsBoolean("remove", false, parameters, dataStreams))
			{
				if (optAsBoolean("global", false, parameters, dataStreams))
					Resolver.putCache(cacheName, null);
				else
					Resolver.putThreadCache(cacheName, null);
			}
			else
			{
				if (optAsBoolean("global", false, parameters, dataStreams))
					result = Resolver.getCache(cacheName);
				else
					result = Resolver.getThreadCache(cacheName);
				if (result == null)
				{
					result = getObj(parameters, dataStreams);
					if (optAsBoolean("global", false, parameters, dataStreams))
						Resolver.putCache(cacheName, result);
					else
						Resolver.putThreadCache(cacheName, result);
				}
			}
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Caches a result, and fetches it automatically if it is in cache. Use Name to specify cache key.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "Object", "name", "String");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}
