package com.heu.cswg.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.StringFactory;

import com.heu.cswg.algorithm.Preproccess;
import com.heu.cswg.model.BFSNode;
import com.heu.cswg.model.Graph;
import com.heu.cswg.model.Vertex;

/*
 * 2018-05-21 LiuChiming
 */

public class FileConverter {
	
    private static BufferedReader readFile(String fileName) throws IOException{
        File file = new File(fileName);
        System.out.println("¶ÁÈ¡ÎÄ¼þ...");
        return new BufferedReader(new FileReader(file));
    }
	
	public static Graph FileToGraph(String fileName)
	{

		BufferedReader reader = null;
		Graph G = new Graph();
		Map<Integer, Vertex> vertexMap = new HashMap<Integer, Vertex>();
		List<Vertex> vertexIdList = new ArrayList<Vertex>();
		int edgeNum = 0;
		try {
			reader = readFile(fileName);
			String temp = null;
			String[] arr = null;
			int fromId,toId = 0;
			//List<Vertex> neighborVertex = new ArrayList<Vertex>();
			Vertex fromVertex, toVertex;
            while ((temp = reader.readLine()) != null) 
            {
                arr = temp.split("	");
                try {
                fromId = Integer.parseInt(arr[0]);
                toId = Integer.parseInt(arr[1]);
                } catch (NumberFormatException e) {
                	continue;
                }
//                if(  70000 <fromId && fromId < 350000 ){
//                if(fromId > 70000){
                if(fromId < 500000){
                	if (!vertexMap.containsKey(fromId)) {
                        vertexMap.put(fromId, new Vertex(fromId));
                        vertexIdList.add(vertexMap.get(fromId));
                    }
                    if (!vertexMap.containsKey(toId)) 
                    {
                    	vertexMap.put(toId, new Vertex(toId));
                    	vertexIdList.add(vertexMap.get(toId));
                    }
                    fromVertex = vertexMap.get(fromId);
                    toVertex = vertexMap.get(toId);
                    fromVertex.getNeighbor().add(toVertex);
                    toVertex.getNeighbor().add(fromVertex);
                }
                edgeNum ++;
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		G.setVertexMap(vertexMap);
		G.setEdgeSize(edgeNum);
		G.setVertexSize(vertexMap.size());
		G.setVertexIdList(vertexIdList);
		return G;
	}
	
	
	
	
}
