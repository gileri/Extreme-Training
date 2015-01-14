package fr.cpe.gae;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

@SuppressWarnings("serial")
public class Domains extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService store = DatastoreServiceFactory.getDatastoreService();
		JsonArray array = new JsonArray();
		Query q = new Query("Domain");
		PreparedQuery pq = store.prepare(q);
		for (Entity e : pq.asIterable()) {
			array.add(new JsonPrimitive((String) e.getProperty("title")));
		}
		Gson gson = new Gson();
		resp.getWriter().write(gson.toJson(array));
	}
}
