package com.heu.cswg.test;

import java.util.HashMap;
import com.heu.cswg.file.FileConverter;
import com.heu.cswg.model.Graph;

public class neibTest {
	
	public static  void neibtest()
	{
		Graph G = FileConverter.FileToGraph("src/data/com-dblp.ungraph.txt");//读入数据
		System.out.println("原始图顶点个数:" + G.getVertexSize());
		HashMap<Integer,Integer> neib = new HashMap<Integer,Integer> ();
		for(int i=0;i<G.getVertexIdList().size();i++)
		{
			neib.put(G.getVertexIdList().get(i).getId(),G.getVertexIdList().get(i).getNeighbor().size());
		}
		
		
	}

}
