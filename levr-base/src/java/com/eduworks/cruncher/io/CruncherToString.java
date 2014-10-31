package com.eduworks.cruncher.io;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherToString extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		return getObj(parameters,dataStreams).toString();
	}

	@Override
	public String getDescription()
	{
		return "ToStrings the obj.";
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
		return jo("obj","Object");
	}

}
