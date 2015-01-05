package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherIndexOf extends Cruncher
{
	  @Override
	   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	   {
	      String str = getAsString("str",c,parameters, dataStreams);
	      String substr = getAsString("substr",c,parameters, dataStreams);
	      
	      return str.indexOf(substr);
	   }

	   @Override
	   public String getDescription()
	   {
	      return "Returns true if string1 ends with string2.  Returns false otherwise.";
	   }

	   @Override
	   public String getReturn()
	   {
	      return "boolean";
	   }

	   @Override
	   public String getAttribution()
	   {
	      return ATTRIB_NONE;
	   }

	   @Override
	   public JSONObject getParameters() throws JSONException
	   {
	      return jo("string1","String","string2","String");
	   }

}
