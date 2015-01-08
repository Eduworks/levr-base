package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.string.ResolverString;

public class ResolverError extends ResolverString
{

	@Override
	public Object resolve(Context c, Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final String msgKey = "msg";
		Object object = get(msgKey, parameters);
		String message = "";
		if (object != null)
			message = object.toString();

		throw new RuntimeException(format(message, parameters, msgKey));
	}

	@Override
	public String getDescription()
	{
		return "Throws an error";
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
		return jo("msg","String","?<any>","String|Number");
	}
}
