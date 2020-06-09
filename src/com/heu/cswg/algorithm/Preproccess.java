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
	
	
	public float mipCompute (Graph G,Vertex vq,Vertex v)//�������ڵ��MIP
	{
//		HashMap<Integer, Float> dist=new HashMap<Integer, Float>();//<�ڵ�id�����ѯ�ڵ�vq��MIP>
//		HashMap<Integer,Boolean> S = new HashMap<Integer,Boolean>();//���ҵ�MIP�Ľڵ㼯�� <�ڵ�ID��1����0>
//
//		//(1)��ʼ��
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
//		S.put(vq.getId(), true);//��ѯ�ڵ�Ž�S��
//		System.out.println("198��");
//		while(!S.containsKey(v.getId()))
//		{
//			//(2)�ҵ�ǰ���Ӱ��Ľڵ�
//			//System.out.println("202��");
//			int u = -1;//�ڵ�u��id
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
//			    //(3) �޸�MIP
//				//System.out.println("217��");
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
		node.setfId(vq.getId());//��ʼ�ڵ�vq��ǰ�ڵ����Լ�
		node.setNodeScore(1);//��ʼ�ڵ�ķ�����1
		queue.add(node);

		while(!queue.isEmpty()){
			BFSNode top = queue.poll();
			if(top.getId() == v.getId()){
				if(top.getNodeScore() > maxScore){//����������max
					maxScore = top.getNodeScore() ;//����max
					
					queue.poll();//��ǰtopΪĿ��ڵ�v���ҵ�һ�����õ�maxScore
					continue;
				}
				else{
					queue.poll();//��ǰtopΪĿ��ڵ�v���õ��ķ�����������maxScore
					continue;
				}
			}
			
			if(top.getNodeScore() < maxScore){//��ǰtop�ķ�֧����֦
				queue.poll();
				continue;
			}
//			if (G.getVertexMap().get(top.getId()).getNeighbor().size() == 0) {
//				continue;
//			}
			for(Vertex i : G.getVertexMap().get(top.getId()).getNeighbor()){//�Ե�ǰtop���ھӱ���				
				if(i.getId() == top.getfId()){//��ǰ�������ھӽڵ��ǲ���top�ĸ��ڵ�
					continue;
				}
				
//				float weight = G.getVertexMap().get(top.getId()).getNeighborWeight().get(i.getId());
//				System.out.println("................." + weight);
				// IAB �ķ�ʽ����
//				float weight = getIAB(G, top, i);
				// Jaccard ϵ��
				float weight = getJaccard(G, top, i);
//				System.out.println(weight);
				// �Լ��޸�ע��
				float tmp = top.getNodeScore() * weight;//
				if( tmp <= maxScore ){
					queue.poll();//��ǰtop������·������֦
					continue;
				}
				BFSNode nb = new BFSNode();
				nb.setId(i.getId());
				nb.setNodeScore(tmp);
				nb.setfId(top.getId());//����ھ�����У����øýڵ��NodeScore�͸��ڵ�
				queue.add(nb);
			}
		}
		System.out.println(vq.getId()+"��"+v.getId()+"��maxScore��"+maxScore);
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
	
	public Graph influenceMaintain (Graph G, float p,Vertex q)//�Ժ�ѡͼ�е�Ӱ����ֵ���д���,p����ֵ,q�ǲ�ѯ�ڵ�
	{
		for(int i=0;i<G.getVertexIdList().size();i++)
		{
			//System.out.println("��ǰ�ڵ�   "+G.getVertexIdList().get(i).getId());
			if(mipCompute (G, q, G.getVertexIdList().get(i)) < p)
			{
				for(int j=0;j<G.getVertexIdList().get(i).getNeighbor().size();j++)//����Ҫɾ���ڵ�i���ھ�
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
	public List<Vertex> findDVertex(Graph G,Vertex vId,int d)//�Ҿ����ѯ����С��d�Ķ��㼯��
	{
		List<Vertex> dVertexList = new ArrayList<Vertex> ();//�洢����ı���
		BlockingQueue<Vertex> Q= new LinkedBlockingQueue<Vertex>();
		vId.setVisit(true);
		Q.add(vId);
		while(true)
		{
			if(Q.isEmpty())//����Ϊ�����˳�ѭ��
			{
				break;
			}
			if(Q.peek().getDist()>d)//����Ԫ�����ѯ�ڵ�ľ������d����Ӷ�����ɾ��
			{
				Q.poll();
				continue;
			}
			dVertexList.add(Q.peek());//�������������������������Ԫ�ؼ�����������
			Q.poll();//Ȼ����׳���
			for(int i=0;i<dVertexList.get(dVertexList.size()-1).getNeighbor().size();i++)
			{
				//�жϸոճ��ӵ�Ԫ�ص��ھӽڵ����Ƿ���ʹ�
				if(!(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).isVisit()))
				{
					//��������
					//���ոճ��ӵ�Ԫ�ص��ھӽڵ�����Ϊ�ѷ��ʣ���ӣ���������Ϊ+1
					dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).setVisit(true);
					Q.add(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i));
					dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).setDist(1+dVertexList.get(dVertexList.size()-1).getDist());
				}
				
			}
		}
		List<Vertex> d1 = new ArrayList<Vertex> ();
		for (int j=0;j<dVertexList.size();j++)//����
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
	
	public boolean isContains(List<Vertex> VertexList,int vId)//�ж�һ��list���Ƿ���IDΪvId�Ľڵ�
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
	public Graph inducedSubGraph (List<Vertex> VertexSet,Graph G)//������Vq������d�Ķ��㼯������ͼ
	{
		for (int i=0;i<VertexSet.size();i++)//�������i��j��ͼG������ͨ�ģ�����������ͨi��j
		{
			for (int j=i+1;j<VertexSet.size();j++)
			{
				//System.out.println("test:"+G.getVertexMap().get(VertexSet.get(i).getId()).getNeighbor().size());
				
				if(isContains(G.getVertexMap().get(VertexSet.get(i).getId()).getNeighbor(),VertexSet.get(j).getId()))//�����ԭʼͼG��i���ھ�����j
				{
					VertexSet.get(i).getNeighbor().add(VertexSet.get(j));
					VertexSet.get(j).getNeighbor().add(VertexSet.get(i));
					VertexSet.get(i).getNeighborWeight().put(VertexSet.get(j).getId(),G.getVertexMap().get(VertexSet.get(i).getId()).getNeighborWeight().get(VertexSet.get(j).getId()));
					VertexSet.get(j).getNeighborWeight().put(VertexSet.get(i).getId(),G.getVertexMap().get(VertexSet.get(j).getId()).getNeighborWeight().get(VertexSet.get(i).getId()));
				}
			}
		}
		//������ͼG0
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
	public int support (Vertex v1,Vertex v2)//��ߵ�֧�ֶ�
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
	
	public List<Vertex> distance (Graph G,Vertex v1,int d)//�����v1�������d�Ķ��㼯��
	{
		List<Vertex> dVertexList = new ArrayList<Vertex> ();//�洢����ı���
		BlockingQueue<Vertex> Q= new LinkedBlockingQueue<Vertex>();
		v1.setVisit(true);
		Q.add(v1);
		while(true)
		{
			if(Q.isEmpty())//����Ϊ�����˳�ѭ��
			{
				break;
			}
			if(Q.peek().getDist()<d||Q.peek().getDist()==d)//����Ԫ�����ѯ�ڵ�ľ������d����Ӷ�����ɾ��
			{
				Q.poll();
				continue;
			}
			dVertexList.add(Q.peek());//�������������������������Ԫ�ؼ�����������
			Q.poll();//Ȼ����׳���
			for(int i=0;i<dVertexList.get(dVertexList.size()-1).getNeighbor().size();i++)
			{
				//�жϸոճ��ӵ�Ԫ�ص��ھӽڵ����Ƿ���ʹ�
				if(!(dVertexList.get(dVertexList.size()-1).getNeighbor().get(i).isVisit()))
				{
					//��������
					//���ոճ��ӵ�Ԫ�ص��ھӽڵ�����Ϊ�ѷ��ʣ���ӣ���������Ϊ+1
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
	public void kdTrussMaintain (Graph G,Vertex vId,int k,int d)//��ͼ��֦Ϊ(k,d)-truss
	{
		while(true)
		{
			/*
			 * ɾ��֧�ֶ�С�ڣ�k-2���ı�
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
			 * ɾ�������ѯ�ڵ����d�ĵ�
			 */
			//System.out.println("G map size"+G.getVertexMap().size());
			//System.out.println("G list size"+G.getVertexIdList().size());
			List<Vertex> dset = distance(G,vId,d);//��������d�Ķ��㼯
			for (int k1=0;k1<dset.size();k1++)
			{
				for(int k2=0;k2<dset.get(k1).getNeighbor().size();k2++)
				{
					//ɾ�������
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
