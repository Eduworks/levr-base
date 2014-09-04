package com.eduworks.resolver.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFileToString extends Cruncher
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		try
		{
			InMemoryFile obj = (InMemoryFile) getObj(parameters, dataStreams);
			return IOUtils.toString(obj.data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Converts an in memory file to a string.";
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
		return jo("obj", "InMemoryFile");
	}

}
