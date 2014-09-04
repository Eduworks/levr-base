package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherParamExists extends Cruncher
{
	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String paramName = getAsString("paramName", parameters, dataStreams);
		
		if(parameters.containsKey(paramName)){
			return get("true", parameters, dataStreams);
		}else{
			return get("false", parameters, dataStreams);
		}
	}

	@Override
	public String getDescription()
	{
		return "Provides branching functionality based on the existence of a parameter, specified by paramName.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("paramName","String","true","Resolvable","false","Resolvable");
	}
}
