package com.eduworks.cruncher.file;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherDeserialize extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		InMemoryFile imf = (InMemoryFile) getObj(c,parameters,dataStreams);
		return SerializationUtils.deserialize(imf.data);
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribution()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
