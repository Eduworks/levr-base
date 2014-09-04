package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.Tuple;

public class CruncherSortBy extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray objAsJsonArray = getObjAsJsonArray(parameters, dataStreams);
		EwList<String> ja = new EwList<String>(objAsJsonArray);
		JSONArray sortBy = getAsJsonArray("by", parameters, dataStreams);
		final List<Tuple<String,Double>> map = new EwList<Tuple<String,Double>>();
		
		JSONArray results = new JSONArray();
		if (optAsBoolean("byNumbers", false, parameters, dataStreams))
		{
			for (int i = 0;i < ja.size();i++)
				map.add(new Tuple<String,Double>(ja.get(i), Double.parseDouble(sortBy.getString(i))));
			Collections.sort(map);
			Collections.reverse(map);
			results = new JSONArray();
			for (Tuple<String,Double> sd : map)
				results.put(sd.getFirst());
		}
		else
		for (int i = 0;i < sortBy.length();i++)
			if (ja.contains(sortBy.getString(i)))
				results.put(sortBy.getString(i));
		return results;
	}

	@Override
	public String getDescription()
	{
		return "Sorts the incoming array by the array provided by 'by'. All elements must be in 'by'";
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
		return jo("obj","JSONArray","by","JSONArray");
	}

}
