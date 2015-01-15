package fr.cpe.gae;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@SuppressWarnings("serial")
public class Search extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		DatastoreService store = DatastoreServiceFactory.getDatastoreService();

		String searchBar = (String) req.getParameter("searchBar");
		String domainSelection = (String) req.getParameter("domain");
		Query q1 = new Query("Training");

		List<Filter> filters = new ArrayList<Filter>();

		if (searchBar != null) {
			filters.add(new FilterPredicate("title",
					Query.FilterOperator.EQUAL, searchBar));
		}

		if (domainSelection != null) {
			Filter fk = new FilterPredicate("title",
					Query.FilterOperator.EQUAL, domainSelection);
			Query qk = new Query("Domain").setFilter(fk);
			PreparedQuery pqk = store.prepare(qk);
			Entity domainEntity = pqk.asSingleEntity();

			String keyDomain = "xx";
			if (domainEntity != null) {
				keyDomain = KeyFactory.keyToString(domainEntity.getKey());
			}
			filters.add(new FilterPredicate("domain",
					Query.FilterOperator.EQUAL, keyDomain));
		}

		if (filters.size() == 1) {
			q1.setFilter(filters.get(0));
		} else if (filters.size() > 1) {
			q1.setFilter(CompositeFilterOperator.and(filters));
		}

		PreparedQuery pq1 = store.prepare(q1);

		JsonArray trainingArray = new JsonArray();
		JsonArray exerciseArray = new JsonArray();

		for (Entity entity : pq1.asIterable()) {
			Filter fe = new FilterPredicate("training",
					Query.FilterOperator.EQUAL, KeyFactory.keyToString(entity
							.getKey()));
			Query qe = new Query("Exercise").setFilter(fe);
			PreparedQuery pqe = store.prepare(qe);

			JsonObject trainObj = new JsonObject();
			trainObj.addProperty("name", (String) entity.getProperty("title"));
			trainObj.addProperty("key", KeyFactory.keyToString(entity.getKey()));

			JsonArray trainingExercisesJSON = new JsonArray();
			for (Entity entity2 : pqe.asIterable()) {
				JsonObject trainingExercise = new JsonObject();
				trainingExercise.addProperty("name",
						(String) entity2.getProperty("title"));
				trainingExercise.addProperty("duration",
						(String) entity2.getProperty("duration"));
				trainingExercise.addProperty("key",
						KeyFactory.keyToString(entity2.getKey()));
				trainingExercise.addProperty("keytraining",
						(String) entity2.getProperty("training"));
				trainingExercisesJSON.add(trainingExercise);
			}
			trainObj.add("exercises", trainingExercisesJSON);
			trainingArray.add(trainObj);
		}

		Query q2 = new Query("Exercise");
		if (searchBar != null) {
			Filter f2 = new FilterPredicate("title",
					Query.FilterOperator.EQUAL, searchBar);
			q2.setFilter(f2);
		}
		PreparedQuery pq2 = store.prepare(q2);

		for (Entity entity : pq2.asIterable()) {
			JsonObject exerciseObject = new JsonObject();
			exerciseObject.addProperty("name",
					(String) entity.getProperty("title"));
			exerciseObject.addProperty("duration",
					(String) entity.getProperty("duration"));
			exerciseObject.addProperty("key",
					KeyFactory.keyToString(entity.getKey()));
			exerciseObject.addProperty("keytraining",
					(String) entity.getProperty("training"));
			exerciseArray.add(exerciseObject);
		}

		JsonArray feedItems = new JsonArray();

		for (String s : RSSParser.parseRSS()) {
			feedItems.add(new JsonPrimitive(s));
		}

		JsonObject resultat = new JsonObject();
		resultat.add("trainings", trainingArray);
		resultat.add("exercises", exerciseArray);
		resultat.add("news", feedItems);

		Gson gson = new Gson();
		gson.toJson(resultat);
		String jsonRes = gson.toJson(resultat);
		resp.getWriter().write(jsonRes);

	}
}
