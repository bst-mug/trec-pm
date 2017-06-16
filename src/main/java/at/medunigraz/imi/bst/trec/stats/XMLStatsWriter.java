package at.medunigraz.imi.bst.trec.stats;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import at.medunigraz.imi.bst.trec.model.Metrics;

public class XMLStatsWriter implements StatsWriter {

	private File output;

	public XMLStatsWriter(File output) {
		this.output = output;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(String topic, Metrics metrics) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Document doc = documentBuilder.newDocument();

		
		
		Element rootElement = doc.createElement("report");
		doc.appendChild(rootElement);

		Element topicElement = doc.createElement("all");
		rootElement.appendChild(topicElement);
		
		Element metricElement = doc.createElement("ndcg");
		metricElement.appendChild(doc.createTextNode("0.5"));
		topicElement.appendChild(metricElement);
		
		
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return;
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(output);

		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
			return;
		}

	}

}
