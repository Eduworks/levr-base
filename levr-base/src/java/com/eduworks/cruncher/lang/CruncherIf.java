package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Cruncher;

public class CruncherIf extends Cruncher
{
	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object operator = getOpValue("operator", parameters,dataStreams);
		Object operand = getOpValue("operand", parameters,dataStreams);
		if (operator instanceof Double && operand instanceof Integer)
			operand = new Double((Integer)operand);
		if (operand instanceof Double && operator instanceof Integer)
			operator = new Double((Integer)operator);
		if (operator instanceof Boolean && operand instanceof String)
			operand = new Boolean((String)operand);
		if (operand instanceof Boolean && operator instanceof String)
			operator = new Boolean((String)operator);
		
		JSONObject resultO = new JSONObject();

		if (evaluatesAsString(operator) && evaluatesAsString(operand) && !areComparable(operator, operand))
		{
			operator = operator.toString();
			operand = operand.toString();
		}

		if (has("eq"))
			evaluate("eq",resultO, operator.equals(operand), parameters, dataStreams);

		if (has("ne"))
			evaluate("ne",resultO, !operator.equals(operand), parameters, dataStreams);

		if (has("eqi") && operator instanceof String && operand instanceof String)
			evaluate("eqi",resultO, ((String)operator).equalsIgnoreCase((String)operand), parameters, dataStreams);

		if (has("nei") && operator instanceof String && operand instanceof String)
			evaluate("nei",resultO, !((String)operator).equalsIgnoreCase((String)operand), parameters, dataStreams);

		if (operator instanceof Comparable<?> && operand instanceof Comparable<?>)
		{
			@SuppressWarnings("unchecked")
			final int result = ((Comparable<Object>) operator).compareTo(operand);

			if (has("lt"))
				evaluate("lt",resultO,(result < 0), parameters, dataStreams);

			if (has("gt"))
				evaluate("gt",resultO,(result > 0), parameters, dataStreams);

			if (has("le"))
				evaluate("le",resultO,(result <= 0), parameters, dataStreams);

			if (has("ge"))
				evaluate("ge",resultO,(result >= 0), parameters, dataStreams);
		}

		if (has("else"))
			resultO.put("else", get("else",parameters,dataStreams));

		switch (resultO.length())
		{
			case 0:
				return null;
			case 1:
				return resultO.get((String) resultO.keys().next());
			default:
				return resultO;
		}
	}

	protected void evaluate(String key, JSONObject result,boolean evaluates, Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
			throws JSONException
	{
		if (!evaluates)
			return;
		else
		{
			result.put(key,get(key,parameters,dataStreams));
		}
	}

	private Object getOpValue(String key, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object opValue = get(key, parameters,dataStreams);

		if (opValue == null) opValue = "";

		if (!(opValue instanceof String) || ((String)opValue).isEmpty() || !Character.isDigit(((String) opValue).charAt(0)))
			return opValue;
		try
		{
			opValue = Integer.parseInt(opValue.toString());
			return opValue;
		}
		catch (NumberFormatException ex)
		{
			
		}

		try
		{
			opValue = Double.parseDouble(opValue.toString());
			return opValue;
		}
		catch (NumberFormatException ex)
		{
			
		}
		
		return opValue;
	}

	private static boolean evaluatesAsString(Object opValue)
	{
		return (opValue instanceof Boolean || opValue instanceof Number || opValue instanceof String);
	}

	private static boolean areComparable(Object operator, Object operand)
	{
		if (operator == null || operand == null) return false;

		return operator.getClass().isInstance(operand) && operand.getClass().isInstance(operator);
	}

	@Override
	public String getDescription()
	{
		return "Provides branching functionality. Will return one or more of possible branches of the following types if the condition is true, comparing operator and operand\n" +
				"eq - Equals, ne - Not Equal, eqi - Equals (ignore Case), nei - Not Equal (ignore Case), lt - Less Than,\n" +
				"gt - Greater Than, le - Less Than or Equal, gt - Greater Than or Equal, else - None executed.";
	}

	@Override
	public String getReturn()
	{
		return "Boolean|JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("operator","String|Number","operand","String|Number","?eq","Resolvable","?ne","Resolvable","?eqi","Resolvable","?nei","Resolvable","?lt","Resolvable","?gt","Resolvable","?le","Resolvable","?gt","Resolvable","?else","Resolvable");
	}
}
