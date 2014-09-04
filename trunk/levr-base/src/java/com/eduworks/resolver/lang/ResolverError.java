package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;

import com.eduworks.resolver.string.ResolverString;

public class ResolverError extends ResolverString
{

	@Override
	public Object resolve(Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(parameters, dataStreams);

		final String msgKey = "msg";
		Object object = get(msgKey, parameters);
		String message = "";
		if (object != null)
			message = object.toString();

		throw new RuntimeException(format(message, parameters, msgKey));
	}

}
