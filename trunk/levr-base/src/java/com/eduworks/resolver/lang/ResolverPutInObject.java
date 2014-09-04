package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.ResolverMatcher;
import com.eduworks.resolver.ResolverMerger;

public class ResolverPutInObject extends ResolverMatcher implements ResolverMerger
{
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"putInMap"};
	}
	@Override
	public Object resolve(Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(parameters, dataStreams);

		boolean remove = optAsBoolean("_remove",false,parameters);
		removeAllSettings();

		final Object item = getItem(parameters, false, false);
		final String dest = optAsString(DEST_KEY, parameters);
		final String destKey = (dest == null || dest.isEmpty()) ? null : dest;
		final boolean unique = optAsBoolean(UNIQUE_KEY, false, parameters);

		final EwJsonObject obj = getObject(parameters, true, true);

		if (remove)
			obj.remove(destKey);
		
		if (item == null)
			merge(obj, null, destKey, parameters);

		else if (destKey != null && !(unique && matches(obj, item)))
			obj.put(destKey, item);
		
		if (obj instanceof Resolver)
			((Resolver)obj).removeAllSettings();
		
		return obj;
	}

	@Override
	public Object merge(Object mergeTo, String srcKey, String destKey, Map<String,String[]> parameters)
			throws JSONException
	{
		/*
		 * If merging and destKey is not specified, json elements at key are merged at root level;
		 * if merging and destKey is specified, all keys and values are accumulated at destKey;
		 * otherwise, we are not merging: all keys and values are accumulated at root level.
		 */

		final boolean merge = optAsBoolean(MERGE_KEY, (destKey == null), parameters);

		if (mergeTo != null && length() > 0)
			for (String key : keySet())
				if (!skipKey(key))
					EwJson.tryMerge(mergeTo, get(key, parameters), (merge) ? destKey : key);

		return mergeTo;
	}

	private boolean skipKey(String key)
	{
		return (
				key == null ||
				key.equals(DEFAULT_OBJ_KEY) ||
				key.equals(DEFAULT_ITEM_KEY) ||
				key.equals(DEST_KEY) ||
				key.equals(MERGE_KEY) ||
				key.equals(UNIQUE_KEY)
			);
	}
	@Override
	public String getDescription()
	{
		return "Will return an object that has an added variable, defined by 'dest' and 'item'";
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
		return jo("obj","JSONObject","dest","String","item","Object");
	}
}
