package at.medunigraz.imi.bst.trec.model;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Topic {

	enum Type {
		TEST
	}

	private int number;
	private Type type;
	private String disease;
	private String variant;
	private String demographic;
	private String other;

	public Topic() {

	}

	/**
	 * Builds a Topic out of a XML file in the format:
	 * 
	 * <pre>
	 * {@code
	 * <topic number="1" type="test">
	 *     <disease>Acute lymphoblastic leukemia</disease>
	 *     <variant>ABL1, PTPN11</variant>
	 *     <demographic>12-year-old male</demographic>
	 *     <other>No relevant factors</other>
	 * </topic>
	 * }
	 * </pre>
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static Topic fromXML(File xmlFile) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		Document doc = null;
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Element element = (Element) doc.getElementsByTagName("topic").item(0);

		int number = Integer.parseInt(getAttribute(element, "number"));
		Type type = Type.valueOf(getAttribute(element, "type").toUpperCase());
		String disease = getElement(element, "disease");
		String variant = getElement(element, "variant");
		String demographic = getElement(element, "demographic");
		String other = getElement(element, "other");

		Topic topic = new Topic().withNumber(number).withType(type).withDisease(disease).withVariant(variant)
				.withDemographic(demographic).withOther(other);

		return topic;
	}

	public Topic withNumber(int number) {
		this.number = number;
		return this;
	}

	public Topic withType(Type type) {
		this.type = type;
		return this;
	}

	public Topic withDisease(String disease) {
		this.disease = disease;
		return this;
	}

	public Topic withVariant(String variant) {
		this.variant = variant;
		return this;
	}

	public Topic withDemographic(String demographic) {
		this.demographic = demographic;
		return this;
	}

	public Topic withOther(String other) {
		this.other = other;
		return this;
	}

	private static String getElement(Element element, String name) {
		return element.getElementsByTagName(name).item(0).getTextContent();
	}

	private static String getAttribute(Element element, String name) {
		return element.getAttribute(name);
	}

	public int getNumber() {
		return number;
	}

	public Type getType() {
		return type;
	}

	public String getDisease() {
		return disease;
	}

	public String getVariant() {
		return variant;
	}

	public String getDemographic() {
		return demographic;
	}

	public String getOther() {
		return other;
	}

	@Override
	public String toString() {
		return "Topic [number=" + number + ", type=" + type + ", disease=" + disease + ", variant=" + variant
				+ ", demographic=" + demographic + ", other=" + other + "]";
	}

}
