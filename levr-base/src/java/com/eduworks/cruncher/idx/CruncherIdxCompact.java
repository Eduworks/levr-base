package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;
import java.util.NavigableSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.Fun;

import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxCompact extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", parameters, dataStreams));

		EwDB ewDB = null;
		try
		{
			ewDB  = EwDB.get(_databasePath, _databaseName);
			ewDB.compact();
		}
		finally
		{
			ewDB.db.commit();
			if (ewDB != null)
				ewDB.close();
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Compacts an IDX Database.";
	}

	@Override
	public String getReturn()
	{
		return "null";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir","String","databaseName","String");
	}

}
