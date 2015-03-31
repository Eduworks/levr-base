package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.Fun;
import org.mapdb.Fun.Tuple2;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherIdxKeys extends Cruncher
{

	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			if (optCommit)
				ewDB.db.commit();
			return new JSONArray(ewDB.db.getHashMap(index).keySet());
		}catch(ClassCastException e){
			NavigableSet<Fun.Tuple2<String,String>> multiMap = ewDB.db.getTreeSet(index);
			
			EwJsonArray arr = new EwJsonArray();
			
			Iterator<Tuple2<String, String>> it = multiMap.iterator();
			while(it.hasNext()){
				Tuple2<String, String> t = it.next();
				
				if(!arr.contains(t.a))
					arr.put(t.a);
			}
			
			return arr;
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
