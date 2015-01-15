package fr.cpe.gae;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public class AddTraining extends HttpServlet{
	public void doPost (HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		 StringBuffer jb = new StringBuffer();
		 String line = null;
		 try {
		   BufferedReader reader = req.getReader();
		   while ((line = reader.readLine()) != null)
		     jb.append(line);
		 } catch (Exception e) { /*report an error*/ }
		 Queue queue = QueueFactory.getDefaultQueue();
		 TaskOptions task = TaskOptions.Builder.withUrl("/savetraining").param("jsonTraining", jb.toString());
		 queue.add(task);
		 queue.purge();
	}
}
