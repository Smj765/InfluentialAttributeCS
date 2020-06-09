package com.heu.cswg.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.text.DecimalFormat;

import com.heu.cswg.model.Graph;
import com.heu.cswg.model.Vertex;

public class Greedy {
	
	public float attrScore(Graph G, Vertex v)
	{
		float score = 0;
		for(int i=0;i<v.getAttr().size();i++)//遍历节点v的属性
		{
			System.out.println("v.getAttr().size():" + v.getAttr().size());
			for(int j=0;j<G.getVertexIdList().size();j++)//遍历图G的所有节点
			{
				float attrNum = 0;
				if(G.getVertexIdList().get(j).getAttr().contains(v.getAttr().get(i)))
				{
					attrNum++;
				}
				score = score + attrNum * attrNum;
			}
		}
		score = score / G.getVertexIdList().size();
		System.out.println("score:" + score);
		return score;
	}
	
	/*
	 * 2018-06-11 LiuChiming
	 */
	public float cutWeight(Graph G0,Graph G)//求社区G cut edges的权重
	{
		float cutWeight = 0;
		for(int i=0;i<G0.getVertexIdList().size();i++)
		{
			for(int j=0;j<G.getVertexMap().get(G0.getVertexIdList().get(i).getId()).getNeighbor().size();j++)
			{
				if(!(G0.getVertexMap().containsKey(G.getVertexMap().get(G0.getVertexIdList().get(i).getId()).getNeighbor().get(j).getId())))
				{
					cutWeight += G.getVertexMap().get(G0.getVertexIdList().get(i).getId()).getNeighborWeight().get(G.getVertexMap().get(G0.getVertexIdList().get(i).getId()).getNeighbor().get(j).getId());
				}
			}
		}
		return cutWeight;
	}
	
	public float communityWeight(Graph G0,Graph G)//求社区G的全部权重(包括cut edge权重)
	{
		float comWeight = 0;
		for(int i=0;i<G0.getVertexIdList().size();i++)
		{
			for(int j=0;j<G0.getVertexIdList().get(i).getNeighbor().size();j++)
			{
				//comWeight += G.getVertexMap().get(G0.getVertexIdList().get(i).getId()).getNeighborWeight().get(G0.getVertexIdList().get(i).getNeighbor().get(j).getId());
				comWeight += G0.getVertexIdList().get(i).getNeighborWeight().get(G0.getVertexIdList().get(i).getNeighbor().get(j).getId());
			}
		}
		comWeight = (comWeight/2) +cutWeight(G0,G);
		return comWeight;
	}
	
	public void PrecRec(int[] so,Graph G, Graph G0){//precision、recall和F1,G为初始化图，G0为得到的结果图
		int p = 0;
		int len = 0;
		for(int j = 0; j <so.length ; j ++){	
			if( isContains(G.getVertexIdList(),so[j]) ){
				len++;
				System.out.println("so"+ j + ":" + so[j]);
			}
		}
		for(int i=0;i<G0.getVertexIdList().size(); i++){
			for(int j = 0; j <so.length ; j ++){	
				if( isContains(G.getVertexIdList(),so[j])){
					if(G0.getVertexIdList().get(i).getId() == so[j]){
						p++;
					}
				}
			}
		}
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数
		float prec = (float)p / (float) G0.getVertexIdList().size();
		String sp = df.format(prec);
		System.out.println("Precision:" +sp);
		float rec = (float)p / (float)len;
		String sr = df.format(rec);
		System.out.println("Recall:" + sr);
		float f1 = 2*prec*rec/(prec + rec);
		String s = df.format(f1);
		System.out.println("F1:" + s);
	}

	
	public float function(Graph G0,Graph G,Vertex q)//社区的score函数
	{
		float score = cutWeight(G0,G) / communityWeight(G0,G);
		return score+attrScore(G, q);//这里实验有无影响力
//		return score;//这里实验无影响力
	}
	
	public float belDegree(Graph G,Graph H,Vertex v)//计算一个社区G中节点v的belonging degree
	{
		float deg = 0;
		//求节点v在社区内的边权和
		float weightIncom =0;
		for(int i=0;i<G.getVertexMap().get(v.getId()).getNeighbor().size();i++)
		{
			if(H.getVertexMap().containsKey(G.getVertexMap().get(v.getId()).getNeighbor().get(i).getId()))
			{
				weightIncom += G.getVertexMap().get(v.getId()).getNeighborWeight().get(G.getVertexMap().get(v.getId()).getNeighbor().get(i).getId());
			}
		}
		//求节点v在原始图中的所有边权和
		float weightAll = 0;
		for(int i=0;i<G.getVertexMap().get(v.getId()).getNeighbor().size();i++)
		{
			weightAll += G.getVertexMap().get(v.getId()).getNeighborWeight().get(G.getVertexMap().get(v.getId()).getNeighbor().get(i).getId());
		}
		if(weightAll == 0)
		{
			return (float)0.00000001;
		}
		deg = weightIncom / weightAll;
		return deg;
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
	
	public Graph greedyWeight(Graph H,Graph G,int k,int d,Vertex q)//贪心函数,参数H为社区,参数G为原始图
	{
		float score = function(H,G,q);
		Graph H0 = new Graph();
		for(int a=0;a<H.getVertexIdList().size();a++)//给H0的VertexList赋初值
		{
			Vertex v = new Vertex();
			v.setId(H.getVertexIdList().get(a).getId());
			for(int x=0;x<H.getVertexIdList().get(a).getAttr().size();x++)//把属性挨个赋给新节点
			{
				v.getAttr().add(H.getVertexIdList().get(a).getAttr().get(x));
			}
			for(int y=0;y<H.getVertexIdList().get(a).getNeighbor().size();y++)//把邻居挨个赋给新节点
			{
				v.getNeighbor().add(H.getVertexIdList().get(a).getNeighbor().get(y));
			}
			for(Integer in : H.getVertexIdList().get(a).getNeighborWeight().keySet())//权重赋值
			{
				v.getNeighborWeight().put(in, H.getVertexIdList().get(a).getNeighborWeight().get(in));
			}
			H0.getVertexIdList().add(v);
		}
		for (Integer in : H.getVertexMap().keySet()) 
		{
			//map.keySet()返回的是所有key的值
		    Vertex u = new Vertex();
			u.setId(in);
			for(int x=0;x<H.getVertexMap().get(in).getAttr().size();x++)//把属性挨个赋给新节点
			{
				u.getAttr().add(H.getVertexMap().get(in).getAttr().get(x));
			}
			for(int y=0;y<H.getVertexMap().get(in).getNeighbor().size();y++)//把邻居挨个赋给新节点
			{
				u.getNeighbor().add(H.getVertexMap().get(in).getNeighbor().get(y));
			}
			for(Integer xy: H.getVertexMap().get(in).getNeighborWeight().keySet())//权重赋值
			{
				u.getNeighborWeight().put(xy, H.getVertexMap().get(in).getNeighborWeight().get(xy));
			}
			H0.getVertexMap().put(in,u);
		 }
		H0.setVertexSize(H0.getVertexMap().size());
		while(H.getVertexMap().size()>k)
		{
			if(score > function(H,G,q))
			{
				//System.out.println("if里");
				
				H0 = new Graph();
				for(int a=0;a<H.getVertexIdList().size();a++)//给H0的VertexList赋初值
				{
					Vertex v = new Vertex();
					v.setId(H.getVertexIdList().get(a).getId());
					for(int x=0;x<H.getVertexIdList().get(a).getAttr().size();x++)//把属性挨个赋给新节点
					{
						v.getAttr().add(H.getVertexIdList().get(a).getAttr().get(x));
					}
					for(int y=0;y<H.getVertexIdList().get(a).getNeighbor().size();y++)//把邻居挨个赋给新节点
					{
						v.getNeighbor().add(H.getVertexIdList().get(a).getNeighbor().get(y));
					}
					for(Integer in : H.getVertexIdList().get(a).getNeighborWeight().keySet())//权重赋值
					{
						v.getNeighborWeight().put(in, H.getVertexIdList().get(a).getNeighborWeight().get(in));
					}
					H0.getVertexIdList().add(v);
				}
				for (Integer in : H.getVertexMap().keySet()) 
				{
					//map.keySet()返回的是所有key的值
				    Vertex u = new Vertex();
					u.setId(in);
					for(int x=0;x<H.getVertexMap().get(in).getAttr().size();x++)//把属性挨个赋给新节点
					{
						u.getAttr().add(H.getVertexMap().get(in).getAttr().get(x));
					}
					for(int y=0;y<H.getVertexMap().get(in).getNeighbor().size();y++)//把邻居挨个赋给新节点
					{
						u.getNeighbor().add(H.getVertexMap().get(in).getNeighbor().get(y));
					}
					for(Integer xy: H.getVertexMap().get(in).getNeighborWeight().keySet())//权重赋值
					{
						u.getNeighborWeight().put(xy, H.getVertexMap().get(in).getNeighborWeight().get(xy));
					}
					H0.getVertexMap().put(in,u);
				 }
				H0.setVertexSize(H0.getVertexMap().size());
				
//				for(int a=0;a<H0.getVertexIdList().size();a++)
//				{
//					if(!H.getVertexIdList().contains(H0.getVertexIdList().get(a)))
//					{
//						//System.out.print("删除 ");
//						H0.getVertexIdList().remove(a);
//					}
//				}
//				for(Integer b: H0.getVertexMap().keySet())//权重赋值
//				{
//					if(!H.getVertexMap().containsKey(b))
//					{
//						H0.getVertexMap().remove(b);
//					}
//				}
//				H0.setVertexSize(H0.getVertexIdList().size());
				
				score = function(H,G,q);
			}
			float beldeg = 1;
			Vertex deleV = null;
			for(int i=0;i<H.getVertexIdList().size();i++)
			{
				if(H.getVertexIdList().get(i).getId() == q.getId())
				{
					continue;
				}
				if(beldeg > belDegree(G,H,H.getVertexIdList().get(i)))
				{
					beldeg = belDegree(G,H,H.getVertexIdList().get(i));
					deleV = H.getVertexIdList().get(i);
				}
			}
			//System.out.println("删除节点id"+deleV.getId());
			//删除Belong Degree最小的节点
			/*
			for(int j=0;j<H.getVertexMap().get(minBelongId).getNeighbor().size();j++)
			{
				H.getVertexMap().get(minBelongId).getNeighbor().get(j).getNeighbor().remove(H.getVertexMap().get(minBelongId));
			}
			H.getVertexMap().remove(minBelongId);
			H.getVertexIdList().remove(H.getVertexMap().get(minBelongId));
			H.setVertexSize(H.getVertexMap().size());
			//修剪
			kdTrussMaintain (H,q,k,d);
			*/
			if(deleV.getId() == q.getId())
			{
				System.out.println("删到查询节点");
				break;
			}
			
			/*
			 * 先删边
			 */
			
			for(int k1=0;k1<deleV.getNeighbor().size();k1++)
			{
				if(isContains(deleV.getNeighbor().get(k1).getNeighbor(),deleV.getId()))
				{
					deleV.getNeighbor().get(k1).getNeighbor().remove(deleV);
				}
			}
			
			/*
			 * 删点
			 */
			H.getVertexMap().remove(deleV.getId());
			H.getVertexIdList().remove(deleV);
			H.setVertexSize(H.getVertexMap().size());
			//System.out.println("H的大小："+H.getVertexMap().size());
			//System.out.println("H0的大小："+H0.getVertexIdList().size());

			kdTrussMaintain (H,q,k,d);
		}
		return H0;
		
	}

}
