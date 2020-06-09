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
	public float unionNum(Vertex v1,Vertex v2)//����������ھӲ���
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
	
	public float support (Vertex v1,Vertex v2)//��ߵ�֧�ֶ�(��������ھӽ�����Ŀ)
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
	
	public void edgeWeightInit(Graph G)//��ͼ�б߸���Ȩ��
	{
		for(int i=0;i<G.getVertexIdList().size();i++)//����ͼ�нڵ�
		{
			for(int j=0;j<G.getVertexIdList().get(i).getNeighbor().size();j++)//���������ڵ���ھ�
			{	
				
				float weight = Jaccard(unionNum(G.getVertexIdList().get(i),G.getVertexIdList().get(i).getNeighbor().get(j)),support (G.getVertexIdList().get(i),G.getVertexIdList().get(i).getNeighbor().get(j)));
				//System.out.println("i:" + G.getVertexIdList().get(i).getId() + "��j:" + G.getVertexIdList().get(j).getId() + "��weight��" + weight);
				//��Ȩֵ
				G.getVertexIdList().get(i).getNeighborWeight().put(G.getVertexIdList().get(i).getNeighbor().get(j).getId(), weight);
				G.getVertexIdList().get(i).getNeighbor().get(j).getNeighborWeight().put(G.getVertexIdList().get(i).getId(), weight);
			}
		}
	}
	
	//�������������ֵ 
	
	//ѡȡ�����
	public static int[] randomArray(int min,int max,int n){  
	    int len = max-min+1;  
	      
	    if(max < min || n > len){  
	        return null;  
	    }  
	      
	    //��ʼ��������Χ�Ĵ�ѡ����  
	    int[] source = new int[len];  
	       for (int i = min; i < min+len; i++){  
	        source[i-min] = i;  
	       }  
	         
	       int[] result = new int[n];  
	       Random rd = new Random();  
	       int index = 0;  
	       for (int i = 0; i < result.length; i++) {  
	        //��ѡ����0��(len-2)���һ���±�  
	           index = Math.abs(rd.nextInt() % len--);  
	           //�������������������  
	           result[i] = source[index];  
	           //����ѡ�����б�������������ô�ѡ����(len-1)�±��Ӧ�����滻  
	           source[index] = source[len];  
	       }  
	       return result;
	}
	
	//������ֵ
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
