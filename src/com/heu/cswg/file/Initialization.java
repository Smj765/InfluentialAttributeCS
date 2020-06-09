package com.heu.cswg.file;

import com.heu.cswg.model.Vertex;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.heu.cswg.model.Graph;

public class Initialization {
	
	/*
	 * 2018-06-11 LiuChiming
	 */
	public float unionNum(Vertex v1,Vertex v2)//求两顶点的邻居并集
	{
		Set <Vertex> vertexUnion = new HashSet<> ();
		for(int i=0;i<v1.getNeighbor().size();i++)
		{
			vertexUnion.add(v1.getNeighbor().get(i));
		}
		for(int j=0;j<v2.getNeighbor().size();j++)
		{
			vertexUnion.add(v2.getNeighbor().get(j));
		}
		float union = (float)(vertexUnion.size());
		return union;
	}
	
	public float support (Vertex v1,Vertex v2)//求边的支持度(两顶点的邻居交集数目)
	{
		int num = 0;
		for(int i=0;i<v1.getNeighbor().size();i++)
		{
			for(int j=0;j<v2.getNeighbor().size();j++)
			{
				if(v1.getNeighbor().get(i).getId() == v2.getNeighbor().get(j).getId())
				{
					num++;
				}
			}
		}
		float insert = (float) num;
		return insert;
	}
	
	public float Jaccard(float union,float insertion)
	{
		float jac = (insertion/union);
		return jac;	
	}
	
	public void edgeWeightInit(Graph G)//给图中边赋予权重
	{
		for(int i=0;i<G.getVertexIdList().size();i++)//遍历图中节点
		{
			for(int j=0;j<G.getVertexIdList().get(i).getNeighbor().size();j++)//遍历各个节点的邻居
			{	
				
				float weight = Jaccard(unionNum(G.getVertexIdList().get(i),G.getVertexIdList().get(i).getNeighbor().get(j)),support (G.getVertexIdList().get(i),G.getVertexIdList().get(i).getNeighbor().get(j)));
				//System.out.println("i:" + G.getVertexIdList().get(i).getId() + "与j:" + G.getVertexIdList().get(j).getId() + "的weight：" + weight);
				//赋权值
				G.getVertexIdList().get(i).getNeighborWeight().put(G.getVertexIdList().get(i).getNeighbor().get(j).getId(), weight);
				G.getVertexIdList().get(i).getNeighbor().get(j).getNeighborWeight().put(G.getVertexIdList().get(i).getId(), weight);
			}
		}
	}
	
	//以下随机赋属性值 
	
	//选取随机数
	public static int[] randomArray(int min,int max,int n){  
	    int len = max-min+1;  
	      
	    if(max < min || n > len){  
	        return null;  
	    }  
	      
	    //初始化给定范围的待选数组  
	    int[] source = new int[len];  
	       for (int i = min; i < min+len; i++){  
	        source[i-min] = i;  
	       }  
	         
	       int[] result = new int[n];  
	       Random rd = new Random();  
	       int index = 0;  
	       for (int i = 0; i < result.length; i++) {  
	        //待选数组0到(len-2)随机一个下标  
	           index = Math.abs(rd.nextInt() % len--);  
	           //将随机到的数放入结果集  
	           result[i] = source[index];  
	           //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换  
	           source[index] = source[len];  
	       }  
	       return result;
	}
	
	//赋属性值
	public void attrInit(Graph G)
	{
		String attr1 = "A";
		String attr2 = "B";
		String attr3 = "C";
		int graphNum = G.getVertexIdList().size();
		int randNum = (int)(graphNum*0.8); 
		int randresult1[] = randomArray(0,graphNum,randNum);
		for(int i=0;i<randresult1.length;i++)
		{
			G.getVertexIdList().get(i).getAttr().add(attr1);
		}
		int randresult2[] = randomArray(0,graphNum,randNum);
		for(int i=0;i<randresult2.length;i++)
		{
			G.getVertexIdList().get(i).getAttr().add(attr2);
		}
		int randresult3[] = randomArray(0,graphNum,randNum);
		for(int i=0;i<randresult3.length;i++)
		{
			G.getVertexIdList().get(i).getAttr().add(attr3);
		}
	}

}
