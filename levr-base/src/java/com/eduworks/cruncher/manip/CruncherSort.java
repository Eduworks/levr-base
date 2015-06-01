package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolvable;

public class CruncherSort extends Cruncher
{

	@Override
	public Object resolve(final Context c, final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams)
			throws JSONException
	{
		JSONArray ja = getObjAsJsonArray(c, parameters, dataStreams);
		if (ja == null) return null;
		if (ja.length() < 2) return ja;

		final Boolean desc = optAsBoolean("desc", false, c, parameters, dataStreams);

		final String paramName = getAsString("paramName", c, parameters, dataStreams);

		final Resolvable op = (Resolvable) get("op");
		final boolean asString = optAsBoolean("asString",false,c,parameters,dataStreams);

		List<Object> list = new EwList<Object>(ja);
		final Map<Object, Object> cache = new HashMap<Object, Object>();
		Collections.sort(list, new Comparator<Object>()
		{

			@Override
			public int compare(Object o1, Object o2)
			{
				try
				{
					Object s1 = cache.get(o1);
					if (s1 == null)
					{
						Map<String, String[]> newParameters = new HashMap<String, String[]>(parameters);
						newParameters.put(paramName, new String[] { o1.toString() });
						Object resolve = ((Resolvable) op.clone()).resolve(c, newParameters, dataStreams);
						if (resolve != null)
							if (asString)
								s1 = resolve.toString();
							else
								s1 = (Number) Double.parseDouble(resolve.toString());
					}
					Object s2 = cache.get(o2);
					if (s2 == null)
					{
						Map<String, String[]> newParameters = new HashMap<String, String[]>(parameters);
						newParameters.put(paramName, new String[] { o2.toString() });
						Object resolve = ((Resolvable) op.clone()).resolve(c, newParameters, dataStreams);
						if (resolve != null)
							if (asString)
								s2 = resolve.toString();
							else
								s2 = (Number) Double.parseDouble(resolve.toString());
					}
					if (asString) return s1.toString().compareTo(s2);
					if (s1 == null && s2 == null) return 0;
					if (s1 == null)
						return -1;
					if (s2 == null)
						return 1;
					if (desc)
						if (((Number) s1).doubleValue() < ((Number) s2).doubleValue())
							return 1;
						else if (s1 == s2)
							return 0;
						else
							return -1;
					else if (((Number) s1).doubleValue() < ((Number) s2).doubleValue())
						return -1;
					else if (s1 == s2)
						return 0;
					else
						return 1;
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					return -1;
				}
				catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
					return -1;
				}
			}
		});

		return new JSONArray(list);
	}

	@Override
	public String getDescription()
	{
		return "Sorts a list based on an operation defined by 'op'";
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
		return jo("obj","JSONArray","?desc","Boolean","paramName","String","op","Resolvable");
	}

}
