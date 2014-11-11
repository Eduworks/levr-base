package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherStartsWith extends Cruncher {
   
   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
   {
      String s1 = getAsString("string1",c,parameters, dataStreams).toLowerCase();
      String s2 = getAsString("string2",c,parameters, dataStreams).toLowerCase();
      if (s1 == null || s2 == null) return false;
      return s1.startsWith(s2);
   }

   @Override
   public String getDescription()
   {
      return "Returns true if string1 starts with string2.  Returns false otherwise.";
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
