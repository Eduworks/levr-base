package com.eduworks.cruncher.solr;

import java.io.InputStream;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherSolrSearch extends Cruncher
{
	@Override
	public Object resolve(Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String solrURL = Resolver.decodeValue(optAsString("solrURL", "http%3A%2F%2Flocalhost%3A8983%2Fsolr%2F", parameters, dataStreams));
		HttpSolrServer solrServer;
		if (!SolrServer.serverMap.containsKey(solrURL)) {
			solrServer = new HttpSolrServer(solrURL);
			SolrServer.serverMap.put(solrURL, solrServer);
		} else 
			solrServer = SolrServer.serverMap.get(solrURL);
		
		String query = optAsString("query", "*:*", parameters, dataStreams);
		if (query.trim().equals(""))
			query = "*:*";
		SolrQuery queryParameters = new SolrQuery();
		
		//Forces each term to be found for query to be satisfied. orange AND taco VS orange OR taco
		boolean useMm = optAsBoolean("useMustMatchAll", true, parameters, dataStreams);
		if (useMm) queryParameters.set("mm", "100%");
		
		queryParameters.set("q", query);
		
		Double start = getAsDouble("start", parameters, dataStreams);
		if (start != null) queryParameters.setStart(start.intValue());
		
		
		queryParameters.setRows(Integer.parseInt(optAsString("rows", "10", parameters, dataStreams)));
		JSONArray defaultFields = getAsJsonArray("fields", parameters, dataStreams);
		if (defaultFields!=null) {
			String fieldAccumulator = "";
			for (int fieldIndex=0; fieldIndex<defaultFields.length(); fieldIndex++) {
				fieldAccumulator += " " + defaultFields.getString(fieldIndex);
			}
			if (fieldAccumulator!="")
				fieldAccumulator = fieldAccumulator.substring(" ".length());
			queryParameters.add("qf", fieldAccumulator);
		}
		
		JSONArray returnFields = getAsJsonArray("returnFields", parameters, dataStreams);
      if (returnFields!=null) {
         String fieldAccumulator = "";
         for (int fieldIndex=0; fieldIndex<returnFields.length(); fieldIndex++) {
            fieldAccumulator += " " + returnFields.getString(fieldIndex);
         }
         if (fieldAccumulator!="")
            fieldAccumulator = fieldAccumulator.substring(" ".length());
         queryParameters.add("fl", fieldAccumulator);
      }
      
		JSONArray sortFields = getAsJsonArray("sort", parameters, dataStreams);
		if (sortFields!=null) {
			for (int fieldIndex=0; fieldIndex<sortFields.length(); fieldIndex++) {
				JSONObject sort = (JSONObject) EwJson.tryParseJson(sortFields.getString(fieldIndex), false);
				queryParameters.addSort(sort.getString("field"), sort.getString("order").equalsIgnoreCase("desc")?SolrQuery.ORDER.desc:SolrQuery.ORDER.asc);
			}
		}
		
		boolean sortById = optAsBoolean("idSort", true, parameters, dataStreams);				
		if (sortById) queryParameters.addSort("id", SolrQuery.ORDER.asc);
		
		queryParameters.set("defType", "edismax");
		
		boolean useCursor = optAsBoolean("useCursor", true, parameters, dataStreams); 
		if (useCursor) queryParameters.set("cursorMark", optAsString("cursor", "*", parameters, dataStreams));
		
		QueryResponse results;
		try {
			results = solrServer.query(queryParameters);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
		
		SolrDocumentList documentList = results.getResults();
		
		JSONArray documentSet = new JSONArray();
		JSONObject response = new JSONObject();
		for (ListIterator<SolrDocument> documentPointer = documentList.listIterator(); documentPointer.hasNext(); ){
			SolrDocument solrDocument = documentPointer.next();
			JSONObject document = new JSONObject();
			for (Iterator<Entry<String, Object>> solrDocumentKeys = solrDocument.iterator(); solrDocumentKeys.hasNext();) {
				Entry<String, Object> solrDocumentKey = solrDocumentKeys.next();
				document.put(solrDocumentKey.getKey(), solrDocumentKey.getValue());
			}
			documentSet.put(document);
		}
		
		response.put("items", documentSet);
		response.put("total", results.getResults().getNumFound());
		if (useCursor)
			response.put("cursor", results.getResponse().get("nextCursorMark"));
		return response;
	}

	@Override
	public String getDescription()
	{
		return "Accepts a solr query with rows and pages. Returns response in json format";
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
		return jo("solrURL", "String", "q", "String", "?pages", "Integer", "?rows", "Integer", "?fields", "JSONArray", 
		          "?returnFields", "JSONArray", "?idSort", "boolean", "?useCursor", "boolean", "?useMustMatchAll", "boolean",
		          "?start","Integer");
	}
}
