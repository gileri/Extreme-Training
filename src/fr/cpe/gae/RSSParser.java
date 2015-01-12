package fr.cpe.gae;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RSSParser {

	public static List<String> parseRSS() {
		try {
			URL url = new URL("http://me.linuxw.info/feed.xml");
			InputStream is = url.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();
			javax.xml.xpath.XPathExpression expression = xPath
					.compile("//item");
			NodeList nl = (NodeList) expression.evaluate(
					doc.getDocumentElement(), XPathConstants.NODESET);
			
			List<String> res = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); ++i) {
				Node n = nl.item(i);
				expression = xPath.compile("title");
	            Node child = (Node) expression.evaluate(n, XPathConstants.NODE);
	            res.add(child.getTextContent());
			}
			return res;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
