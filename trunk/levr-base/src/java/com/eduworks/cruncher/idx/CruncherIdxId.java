package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxId extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, parameters, dataStreams);
		String index = Resolver.decodeValue(getAsString("index", parameters, dataStreams));
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			return ewDB.db.getAtomicInteger(index).getAndIncrement();
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
		return "Returns a unique ID associated with an on-disk hash database.";
	}

	@Override
	public String getReturn()
	{
		return "Number";
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
