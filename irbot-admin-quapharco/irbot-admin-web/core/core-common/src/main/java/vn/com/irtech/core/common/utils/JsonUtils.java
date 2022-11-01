package vn.com.irtech.core.common.utils;

import com.google.gson.Gson;

public class JsonUtils {
	
	public static String objToJson(Object obj) {
		if (obj == null) {
			return null;
		}
		
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
}
