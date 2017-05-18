package at.medunigraz.imi.bst.trec.model;

public class Result {
	private int id;

	private float score;

	public Result(int id, float score) {
		this.id = id;
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public float getScore() {
		return score;
	}
}
