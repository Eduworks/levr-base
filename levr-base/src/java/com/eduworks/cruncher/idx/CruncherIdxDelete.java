package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxDelete extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		String key = getAsString("key", c, parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			ewDB.compact = true;
			return ewDB.db.getHashMap(index).remove(key);
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
		// TODO Auto-generated method stub
		return "Removed from a string only on-disk multimap defined by indexDir+databaseName->index->key += value. Returns value (as String).";
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
		return jo("indexDir","LocalPathString","databaseName","String","index","String","key","String");
	}
}
