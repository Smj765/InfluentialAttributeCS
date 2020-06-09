package com.heu.cswg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.StringFactory;

public class Graph {
	
	private int vertexSize;
	
	private int edgeSize;
	
	private float maxIAB;
	
	private List<Vertex> vertexIdList = new ArrayList<Vertex> ();
	
	private Map<Integer,Vertex> vertexMap = new HashMap<Integer,Vertex> ();

	public int getVertexSize() {
		return vertexSize;
	}

	public void setVertexSize(int vertexSize) {
		this.vertexSize = vertexSize;
	}

	public List<Vertex> getVertexIdList() {
		return vertexIdList;
	}

	public void setVertexIdList(List<Vertex> vertexIdList) {
		this.vertexIdList = vertexIdList;
	}

	public Map<Integer,Vertex> getVertexMap() {
		return vertexMap;
	}

	public void setVertexMap(Map<Integer,Vertex> vertexMap) {
		this.vertexMap = vertexMap;
	}

	public int getEdgeSize() {
		return edgeSize;
	}

	public void setEdgeSize(int edgeSize) {
		this.edgeSize = edgeSize;
	}

	public float getMaxIAB() {
		return maxIAB;
	}

	public void setMaxIAB(float maxIAB) {
		this.maxIAB = maxIAB;
	}

	public void initIAB() {
		float maxIAB = Integer.MIN_VALUE;
		for (int vid : this.getVertexMap().keySet()) {
			Vertex from = this.getVertexMap().get(vid);
			for (Vertex to : from.getNeighbor()) {
				float temp = this.getIAB(from.getId(), to.getId());
				if (maxIAB < temp) {
					maxIAB = temp;
				}
			}
		}
		this.maxIAB = maxIAB;
	}

	private float getIAB(int fromId, int toId) {
		int kA = this.getVertexMap().get(fromId).getNeighbor().size();
		int kB = this.getVertexMap().get(toId).getNeighbor().size();
		if (kA * kB == 0) {
			return 0;
		}
		int M = this.getEdgeSize();
		float t = 0;
		try {
		    t = (float) -Math.log(1-(float)(StringFactory.C(M, kA, kB)));
		} catch (Exception e) {
			System.out.println("C(M-kA, kB)" + StringFactory.C(M, kA, kB));
			System.out.println("C(M, kB)" + StringFactory.C(M, kA, kB));
			System.out.println(M);
			System.out.println(kA);
			System.out.println(kB);
		}
		return t;
	}
	
}
