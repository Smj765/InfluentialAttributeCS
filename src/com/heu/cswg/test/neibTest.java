package com.heu.cswg.test;

import java.util.HashMap;
import com.heu.cswg.file.FileConverter;
import com.heu.cswg.model.Graph;

public class neibTest {
	
	public static  void neibtest()
	{
		Graph G = FileConverter.FileToGraph("src/data/com-dblp.ungraph.txt");//��������
		System.out.println("ԭʼͼ�������:" + G.getVertexSize());
		HashMap<Integer,Integer> neib = new HashMap<Integer,Integer> ();
		for(int i=0;i<G.getVertexIdList().size();i++)
		{
			neib.put(G.getVertexIdList().get(i).getId(),G.getVertexIdList().get(i).getNeighbor().size());
		}
		
		
	}

}
