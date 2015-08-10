package com.eduworks.cruncher.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
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
		Whitelist wl = Whitelist.simpleText();
		
		JSONArray allowTags = getAsJsonArray("allowTags", c, parameters, dataStreams);
		String[] tagList = new String[allowTags.length()+2];
		tagList[0] = "sub";
		tagList[1] = "sup";
		
		if (allowTags != null) {
			for (int i=0; i<allowTags.length(); i++) {
				tagList[i+2] = allowTags.getString(i);
			}
			wl = wl.addTags(tagList);
		}
		
		return Jsoup.clean(text, wl);
	}

	@Override
	public String getDescription()
	{
		return "Strips any non-simple-text-manipulating HTML Tags from the input string.";
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
		return jo("obj","String", "allowTags", "JSONArray");
	}

}
