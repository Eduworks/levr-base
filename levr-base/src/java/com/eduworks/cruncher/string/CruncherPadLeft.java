package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherPadLeft extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String str = getAsString("obj",parameters, dataStreams);
		int pad = getAsDouble("pad", parameters, dataStreams).intValue();
		String with = getAsString("with",parameters,dataStreams);
		while (str.length() < pad)
			str = with + str;
		return str;
	}

	@Override
	public String getDescription() {
		return "Pads the left side of a string with";
	}

	@Override
	public String getReturn() {
		return "String";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("obj","String","pad","Number","with","String");
	}

}
