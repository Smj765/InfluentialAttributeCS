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
		for(int i=0;i<v.getAttr().size();i++)//�����ڵ�v������
		{
			System.out.println("v.getAttr().size():" + v.getAttr().size());
			for(int j=0;j<G.getVertexIdList().size();j++)//����ͼG�����нڵ�
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
	public float cutWeight(Graph G0,Graph G)//������G cut edges��Ȩ��
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
	
	public float communityWeight(Graph G0,Graph G)//������G��ȫ��Ȩ��(����cut edgeȨ��)
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
	
	public void PrecRec(int[] so,Graph G, Graph G0){//precision��recall��F1,GΪ��ʼ��ͼ��G0Ϊ�õ��Ľ��ͼ
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
		DecimalFormat df = new DecimalFormat("0.00");//��ʽ��С��
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

	
	public float function(Graph G0,Graph G,Vertex q)//������score����
	{
		float score = cutWeight(G0,G) / communityWeight(G0,G);
		return score+attrScore(G, q);//����ʵ������Ӱ����
//		return score;//����ʵ����Ӱ����
	}
	
	public float belDegree(Graph G,Graph H,Vertex v)//����һ������G�нڵ�v��belonging degree
	{
		float deg = 0;
		//��ڵ�v�������ڵı�Ȩ��
		float weightIncom =0;
		for(int i=0;i<G.getVertexMap().get(v.getId()).getNeighbor().size();i++)
		{
			if(H.getVertexMap().containsKey(G.getVertexMap().get(v.getId()).getNeighbor().get(i).getId()))
			{
				weightIncom += G.getVertexMap().get(v.getId()).getNeighborWeight().get(G.getVertexMap().get(v.getId()).getNeighbor().get(i).getId());
			}
		}
		//��ڵ�v��ԭʼͼ�е����б�Ȩ��
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
	
	public Graph greedyWeight(Graph H,Graph G,int k,int d,Vertex q)//̰�ĺ���,����HΪ����,����GΪԭʼͼ
	{
		float score = function(H,G,q);
		Graph H0 = new Graph();
		for(int a=0;a<H.getVertexIdList().size();a++)//��H0��VertexList����ֵ
		{
			Vertex v = new Vertex();
			v.setId(H.getVertexIdList().get(a).getId());
			for(int x=0;x<H.getVertexIdList().get(a).getAttr().size();x++)//�����԰��������½ڵ�
			{
				v.getAttr().add(H.getVertexIdList().get(a).getAttr().get(x));
			}
			for(int y=0;y<H.getVertexIdList().get(a).getNeighbor().size();y++)//���ھӰ��������½ڵ�
			{
				v.getNeighbor().add(H.getVertexIdList().get(a).getNeighbor().get(y));
			}
			for(Integer in : H.getVertexIdList().get(a).getNeighborWeight().keySet())//Ȩ�ظ�ֵ
			{
				v.getNeighborWeight().put(in, H.getVertexIdList().get(a).getNeighborWeight().get(in));
			}
			H0.getVertexIdList().add(v);
		}
		for (Integer in : H.getVertexMap().keySet()) 
		{
			//map.keySet()���ص�������key��ֵ
		    Vertex u = new Vertex();
			u.setId(in);
			for(int x=0;x<H.getVertexMap().get(in).getAttr().size();x++)//�����԰��������½ڵ�
			{
				u.getAttr().add(H.getVertexMap().get(in).getAttr().get(x));
			}
			for(int y=0;y<H.getVertexMap().get(in).getNeighbor().size();y++)//���ھӰ��������½ڵ�
			{
				u.getNeighbor().add(H.getVertexMap().get(in).getNeighbor().get(y));
			}
			for(Integer xy: H.getVertexMap().get(in).getNeighborWeight().keySet())//Ȩ�ظ�ֵ
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
				//System.out.println("if��");
				
				H0 = new Graph();
				for(int a=0;a<H.getVertexIdList().size();a++)//��H0��VertexList����ֵ
				{
					Vertex v = new Vertex();
					v.setId(H.getVertexIdList().get(a).getId());
					for(int x=0;x<H.getVertexIdList().get(a).getAttr().size();x++)//�����԰��������½ڵ�
					{
						v.getAttr().add(H.getVertexIdList().get(a).getAttr().get(x));
					}
					for(int y=0;y<H.getVertexIdList().get(a).getNeighbor().size();y++)//���ھӰ��������½ڵ�
					{
						v.getNeighbor().add(H.getVertexIdList().get(a).getNeighbor().get(y));
					}
					for(Integer in : H.getVertexIdList().get(a).getNeighborWeight().keySet())//Ȩ�ظ�ֵ
					{
						v.getNeighborWeight().put(in, H.getVertexIdList().get(a).getNeighborWeight().get(in));
					}
					H0.getVertexIdList().add(v);
				}
				for (Integer in : H.getVertexMap().keySet()) 
				{
					//map.keySet()���ص�������key��ֵ
				    Vertex u = new Vertex();
					u.setId(in);
					for(int x=0;x<H.getVertexMap().get(in).getAttr().size();x++)//�����԰��������½ڵ�
					{
						u.getAttr().add(H.getVertexMap().get(in).getAttr().get(x));
					}
					for(int y=0;y<H.getVertexMap().get(in).getNeighbor().size();y++)//���ھӰ��������½ڵ�
					{
						u.getNeighbor().add(H.getVertexMap().get(in).getNeighbor().get(y));
					}
					for(Integer xy: H.getVertexMap().get(in).getNeighborWeight().keySet())//Ȩ�ظ�ֵ
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
//						//System.out.print("ɾ�� ");
//						H0.getVertexIdList().remove(a);
//					}
//				}
//				for(Integer b: H0.getVertexMap().keySet())//Ȩ�ظ�ֵ
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
			//System.out.println("ɾ���ڵ�id"+deleV.getId());
			//ɾ��Belong Degree��С�Ľڵ�
			/*
			for(int j=0;j<H.getVertexMap().get(minBelongId).getNeighbor().size();j++)
			{
				H.getVertexMap().get(minBelongId).getNeighbor().get(j).getNeighbor().remove(H.getVertexMap().get(minBelongId));
			}
			H.getVertexMap().remove(minBelongId);
			H.getVertexIdList().remove(H.getVertexMap().get(minBelongId));
			H.setVertexSize(H.getVertexMap().size());
			//�޼�
			kdTrussMaintain (H,q,k,d);
			*/
			if(deleV.getId() == q.getId())
			{
				System.out.println("ɾ����ѯ�ڵ�");
				break;
			}
			
			/*
			 * ��ɾ��
			 */
			
			for(int k1=0;k1<deleV.getNeighbor().size();k1++)
			{
				if(isContains(deleV.getNeighbor().get(k1).getNeighbor(),deleV.getId()))
				{
					deleV.getNeighbor().get(k1).getNeighbor().remove(deleV);
				}
			}
			
			/*
			 * ɾ��
			 */
			H.getVertexMap().remove(deleV.getId());
			H.getVertexIdList().remove(deleV);
			H.setVertexSize(H.getVertexMap().size());
			//System.out.println("H�Ĵ�С��"+H.getVertexMap().size());
			//System.out.println("H0�Ĵ�С��"+H0.getVertexIdList().size());

			kdTrussMaintain (H,q,k,d);
		}
		return H0;
		
	}

}
