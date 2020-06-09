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
		Graph G = FileConverter.FileToGraph("src/data/com-amazon.ungraph.txt");//读入数据
//		Graph G = FileConverter.FileToGraph("E:/AttributedinfluencalCS/CommunitySearchOverWeightGraph/src/data/com-f0.ungraph.txt");//读入数据
		System.out.println("原始图顶点个数:" + G.getVertexSize());
		Initialization ob = new Initialization();
		ob.edgeWeightInit(G);//给边赋权重
		System.out.println("初始化完成");
		/*
			 // 以下测试权重
		float union = ob.unionNum(G.getVertexMap().get(0), G.getVertexMap().get(2));
		float insert = ob.support(G.getVertexMap().get(0), G.getVertexMap().get(2));
		System.out.println("并交集："+union+","+insert);
		System.out.println("权重："+G.getVertexMap().get(0).getNeighborWeight().get(2));
		*/
		Preproccess o = new Preproccess();
		Greedy o1 = new Greedy();
		int qId = 46703;
		int k = 3;
		int d = 3;
		long startTime=System.currentTimeMillis();  //获取开始时间
		List<Vertex> dlist = o.findDVertex(G,G.getVertexMap().get(qId),d);//找距离查询顶点小于d的顶点集
		Graph G0 = o.inducedSubGraph(dlist, G);//将上一步得到的顶点集连成子图

		G0 = o.influenceMaintain(G0, (float) 0.6, G.getVertexMap().get(qId));
		/*
		 * 测试评分函数
		 */
		float score = o1.function(G0, G,G.getVertexMap().get(qId));
		System.out.println("此时G0的评分："+score);
		System.out.println("第一次生成G0的顶点个数："+dlist.size()); 
		o.kdTrussMaintain(G0, G.getVertexMap().get(qId),k,d);//将得到的子图剪枝成(k，d)-truss
		System.out.println("第一次kdtruss剪枝后的图顶点个数："+G0.getVertexMap().size());

		//System.out.println("移除顶点个数："+ (G.getVertexMap().size() - G0.getVertexMap().size()));
		//System.out.println("此时G的大小："+G.getVertexMap().size());
		Graph A = o1.greedyWeight(G0,G,k,d,G0.getVertexMap().get(qId));//候选图通过贪心算法GCSM(H)进行搜索得到G'
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		System.out.println("最终结果："+A.getVertexIdList().size());
		
		
		System.out.println("最终结果评分："+o1.function(A, G,G.getVertexMap().get(qId)));
		for(int xx=0;xx<A.getVertexIdList().size();xx++)
		{
			float beldeg = o1.belDegree(G,A,A.getVertexIdList().get(xx));
			System.out.println(A.getVertexIdList().get(xx).getId()+" "+beldeg);
		}
		
		
	}
	
	public static void main(String[] args)
	{
		firsttest();
		System.out.println("———End———");
		
	}

}
