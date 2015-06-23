package com.eduworks.cruncher.db.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.ContextEvent;
import com.eduworks.resolver.Cruncher;

public class CruncherSql extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String DB_URL = getAsString("sqlConnectionString", c, parameters, dataStreams);
		String USER = getAsString("sqlUsername", c, parameters, dataStreams);
		String PASS = getAsString("sqlPassword", c, parameters, dataStreams);
		boolean mysql = optAsBoolean("sqlMysql", false, c, parameters, dataStreams);
		boolean sqlserver = optAsBoolean("sqlSqlServer", false, c, parameters, dataStreams);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		JSONArray ja = new JSONArray();
		try
		{
			// STEP 2: Register JDBC driver
			if (mysql)
				Class.forName("com.mysql.jdbc.Driver");
			if (sqlserver)
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			// STEP 3: Open a connection
			conn = (Connection) c.get(DB_URL);
			if (conn == null)
			{
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				final Connection conFinal = conn;
				c.onFinally(new ContextEvent()
				{
					@Override
					public void go()
					{
						if (conFinal != null)
							try
							{
								conFinal.close();
							}
							catch (SQLException e)
							{
								e.printStackTrace();
							}
					}
				});
			}

			// STEP 4: Execute a query
			stmt = conn.createStatement();
			String sql;
			sql = getObj(c, parameters, dataStreams).toString();
			rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next())
			{
				JSONObject jo = new JSONObject();
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++)
				{
					String colName = rs.getMetaData().getColumnName(i);
					int colType = rs.getMetaData().getColumnType(i);
					if (colType == java.sql.Types.VARCHAR)
						jo.put(colName, rs.getString(colName).trim());
					if (colType == java.sql.Types.INTEGER)
						jo.put(colName, rs.getInt(colName));
					if (colType == java.sql.Types.DECIMAL)
						jo.put(colName, rs.getBigDecimal(colName));
					if (colType == java.sql.Types.NUMERIC)
						jo.put(colName, rs.getDouble(colName));
				}
				ja.put(jo);
			}
			// STEP 6: Clean-up environment
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;

		}
		catch (SQLException se)
		{
			// Handle errors for JDBC
			se.printStackTrace();
		}
		catch (Exception e)
		{
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		finally
		{
			// finally block used to close resources
			try
			{
				if (rs != null)
					rs.close();
			}
			catch (SQLException se2)
			{
			}// nothing we can do
				// finally block used to close resources
			try
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se2)
			{
			}// nothing we can do
		}// end try
		return ja;
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribution()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
