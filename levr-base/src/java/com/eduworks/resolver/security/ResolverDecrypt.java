package com.eduworks.resolver.security;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ResolverDecrypt extends ResolverSecurity
{
	@Override
	public Object resolve(Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		if (has(KEY))
			resolveAChild(parameters, dataStreams, KEY);

		resolveAChild(parameters, dataStreams, VALUE);

	    try
		{
	    	return decrypt(getAsString("salt",parameters),parameters);
		}
		catch (GeneralSecurityException gse)
		{
			throw new RuntimeException(gse);
		}
	}

	@Override
	public String getDescription()
	{
		return "Decrypts 'value' using 'key'";
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
		return jo("key","String","value","String");
	}

}
