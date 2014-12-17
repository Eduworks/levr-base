package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyCreateObjectProperty extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String propertyId = Resolver.decodeValue(optAsString("propertyId", "", c, parameters, dataStreams));

		JSONObject vals = new JSONObject(Resolver.decodeValue(optAsString("vals", "{}", c, parameters, dataStreams)));

		Ontology o = null;
		JSONObject jsonRepresentation = null;
		Dataset tdbDataset = getDataSet(directory,ReadWrite.WRITE,c);
		
		try
		{
			try
			{
				o = getOntology(ontologyId, tdbDataset, c);
			}
			catch (ClosedException e)
			{
				c.remove(TDB_ONTOLOGY);
				return resolve(c,parameters, dataStreams);
			}

			jsonRepresentation = o.createObjectProperty(propertyId, vals).getJSONRepresentation();

		}
		catch (RuntimeException e)
		{

			throw e;
		}
		finally
		{
			if (o != null)
				o.close(false);
		}

		return jsonRepresentation;
	}

	@Override
	public String getDescription()
	{
		return "Creates a new object property in the ontology specified, vals defines any superclasses, domain, and range the property could have, as well as other"
				+ " properties such as transitive, reflexive, etc. An object property is a property that relates two different instances";
	}

	@Override
	public String getReturn()
	{
		return "Object representing the new property";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("ontologyId", "string", "directory", "path string", "propertyId", "string", "vals", "Object");
	}

}