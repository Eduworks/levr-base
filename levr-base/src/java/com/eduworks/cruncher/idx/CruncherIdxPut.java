package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.HTreeMap;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxPut extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c, parameters, dataStreams);
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		String key = getAsString("key", c, parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			HTreeMap<Object, Object> hashMap = ewDB.db.getHashMap(index);
			ewDB.writeCount.incrementAndGet();
			JSONObject jo = null;
			if (hashMap.containsKey(key) == false)
				jo = new JSONObject();
			else
				jo = (JSONObject) EwJson.getJsonObject(hashMap.get(key).toString());
			if (obj != null)
			{
				for (String keyx : EwJson.getKeys(obj))
				{
					if (keyx.equals("indexDir")) continue;
					if (keyx.equals("databaseName")) continue;
					if (keyx.equals("index")) continue;
					if (keyx.equals("key")) continue;
					jo.put(keyx, obj.get(keyx));
				}
			}
			else
				for (String keyx : keySet())
				{
					if (keyx.equals("indexDir")) continue;
					if (keyx.equals("databaseName")) continue;
					if (keyx.equals("index")) continue;
					if (keyx.equals("key")) continue;
					jo.put(keyx, get(keyx, c, parameters, dataStreams));
				}
			hashMap.put(key,jo.toString());
			return jo;
		}
		finally
		{
			if (optCommit)
				ewDB.db.commit();
			if (ewDB != null)
				ewDB.close();
		}
	}

	@Override
	public String getDescription()
	{
		return "Modifies an object being kept on an on-disk multimap defined by indexDir+databaseName->index->key = value\nWill convert into a string if not already.";
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
		return jo("obj","Object","indexDir","LocalPathString","databaseName","String","index","String","key","String");
	}

}
