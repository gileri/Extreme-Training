package fr.cpe.gae;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	private static final Map<String, String> openIdProviders;
	static {
		openIdProviders = new HashMap<String, String>();
		openIdProviders.put("Google", "https://www.google.com/accounts/o8/id");
		openIdProviders.put("Yahoo", "yahoo.com");
		openIdProviders.put("MyOpenId.com", "myopenid.com");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser(); // or req.getUserPrincipal()
		Set<String> attributes = new HashSet<String>();
		Gson gson = GSonFactory.getGsonInstance();

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		if (user != null) {
			JsonObject j = new JsonObject();
			j.addProperty("name", user.getNickname());
			j.addProperty("logout", userService.createLogoutURL(req.getRequestURI()));
			
			out.write(gson.toJson(j));
		} else {
			JsonArray user_auth = new JsonArray();
			
			out.println("Hello world! Sign in at: ");
			for (String providerName : openIdProviders.keySet()) {
				String providerUrl = openIdProviders.get(providerName);
				String loginURL = userService.createLoginURL(
						req.getRequestURI(), null, providerUrl, attributes);
				JsonObject jo = new JsonObject();
				jo.addProperty("name", providerName);
				jo.addProperty("providerURL", providerUrl);
				jo.addProperty("loginURL", loginURL);
				user_auth.add(jo);
			}
			out.write(gson.toJson(user_auth));
			
		}
	}
}
