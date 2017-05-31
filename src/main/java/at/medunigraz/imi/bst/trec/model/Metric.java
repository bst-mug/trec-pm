package at.medunigraz.imi.bst.trec.model;

public class Metric {
	private String name;
	private double value;

	public Metric(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}
}
