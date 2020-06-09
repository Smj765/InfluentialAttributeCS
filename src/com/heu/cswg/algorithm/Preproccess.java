package com.heu.cswg.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.heu.cswg.model.BFSNode;
import com.heu.cswg.model.Graph;
import com.heu.cswg.model.Vertex;
import Utils.StringFactory;

public class Preproccess {
	
	
	public float mipCompute (Graph G,Vertex vq,Vertex v)//计算两节点的MIP
	{
//		HashMap<Integer, Float> dist=new HashMap<Integer, Float>();//<节点id，与查询节点vq的MIP>
//		HashMap<Integer,Boolean> S = new HashMap<Integer,Boolean>();//已找到MIP的节点集合 <节点ID，1或者0>
//
//		//(1)初始化
//		for(int i=0;i<G.getVertexIdList().size();i++)
//		{
//			if (G.getVertexIdList().get(i).getNeighbor().contains(vq))
//			{
//				dist.put(G.getVertexIdList().get(i).getId(), G.getVertexIdList().get(i).getNeighborWeight().get(vq.getId()));
//			}
//			else
//			{
//				dist.put(G.getVertexIdList().get(i).getId(), (float) 0);
//			}
//		}		
//		S.put(vq.getId(), true);//查询节点放进S集
//		System.out.println("198行");
//		while(!S.containsKey(v.getId()))
//		{
//			//(2)找当前最大影响的节点
//			//System.out.println("202行");
//			int u = -1;//节点u的id
//			float temp = 0;
//			for(int j=0;j<G.getVertexIdList().size();j++)
//			{
//				//float max = Float.MIN_VALUE;			
//				if((!(S.containsKey(G.getVertexIdList().get(j).getId()))) && dist.get(G.getVertexIdList().get(j).getId())>temp)
//				{
//					u = G.getVertexIdList().get(j).getId();
//					temp = dist.get(G.getVertexIdList().get(j).getId());
//				}
//				
//			}
//			if(u != -1)
//			{
//				S.put(u, true);
//			    //(3) 修改MIP
//				//System.out.println("217行");
//				for(int i=0;i<G.getVertexIdList().size();i++)
//				{
//					if(!S.containsKey(G.getVertexIdList().get(i).getId()))
//					{
//						if(G.getVertexIdList().get(i).getNeighbor().contains(G.getVertexMap().get(u)) && (dist.get(u)*G.getVertexIdList().get(i).getNeighborWeight().get(u)) > dist.get(G.getVertexIdList().get(i).getId()))
//						{
//							dist.put(G.getVertexIdList().get(i).getId(), dist.get(u) * G.getVertexIdList().get(i).getNeighborWeight().get(u));
//						}
//					}
//				}
//			}
//			else
//			{
//				return 0;
//			}
//		}
//		System.out.println("mip" + dist.get(G.getVertexMap().get(v.getId()).getId()));				
//		return dist.get(G.getVertexMap().get(v.getId()).getId());
		Queue<BFSNode> queue = new LinkedList<>();
		BFSNode node = new BFSNode();
		float maxScore = 0.05F;
		node.setId( vq.getId());
		node.setfId(vq.getId());//起始节点vq的前节点是自己
		node.setNodeScore(1);//起始节点的分数是1
		queue.add(node);

		while(!queue.isEmpty()){
			BFSNode top = queue.poll();
			if(top.getId() == v.getId()){
				if(top.getNodeScore() > maxScore){//弹出，更新max
					maxScore = top.getNodeScore() ;//更新max
					
					queue.poll();//当前top为目标节点v，找到一个更好地maxScore
					continue;
				}
				else{
					queue.poll();//当前top为目标节点v，得到的分数并不大于maxScore
					continue;
				}
			}
			
			if(top.getNodeScore() < maxScore){//当前top的分支被剪枝
				queue.poll();
				continue;
			}
//			if (G.getVertexMap().get(top.getId()).getNeighbor().size() == 0) {
//				continue;
//			}
			for(Vertex i : G.getVertexMap().get(top.getId()).getNeighbor()){//对当前top的邻居遍历				
				if(i.getId() == top.getfId()){//当前操作的邻居节点是不是top的父节点
					continue;
				}
				
//				float weight = G.getVertexMap().get(top.getId()).getNeighborWeight().get(i.getId());
//				System.out.println("................." + weight);
				// IAB 的方式计算
//				float weight = getIAB(G, top, i);
				// Jaccard 系数
				float weight = getJaccard(G, top, i);
//				System.out.println(weight);
				// 自己修改注释
				float tmp = top.getNodeScore() * weight;//
				if( tmp <= maxScore ){
					queue.poll();//当前top所代表路径被剪枝
					continue;
				}
				BFSNode nb = new BFSNode();
				nb.setId(i.getId());
				nb.setNodeScore(tmp);
				nb.setfId(top.getId());//这个邻居入队列，设置该节点的NodeScore和父节点
				queue.add(nb);
			}
		}
		System.out.println(vq.getId()+"到"+v.getId()+"的maxScore："+maxScore);
		return maxScore;
	}

	private static float getIAB(Graph G, BFSNode from, Vertex to) {
		int M = G.getEdgeSize();
		int kA = G.getVertexMap().get(from.getId()).getNeighbor().size();
		int kB = G.getVertexMap().get(to.getId()).getNeighbor().size();
//		System.out.println("kA:"+kA);
//		System.out.println("kB:"+kB);
		float iab = (float) -Math.log(1-StringFactory.C(M, kA, kB))/G.getMaxIAB();
//		System.out.println("G.getMaxIAB():"+G.getMaxIAB());
//		System.out.println("iab:"+iab);
		return iab;
	}
	
	private static float getJaccard(Graph G, BFSNode from, Vertex to) {
		List<Vertex> nFrom = new ArrayList<>(G.getVertexMap().get(from.getId()).getNeighbor());
		List<Vertex> nTo = new ArrayList<>(G.getVertexMap().get(to.getId()).getNeighbor());
//		System.out.println(from.getId() + " : " + nFrom);
//		System.out.println(to.getId() + " : " + nTo);
		nFrom.retainAll(nTo);
		int n = nFrom.size();
		nFrom = new ArrayList<>(G.getVertexMap().get(from.getId()).getNeighbor());
		nFrom.removeAll(nTo);
		nFrom.addAll(nTo);
		int m = nFrom.size();
//		System.out.println(n + ", " + m);
		return (float) n / m;
	}
	
	public Graph influenceMaintain (Graph G, float p,Vertex q)//对候选图中的影响力值进行处理,p是阈值,q是查询节点
	{
		for(int i=0;i<G.getVertexIdList().size();i++)
		{
			//System.out.println("当前节点   "+G.getVertexIdList().get(i).getId());
			if(mipCompute (G, q, G.getVertexIdList().get(i)) < p)
			{
				for(int j=0;j<G.getVertexIdList().get(i).getNeighbor().size();j++)//遍历要删除节点i的邻居
				{
					G.getVertexIdList().get(i).getNeighbor().get(j).getNeighbor().remove(G.getVertexIdList().get(i));
				}
				G.getVertexMap().remove(G.getVertexIdList().get(i).getId());
				G.getVertexIdList().remove(i);		
				G.setVertexSize(G.getVertexIdList().size());
			}
		}
		if(!G.getVertexMap().containsKey(q.getId()))
		{
			G.getVertexMap().put(q.getId(), q);
			G.getVertexIdList().add(q);
			G.setVertexSize(G.getVertexIdList().size());
		}
		return G;
	}
	
	/*
	 * 1
	 */
	public List<Vertex> findDVertex(Graph G,Vertex vId,int d)//找距离查询顶点小于d的顶点集合
	{
		List<Vertex> dVertexList = new ArrayList<Vertex> ();//存储结果的变量
		BlockingQueue<Vertex> Q= new LinkedBlockingQueue<Vertex>();
		vId.setVisit(true);
		Q.add(vId);
		while(true)
		{
			if(Q.isEmpty())//队列为空则退出循环
			{
				break;
			}
			if(Q.peek().getDist()>d)//队首元素与查询节点的距离大于d则将其从队列中删除
			{
				Q.poll();
				continue;
			}
			dVertexList.add(Q.peek());//若不是上述两种情况，将队首元素加入结果变量中
			Q.poll();//然后队首出队
			for(int i=0;i<dVertexList.get(dVertexList.size()-1).getNeighbor().size();i++)
			{
				//判断刚刚出队的元素的邻居节点们是否访问过
				if(!(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).isVisit()))
				{
					//以下三句
					//将刚刚出队的元素的邻居节点设置为已访问，入队，距离设置为+1
					dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).setVisit(true);
					Q.add(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i));
					dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).setDist(1+dVertexList.get(dVertexList.size()-1).getDist());
				}
				
			}
		}
		List<Vertex> d1 = new ArrayList<Vertex> ();
		for (int j=0;j<dVertexList.size();j++)//归零
		{
			if(G.getVertexMap().containsKey(dVertexList.get(j).getId()))
			{
				Vertex v = new Vertex(dVertexList.get(j).getId());
				d1.add(v);
			}
			dVertexList.get(j).setDist(0);
			dVertexList.get(j).setVisit(false);
		}
		return d1;
	}
	
	public boolean isContains(List<Vertex> VertexList,int vId)//判断一个list中是否有ID为vId的节点
	{
		for(int a=0;a<VertexList.size();a++)
		{
			if (VertexList.get(a).getId() == vId)
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	/*
	 * 2
	 */
	public Graph inducedSubGraph (List<Vertex> VertexSet,Graph G)//将距离Vq不大于d的顶点集连成子图
	{
		for (int i=0;i<VertexSet.size();i++)//如果顶点i与j在图G中是连通的，则在这里连通i，j
		{
			for (int j=i+1;j<VertexSet.size();j++)
			{
				//System.out.println("test:"+G.getVertexMap().get(VertexSet.get(i).getId()).getNeighbor().size());
				
				if(isContains(G.getVertexMap().get(VertexSet.get(i).getId()).getNeighbor(),VertexSet.get(j).getId()))//如果在原始图G中i的邻居里有j
				{
					VertexSet.get(i).getNeighbor().add(VertexSet.get(j));
					VertexSet.get(j).getNeighbor().add(VertexSet.get(i));
					VertexSet.get(i).getNeighborWeight().put(VertexSet.get(j).getId(),G.getVertexMap().get(VertexSet.get(i).getId()).getNeighborWeight().get(VertexSet.get(j).getId()));
					VertexSet.get(j).getNeighborWeight().put(VertexSet.get(i).getId(),G.getVertexMap().get(VertexSet.get(j).getId()).getNeighborWeight().get(VertexSet.get(i).getId()));
				}
			}
		}
		//生成子图G0
		Graph G0 = new Graph();
		G0.setVertexSize(VertexSet.size());
		int edgeNum = 0;
		for(int k=0;k<VertexSet.size();k++)
		{
			G0.getVertexIdList().add(VertexSet.get(k));
			G0.getVertexMap().put(VertexSet.get(k).getId(),VertexSet.get(k));
			edgeNum += VertexSet.get(k).getNeighbor().size();
		}
		G0.setEdgeSize(edgeNum);
		G0.initIAB();
		return G0;
	
	}

	/*
	 * 2018-05-19 LiuChiming
	 */
	public int support (Vertex v1,Vertex v2)//求边的支持度
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
		
		return num;
	}
	
	public List<Vertex> distance (Graph G,Vertex v1,int d)//求距离v1距离大于d的顶点集合
	{
		List<Vertex> dVertexList = new ArrayList<Vertex> ();//存储结果的变量
		BlockingQueue<Vertex> Q= new LinkedBlockingQueue<Vertex>();
		v1.setVisit(true);
		Q.add(v1);
		while(true)
		{
			if(Q.isEmpty())//队列为空则退出循环
			{
				break;
			}
			if(Q.peek().getDist()<d||Q.peek().getDist()==d)//队首元素与查询节点的距离大于d则将其从队列中删除
			{
				Q.poll();
				continue;
			}
			dVertexList.add(Q.peek());//若不是上述两种情况，将队首元素加入结果变量中
			Q.poll();//然后队首出队
			for(int i=0;i<dVertexList.get(dVertexList.size()-1).getNeighbor().size();i++)
			{
				//判断刚刚出队的元素的邻居节点们是否访问过
				if(!(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).isVisit()))
				{
					//以下三句
					//将刚刚出队的元素的邻居节点设置为已访问，入队，距离设置为+1
					dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).setVisit(true);
					Q.add(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i));
					dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).setDist(1+dVertexList.get(dVertexList.size()-1).getDist());
				}
			}
		}
		List<Vertex> d1 = new ArrayList<Vertex>();
		for(int j=0;j<dVertexList.size();j++)
		{
			if(G.getVertexMap().containsKey(dVertexList.get(j).getId()))
			{
				Vertex v = new Vertex(dVertexList.get(j).getId());
				d1.add(v);
			}
			dVertexList.get(j).setDist(0);
			dVertexList.get(j).setVisit(false);
		}
		
		return d1;
		
	}
	
	/*
	 * 3
	 */
	public void kdTrussMaintain (Graph G,Vertex vId,int k,int d)//将图剪枝为(k,d)-truss
	{
		while(true)
		{
			/*
			 * 删除支持度小于（k-2）的边
			 */
			for (int i=0;i<G.getVertexIdList().size();i++)
			{
				for (int j=i+1;j<G.getVertexIdList().size();j++)
				{
					if(support(G.getVertexIdList().get(i),G.getVertexIdList().get(j))<(k-2))
					{
						if(isContains(G.getVertexIdList().get(i).getNeighbor(),G.getVertexIdList().get(j).getId()))
						{
							 G.getVertexIdList().get(i).getNeighbor().remove(G.getVertexIdList().get(j));
							 G.getVertexIdList().get(j).getNeighbor().remove(G.getVertexIdList().get(i));
						}
						
					}
				}
			}
			for(int x=0;x<G.getVertexIdList().size();x++)
			{
				if(G.getVertexIdList().get(x).getNeighbor().size() == 0)
				{
					G.getVertexMap().remove(G.getVertexIdList().get(x).getId());
					G.getVertexIdList().remove(G.getVertexIdList().get(x));
					G.setVertexSize(G.getVertexMap().size());
				}
			}
			/*
			 * 删除距离查询节点大于d的点
			 */
			//System.out.println("G map size"+G.getVertexMap().size());
			//System.out.println("G list size"+G.getVertexIdList().size());
			List<Vertex> dset = distance(G,vId,d);//求距离大于d的顶点集
			for (int k1=0;k1<dset.size();k1++)
			{
				for(int k2=0;k2<dset.get(k1).getNeighbor().size();k2++)
				{
					//删除多余边
					dset.get(k1).getNeighbor().get(k2).getNeighbor().remove(dset.get(k1));
				}
				G.getVertexMap().remove(dset.get(k1).getId());
			}
			G.getVertexIdList().removeAll(dset);
			G.setVertexSize(G.getVertexMap().size());
			if(dset.size()==0)
			{
				if(!(isContains(G.getVertexIdList(),vId.getId())))
				{
					G.getVertexMap().put(vId.getId(), vId);
					G.getVertexIdList().add(0,vId);
					G.setVertexSize(G.getVertexMap().size());
				}
				break;
			}
			
		}

	}

}
