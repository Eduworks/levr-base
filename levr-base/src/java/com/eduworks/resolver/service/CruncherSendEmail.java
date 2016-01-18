package com.eduworks.resolver.service;

import java.io.InputStream;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.net.mail.EwMail;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.string.CruncherString;

public class CruncherSendEmail extends Cruncher
{

	/**
	 * Sends an email using the parameters passed in. Expected parameters:
	 * <list> <li><b>_to</b>: destination email</li> <li><b>_from</b>: source
	 * email</li> <li><b>_subject</b>: email subject</li> <li><b>_template</b>:
	 * email body</li> </list> <br/>
	 * <br/>
	 * The template value searches for "${from}" Email sending has no return
	 * value, so this method just returns the body of the email sent.
	 *
	 * @return the body of the email sent
	 */
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final String fromEmail = getAsString("_from", c, parameters, dataStreams);
		final String toEmail = getAsString("_to", c, parameters, dataStreams);
		final String subject = CruncherString.format(this,getAsString("_subject", c, parameters, dataStreams), c, parameters, dataStreams);
		final String template = CruncherString.format(this,getAsString("_template", c, parameters, dataStreams), c, parameters, dataStreams);
		final String smtpHost = getAsString("_smtpHost",c, parameters, dataStreams);
		final String smtpPort = getAsString("_smtpPort",c, parameters, dataStreams);
		final String smtpUser = getAsString("_smtpUser",c, parameters, dataStreams);
		final String smtpPass = getAsString("_smtpPass",c, parameters, dataStreams);

		if (optAsBoolean("html", false, c, parameters, dataStreams))
			try
			{
				EwMail.sendHtmlEmail(smtpHost,smtpPort,smtpUser,smtpPass,fromEmail, toEmail, subject, template);
			}
			catch (AddressException e)
			{
				throw new RuntimeException(e);
			}
			catch (MessagingException e)
			{
				throw new RuntimeException(e);
			}
		else
			try
			{
				EwMail.sendEmail(smtpHost,smtpPort,smtpUser,smtpPass,fromEmail, toEmail, subject, template);
			}
			catch (AddressException e)
			{
				throw new RuntimeException(e);
			}
			catch (MessagingException e)
			{
				throw new RuntimeException(e);
			}

		final EwJsonObject obj = new EwJsonObject();

		obj.put("from", fromEmail);
		obj.put("to", toEmail);
		obj.put("subject", subject);
		obj.put("body", template);

		return obj;
	}

	@Override
	public String getDescription()
	{
		return "Sends an email (TODO: This)";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo();
	}
}
