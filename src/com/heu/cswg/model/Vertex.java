package com.heu.cswg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex {
	
	private int id;//�ڵ���
	
	private List<Vertex> neighbor = new ArrayList<Vertex> ();//�ھӽڵ㼯��
	
	private Map<Integer,Float> neighborWeight = new HashMap<> ();//Ӱ����ֵ����
	
	private int dist = 0;//���ѯ�ڵ���루������
	
	private boolean visit = false;//�Ƿ񱻷��ʵı��
	
	private List<String> attr = new ArrayList<String> ();//�ڵ������

	public Vertex(int Id) {
		setId(Id);
		// TODO Auto-generated constructor stub
	}

	public Vertex() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Vertex> getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(List<Vertex> neighbor) {
		this.neighbor = neighbor;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public boolean isVisit() {
		return visit;
	}

	public void setVisit(boolean visit) {
		this.visit = visit;
	}

	public Map<Integer,Float> getNeighborWeight() {
		return neighborWeight;
	}

	public void setNeighborWeight(Map<Integer,Float> neighborWeight) {
		this.neighborWeight = neighborWeight;
	}

	public List<String> getAttr() {
		return attr;
	}

	public void setAttr(List<String> attr) {
		this.attr = attr;
	}

	@Override
	public String toString() {
		return "Vertex{" +
				"id=" + id +
				'}';
	}
}
