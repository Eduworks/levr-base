package com.eduworks.resolver.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticdesktop.aperture.mime.identifier.MimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;

import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.InMemoryFile;

public class ResolverMimeType extends Resolver
{

	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(parameters, dataStreams);
		Object obj = get("file", parameters);
		try
		{
			if (obj instanceof File)
				return getMimeType((File) obj);
			else if (obj instanceof InMemoryFile)
				return getMimeType((InMemoryFile) obj);
			else
				return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Object getMimeType(InMemoryFile file) throws IOException
	{
		String mimeType = null;
		MimeTypeIdentifier identifier = new MagicMimeTypeIdentifier();
		mimeType = identifier.identify(file.data, file.name, null);
		mimeType = fixMimeType(mimeType, file.name, file.data);
		return mimeType;
	}

	public static Object getMimeType(File file) throws IOException
	{
		String mimeType = null;
		if (mimeType == null)
		{
			byte[] bytes = null;
			MimeTypeIdentifier identifier = new MagicMimeTypeIdentifier();
			int length = Math.max(1024, identifier.getMinArrayLength());
			bytes = FileUtils.readFileToByteArray(file);
			mimeType = identifier.identify(bytes, file.getName(), null);
			mimeType = fixMimeType(mimeType, file.getName(), bytes);
		}
		return mimeType;
	}

	private static String fixMimeType(String mime, String url, byte[] firstBytes)
	{
		if ((url == null || url.endsWith(".fla")) && firstBytes != null && firstBytes.length >= 4 && firstBytes[0] == -48 && firstBytes[1] == -49
				&& firstBytes[2] == 17 && firstBytes[3] == -32)
			return "application/octet-stream";
		// Shockwave audio files (.swa) are just specialized .mp3 files
		if (url != null && url.toLowerCase().endsWith(".swa") && firstBytes != null && firstBytes.length > 4 && firstBytes[0] == 0 && firstBytes[1] == 0
				&& firstBytes[3] == 32)
			return "audio/mpeg";
		// Javascript is sometimes misclassified as HTML
		if (mime != null && (mime.equalsIgnoreCase("text/html") || mime.equalsIgnoreCase("application/xhtml+xml")) && url.toLowerCase().endsWith(".js"))
			return "text/javascript";
		if (mime != null && (mime.equalsIgnoreCase("text/xml")) && url.toLowerCase().endsWith("html"))
			return "application/xhtml+xml";
		return mime == null ? "application/octet-stream" : mime;
	}

	@Override
	public String getDescription()
	{
		return "Detects the mime type of a file";
	}

	@Override
	public String[] getResolverNames()
	{
		return new String[] { getResolverName(), "mimetype" };
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_PROPRIETARY;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("file", "File|InMemoryFile");
	}

}
