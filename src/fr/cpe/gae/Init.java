package fr.cpe.gae;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class Init extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
        Entity msg = new Entity("Messages");
        msg.setProperty("type", "motd");
        msg.setProperty("content", "Welcome to our website. This message is so long that it must be cached!");
        datastore.put(msg);
        
        Entity train = new Entity("Training");
        train.setProperty("name", "running");
        datastore.put(train);
        
        Entity exercice = new Entity("Exercise");
        exercice.setProperty("name", "running");
        datastore.put(exercice);
        
		resp.setStatus(200);
	}
}
