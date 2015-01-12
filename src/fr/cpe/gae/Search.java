package fr.cpe.gae;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class Search extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException {
		String searchBar =(String)req.getParameter("searchBar");
		DatastoreService store = DatastoreServiceFactory.getDatastoreService();
		
		Filter f1 = new FilterPredicate("title", Query.FilterOperator.EQUAL,searchBar);
		Query q1 = new Query("Training").setFilter(f1);
        PreparedQuery pq1 = store.prepare(q1);
        FetchOptions fo1 = FetchOptions.Builder.withDefaults();
        List <Entity> trainings = pq1.asList(fo1);
        JsonArray trainingArray = new JsonArray();
        
        
        for (Entity entity : trainings) {
        	Filter fe = new FilterPredicate("training", Query.FilterOperator.EQUAL, KeyFactory.keyToString(entity.getKey()));
    		Query qe = new Query("Exercise").setFilter(fe);
            PreparedQuery pqe = store.prepare(qe);
            FetchOptions foe = FetchOptions.Builder.withDefaults();
            List <Entity> trainingExercises = pqe.asList(foe);
            
            JsonObject trainObj = new JsonObject();
            trainObj.addProperty("name", (String) entity.getProperty("title"));
            
            JsonArray trainingExercisesJSON = new JsonArray();
            for (Entity entity2 : trainingExercises) {
            	JsonObject trainingExercise = new JsonObject();
            	trainingExercise.addProperty("name", (String) entity2.getProperty("title"));
            	trainingExercise.addProperty("duration", (String) entity2.getProperty("duration"));
            	trainingExercisesJSON.add(trainingExercise);
			}
            trainObj.add("exercises", trainingExercisesJSON);
            trainingArray.add(trainObj);
		}
        
        
        Filter f2 = new FilterPredicate("title", Query.FilterOperator.EQUAL,searchBar);
		Query q2 = new Query("Exercise").setFilter(f2);
        PreparedQuery pq2 = store.prepare(q2);
        FetchOptions fo2 = FetchOptions.Builder.withDefaults();
        List <Entity> exercises = pq2.asList(fo2);
        
        JsonArray exerciseArray = new JsonArray();
        for (Entity entity : exercises) {
			JsonObject exerciseObject = new JsonObject();
			exerciseObject.addProperty("name", (String) entity.getProperty("title"));
			exerciseObject.addProperty("duration", (String) entity.getProperty("duration"));
			exerciseArray.add(exerciseObject);
		}
        
        
        JsonObject resultat= new JsonObject();
        resultat.add("trainings", trainingArray);
        resultat.add("exercises", exerciseArray);
        
        
        Gson gson = new Gson();
        gson.toJson(resultat);
        String jsonRes = gson.toJson(resultat);
        resp.getWriter().write(jsonRes);
        
	}
}
