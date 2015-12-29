package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import com.eduworks.lang.EwMap;
import com.eduworks.util.io.InMemoryFile;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToDatastream extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String paramName=getAsString("paramName",c,parameters, dataStreams);
		InMemoryFile o = (InMemoryFile) getObj(c,parameters, dataStreams);

		final EwMap<String, InputStream> newData = new EwMap<String, InputStream>(dataStreams);
        newData.put(paramName, o.getInputStream());
        Object result = resolveAChild("op",c, parameters, newData);
        return result;
	}
    
	@Override
	public String getDescription()
	{
		return "Converts obj into a named datastream and runs op.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Object","paramName","String","op","Resolvable");
	}
}
