package com.eduworks.cruncher.parse;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherStripTags extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String text = (String)getObj(c, parameters, dataStreams);
		
		return Jsoup.clean(text, Whitelist.simpleText().addTags("sup", "sub"));
	}

	@Override
	public String getDescription()
	{
		return "Returns a list that represents the discrete intersection of all lists represented by the parameters of this Resolver.\n Has alternate behavior, if 'contains' is used, will return any items that have a substring of the 'contains' parameter.";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String");
	}

}
