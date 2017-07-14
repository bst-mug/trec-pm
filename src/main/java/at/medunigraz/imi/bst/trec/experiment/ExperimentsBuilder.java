package at.medunigraz.imi.bst.trec.experiment;

import java.util.HashSet;
import java.util.Set;

import at.medunigraz.imi.bst.trec.query.Query;

public class ExperimentsBuilder {

	private Set<Experiment> experiments = new HashSet<>();

	private Experiment buildingExp = null;

	public ExperimentsBuilder() {
	}

	public ExperimentsBuilder newExperiment() {
		addBuilding();
		buildingExp = new Experiment();
		return this;
	}

	public ExperimentsBuilder newExperiment(Experiment base) {
		buildingExp = new Experiment();
		buildingExp.setExperimentId(base.getExperimentId());
		buildingExp.setDecorator(base.getDecorator());
		return this;
	}

	public ExperimentsBuilder withId(String id) {
		buildingExp.setExperimentId(id);
		return this;
	}

	public ExperimentsBuilder withDecorator(Query decorator) {
		buildingExp.setDecorator(decorator);
		return this;
	}

	public Set<Experiment> build() {
		addBuilding();
		return experiments;
	}

	private void addBuilding() {
		if (buildingExp != null) {
			this.experiments.add(buildingExp);
		}
	}
}
