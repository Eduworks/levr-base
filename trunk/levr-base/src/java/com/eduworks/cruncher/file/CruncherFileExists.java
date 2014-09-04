package com.eduworks.cruncher.file;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherFileExists extends Cruncher {
	public Object resolve(java.util.Map<String, String[]> parameters,
			java.util.Map<String, java.io.InputStream> dataStreams)
			throws org.json.JSONException {
		
		String path = getAsString("path", parameters, dataStreams);
		if (path.contains(".."))
			throw new RuntimeException("Cannot go up in filesystem.");
		File f = new File(path);

		if(f.exists()){
			return true;
		}else{
			return false;
		}

	}

	@Override
	public String getDescription() {
		return "Checks whether file exists on the filesystem.";
	}

	@Override
	public String getReturn() {
		return "InMemoryFile";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("path","String");
	};
}
