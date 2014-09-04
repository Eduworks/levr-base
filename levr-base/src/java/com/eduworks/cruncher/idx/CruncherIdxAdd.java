package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;
import java.util.NavigableSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.Fun;

import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxAdd extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", parameters, dataStreams));
		String index = Resolver.decodeValue(getAsString("index", parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, parameters, dataStreams);
		String key = getAsString("key", parameters, dataStreams);
		Object value = get("value", parameters, dataStreams);
		if (value == null)
			value = getObj(parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);
			
			NavigableSet<Fun.Tuple2<String,String>> multiMap = ewDB.db.getTreeSet(index);
			ewDB.writeCount.incrementAndGet();
			multiMap.add(Fun.t2(key, value.toString()));
		}
		finally
		{
			if (optCommit)
				ewDB.db.commit();
			if (ewDB != null)
				ewDB.close();
		}
		return value;
	}

	@Override
	public String getDescription()
	{
		return "Adds obj to a string only on-disk multimap defined by indexDir+databaseName->index->key += value\nWill convert into a string if not already.";
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
