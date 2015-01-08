package fr.cpe.gae;

import com.google.gson.Gson;

public class GSonFactory {
	
	private static Gson gson;
	
	public static Gson getGsonInstance() {
		if(GSonFactory.gson == null)
			GSonFactory.gson = new Gson();
		return GSonFactory.gson;
	}

}
