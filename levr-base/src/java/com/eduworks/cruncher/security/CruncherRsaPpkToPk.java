package com.eduworks.cruncher.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import org.apache.xerces.impl.dv.util.Base64;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRsaPpkToPk extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		StringWriter sw = new StringWriter();
		String key = getObjAsString(c, parameters, dataStreams);
		try
		{
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.decode(key.replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "").replaceAll("\r?\n", "")));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateCrtKey ppk = (RSAPrivateCrtKey) keyFactory.generatePrivate(bobPubKeySpec);
			RSAPublicKey pk = new RSAPublicKey(ppk.getModulus(), ppk.getPublicExponent());
			PemWriter pw = new PemWriter(sw);
			pw.writeObject(new PemObject("RSA PRIVATE KEY", pk.getEncoded()));
			pw.flush();
			pw.close();
		}
		catch (IOException e)
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
		return sw.toString();
	}

	@Override
	public String getDescription()
	{
		return "Converts a RSA Private Key in PEM form to a RSA Public Key in PEM form.";
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
		return jo("obj", "String");
	}

}
