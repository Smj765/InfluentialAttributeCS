package com.heu.cswg.model;

public class BFSNode {
	private int id;
	
	private int fId;
	
	private float nodeScore;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getfId() {
		return fId;
	}

	public void setfId(int fId) {
		this.fId = fId;
	}

	public float getNodeScore() {
		return nodeScore;
	}

	public void setNodeScore(float nodeScore) {
		this.nodeScore = nodeScore;
	}

	@Override
	public String toString() {
		return "BFSNode [id=" + id + ", fId=" + fId + ", nodeScore="
				+ nodeScore + "]";
	}
	
	
	
}
