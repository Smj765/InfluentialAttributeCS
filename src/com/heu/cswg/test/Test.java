package com.heu.cswg.test;

import java.util.List;

import Utils.StringFactory;

import com.heu.cswg.model.Graph;
import com.heu.cswg.model.Vertex;
import com.heu.cswg.algorithm.*;
import com.heu.cswg.file.*;


public class Test {
	
	/*
	 * 2018-05-21 LiuChiming
	 */
	
	public static  void firsttest()
	{
		Graph G = FileConverter.FileToGraph("src/data/com-amazon.ungraph.txt");//��������
//		Graph G = FileConverter.FileToGraph("E:/AttributedinfluencalCS/CommunitySearchOverWeightGraph/src/data/com-f0.ungraph.txt");//��������
		System.out.println("ԭʼͼ�������:" + G.getVertexSize());
		Initialization ob = new Initialization();
		ob.edgeWeightInit(G);//���߸�Ȩ��
		System.out.println("��ʼ�����");
		/*
			 // ���²���Ȩ��
		float union = ob.unionNum(G.getVertexMap().get(0), G.getVertexMap().get(2));
		float insert = ob.support(G.getVertexMap().get(0), G.getVertexMap().get(2));
		System.out.println("��������"+union+","+insert);
		System.out.println("Ȩ�أ�"+G.getVertexMap().get(0).getNeighborWeight().get(2));
		*/
		Preproccess o = new Preproccess();
		Greedy o1 = new Greedy();
		int qId = 46703;
		int k = 3;
		int d = 3;
		long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		List<Vertex> dlist = o.findDVertex(G,G.getVertexMap().get(qId),d);//�Ҿ����ѯ����С��d�Ķ��㼯
		Graph G0 = o.inducedSubGraph(dlist, G);//����һ���õ��Ķ��㼯������ͼ

		G0 = o.influenceMaintain(G0, (float) 0.6, G.getVertexMap().get(qId));
		/*
		 * �������ֺ���
		 */
		float score = o1.function(G0, G,G.getVertexMap().get(qId));
		System.out.println("��ʱG0�����֣�"+score);
		System.out.println("��һ������G0�Ķ��������"+dlist.size()); 
		o.kdTrussMaintain(G0, G.getVertexMap().get(qId),k,d);//���õ�����ͼ��֦��(k��d)-truss
		System.out.println("��һ��kdtruss��֦���ͼ���������"+G0.getVertexMap().size());

		//System.out.println("�Ƴ����������"+ (G.getVertexMap().size() - G0.getVertexMap().size()));
		//System.out.println("��ʱG�Ĵ�С��"+G.getVertexMap().size());
		Graph A = o1.greedyWeight(G0,G,k,d,G0.getVertexMap().get(qId));//��ѡͼͨ��̰���㷨GCSM(H)���������õ�G'
		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		System.out.println("��������ʱ�䣺 "+(endTime-startTime)+"ms");
		System.out.println("���ս����"+A.getVertexIdList().size());
		
		
		System.out.println("���ս�����֣�"+o1.function(A, G,G.getVertexMap().get(qId)));
		for(int xx=0;xx<A.getVertexIdList().size();xx++)
		{
			float beldeg = o1.belDegree(G,A,A.getVertexIdList().get(xx));
			System.out.println(A.getVertexIdList().get(xx).getId()+" "+beldeg);
		}
		
		
	}
	
	public static void main(String[] args)
	{
		firsttest();
		System.out.println("������End������");
		
	}

}
