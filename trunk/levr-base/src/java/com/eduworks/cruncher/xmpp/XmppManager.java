package com.eduworks.cruncher.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;

public class XmppManager
{
	static Map<String, XMPPConnection>		connections	= new HashMap<String, XMPPConnection>();
	static Map<String, FileTransferManager>	ftms		= new HashMap<String, FileTransferManager>();
	static Map<String, Chat>				chats		= new HashMap<String, Chat>();

	public static XMPPConnection get(String server, String port, String loginHostname, String username, String password)
	{
		String hash = server + port + username + password;
		XMPPConnection connection = null;
		if (connections.containsKey(hash))
			connection = connections.get(hash);
		else
		{
			ConnectionConfiguration config = new ConnectionConfiguration(server, Short.parseShort(port), loginHostname);
			config.setSelfSignedCertificateEnabled(true);

			connection = new XMPPConnection(config);
			connections.put(hash, connection);
		}
		try
		{
			if (!connection.isConnected())
				connection.connect();
			if (!connection.isAuthenticated())
				connection.login(username, password);
		}
		catch (XMPPException e)
		{
			connections.remove(hash);
			ftms.remove(hash);
			throw new RuntimeException(e);
		}
		return connection;
	}

	public static FileTransferManager getFtm(String server, String port, String loginHostname, String username,
			String password)
	{
		String hash = server + port + username + password;
		XMPPConnection connection = get(server, port, loginHostname, username, password);
		FileTransferManager ftm = null;
		if (ftms.containsKey(hash))
			ftm = ftms.get(hash);
		else
		{
			ftm = new FileTransferManager(connection);
			ftms.put(hash, ftm);
		}
		return ftm;
	}

	public static Chat getChat(XMPPConnection connection, String recipient)
	{
		String hash = recipient;
		Chat chat = null;
		if (chats.containsKey(hash))
			chat = chats.get(hash);
		else
		{
			chat = connection.getChatManager().createChat(recipient, null);
			chats.put(hash, chat);
		}
		return chat;
	}

}
