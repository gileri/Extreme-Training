package fr.cpe.gae;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Search extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException {
		String searchBar =(String)req.getParameter("searchBar");
		DatastoreService store = DatastoreServiceFactory.getDatastoreService();
		
		Filter f1 = new FilterPredicate("name", Query.FilterOperator.EQUAL,searchBar);
		Query q1 = new Query("Training").setFilter(f1);
        PreparedQuery pq1 = store.prepare(q1);
        FetchOptions fo1 = FetchOptions.Builder.withDefaults();
        List <Entity> trainings = pq1.asList(fo1);
       
        //String message = (String) pq1.asSingleEntity().getProperty("name");
        //resp.getWriter().println("Hello pouet, "+message);
        
        
        Filter f2 = new FilterPredicate("name", Query.FilterOperator.EQUAL,searchBar);
		Query q2 = new Query("Exercise").setFilter(f2);
        PreparedQuery pq2 = store.prepare(q2);
        FetchOptions fo2 = FetchOptions.Builder.withDefaults();
        List <Entity> exercises = pq2.asList(fo2);
        //message = (String) pq2.asSingleEntity().getProperty("name");
        
        HashMap <String, List> resultat= new HashMap();
        resultat.put("trainings", trainings);
        resultat.put("exercices", exercises);
        
        Gson gson = new Gson();
        gson.toJson(resultat);
        String jsonRes = gson.toJson(resultat);
        resp.getWriter().write(jsonRes);
        
	}
}
