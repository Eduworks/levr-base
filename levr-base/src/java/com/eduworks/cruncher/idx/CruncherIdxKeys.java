package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxKeys extends Cruncher
{

	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", parameters, dataStreams));
		String index = Resolver.decodeValue(getAsString("index", parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			if (optCommit)
				ewDB.db.commit();
			return new JSONArray(ewDB.db.getHashMap(index).keySet());
		}
		finally
		{
			if (ewDB != null)
				ewDB.close();
		}
	}

	@Override
	public String getDescription()
	{
		return "Returns all keys of a string only on-disk multimap defined by indexDir+databaseName->index->key += value.";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir","LocalPathString","databaseName","String","index","String");
	}

}
