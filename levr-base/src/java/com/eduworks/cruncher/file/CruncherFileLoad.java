package com.eduworks.cruncher.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFileLoad extends Cruncher {
	public Object resolve(java.util.Map<String, String[]> parameters,
			java.util.Map<String, java.io.InputStream> dataStreams)
			throws org.json.JSONException {
		String path = getAsString("path", parameters, dataStreams);
		if (path.contains(".."))
			throw new RuntimeException("Cannot go up in filesystem.");
		File f = new File(path);
		try {
			InMemoryFile imf = new InMemoryFile(f);
			if (optAsBoolean("text", false, parameters, dataStreams))
				return IOUtils.toString(imf.getInputStream());
			return imf;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public String getDescription() {
		return "Loads a file in the etc filesystem.";
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
