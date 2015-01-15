package fr.cpe.gae;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SaveTraining extends HttpServlet{
	public void doPost (HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		 
		String jb = req.getParameter("jsonTraining");
		JsonParser parser = new JsonParser();
		JsonObject json = (JsonObject)parser.parse(jb);
		
		 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		 
		Entity training = new Entity("Training");
		String title = json.get("title").getAsString();
		training.setProperty("title", title);
		String description = json.get("description").getAsString();
		training.setProperty("description", description);
		 
		 
		String domain = json.get("domain").getAsString();
		
		 
		Filter fe = new FilterPredicate("title", Query.FilterOperator.EQUAL, domain);
		Query qe = new Query("Domain").setFilter(fe);
		PreparedQuery pqe = datastore.prepare(qe);
		Entity domainEntity = pqe.asSingleEntity(); 
		
		// TODO Handle error when non-existant domain
		training.setProperty("domain", KeyFactory.keyToString(domainEntity.getKey()));
		datastore.put(training);
		
		JsonArray arrayExercice = json.getAsJsonArray("exercises");
		for (JsonElement jsonElement : arrayExercice) {
			Entity exercice = new Entity("Exercise");
			String titleEx = jsonElement.getAsJsonObject().get("title").getAsString();
			exercice.setProperty("title", titleEx);
			String descriptionEx = jsonElement.getAsJsonObject().get("description").getAsString();
			exercice.setProperty("description", descriptionEx);
			String duration = jsonElement.getAsJsonObject().get("duration").getAsString();
			exercice.setProperty("duration", duration);
			exercice.setProperty("training", KeyFactory.keyToString(training.getKey()));
			datastore.put(exercice);
		}
	}
}
