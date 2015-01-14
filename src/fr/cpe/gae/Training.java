package fr.cpe.gae;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class Training extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		String keyString =(String)req.getParameter("key");
		
		//get training
		Key key = KeyFactory.stringToKey(keyString);
		Entity training = null;
		try {
			training = datastore.get(key);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		
		// get exercises list
		Filter fe = new FilterPredicate("training", Query.FilterOperator.EQUAL, KeyFactory.keyToString(key));
		Query qe = new Query("Exercise").setFilter(fe);
        PreparedQuery pqe = datastore.prepare(qe);
        FetchOptions foe = FetchOptions.Builder.withDefaults();
        List <Entity> trainingExercises = pqe.asList(foe);
        
        //get domain
        String domainKeyString = (String) training.getProperty("domain");
        Key keyDomain = KeyFactory.stringToKey(domainKeyString);
        Entity domain = null;
		try {
			domain = datastore.get(keyDomain);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
        
        
        JsonObject trainObj = new JsonObject();
        trainObj.addProperty("title", (String) training.getProperty("title"));
        trainObj.addProperty("description", (String) training.getProperty("description"));
        trainObj.addProperty("domain", (String) domain.getProperty("title"));
        
        JsonArray exerciseArray = new JsonArray();
        for (Entity entity : trainingExercises) {
        	JsonObject exerciseObject = new JsonObject();
			exerciseObject.addProperty("title", (String) entity.getProperty("title"));
			exerciseObject.addProperty("description", (String) entity.getProperty("description"));
			exerciseObject.addProperty("duration", (String) entity.getProperty("duration"));
			exerciseArray.add(exerciseObject);
		}
        trainObj.add("exercises", exerciseArray);
        
        Gson gson = new Gson();
        String jsonRes = gson.toJson(trainObj);
        resp.getWriter().write(jsonRes);
        
		resp.setStatus(200);
	}
}