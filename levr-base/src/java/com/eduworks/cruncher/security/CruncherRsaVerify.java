package com.eduworks.cruncher.security;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

import org.apache.xerces.impl.dv.util.Base64;

public class CruncherRsaVerify extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String signature = getObj(c, parameters, dataStreams).toString();
		String key = getAsString("pk", c, parameters, dataStreams);
		Object against = get("against", c, parameters, dataStreams);
		if (against instanceof JSONObject)
			against = new EwJsonObject(against);
		try
		{
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.decode(key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\r?\n","")));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey bobPubKey = keyFactory.generatePublic(bobPubKeySpec);
			Signature sig = Signature.getInstance("sha1withrsa");
			sig.initVerify(bobPubKey);
			sig.update(against.toString().getBytes());
			boolean verify = sig.verify(Base64.decode(signature));
			if (verify)
				return true;
			return false;
		}
		catch (SignatureException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvalidKeyException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvalidKeySpecException e)
		{
			throw new RuntimeException(e);
		}
	}
		
	@Override
	public String getDescription()
	{
		return "Verifies an RSA signature using a public key and a piece of data.";
	}

	@Override
	public String getReturn()
	{
		return "Boolean";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "String", "pk", "String", "against", "String");
	}

}
