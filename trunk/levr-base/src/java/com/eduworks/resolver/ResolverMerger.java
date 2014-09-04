package com.eduworks.resolver;

import java.util.Map;

import org.json.JSONException;

/**
 * An api for Resolvers that process JSON merges with common options (key values).
 * @author dharvey
 * @since 11/2011
 */
@Deprecated
public interface ResolverMerger
{
	final static String DEST_KEY = "dest";
	final static String MERGE_KEY = "merge";
	final static String UNIQUE_KEY = "unique";

	public Object merge(Object mergeTo, String srcKey, String destKey, Map<String, String[]> parameters) throws JSONException;

}
