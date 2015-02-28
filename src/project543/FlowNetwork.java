package project543;

import java.io.*;
import java.util.*;
import java.text.*;

public class FlowNetwork {
	
	private SimpleGraph G;
	private Vertex s;
	private Vertex t;
	private int flow;
	//private Hashtable table;
	
	public FlowNetwork(SimpleGraph G, Vertex s, Vertex t){
		this.G=G;
		this.s=s;
		this.t=t;
		flow = 0;
		
		Iterator m;
		Edge e;
		int cur;
		for(m=G.edges();m.hasNext();){
			e=(Edge) m.next();
			if(e.getData() instanceof Double){
				cur = (int) Math.floor((double) e.getData());
				int[] data = {cur,0};
				e.setData(data);
				//initialize the data of each edge to be an array
				//data[0] is the capacity, data[1] is flow of this edge
				}
		}
	}
	
	public Vertex getS(){
		return this.s;
	}
	
	public Vertex getT(){
		return this.t;
	}
	
	public SimpleGraph getGraph(){
		return this.G;
	}
	
	public int getFlow(){
		return flow;
	}
	
	//return the max capacity of all edges
	public int getMaxCapacity(){
		Iterator m;
		Edge e;
		int max=1;
		for(m=G.edges();m.hasNext();){
			e=(Edge) m.next();
			int[] capacity = (int[]) e.getData();
			max = Math.max(max, capacity[0]);
		}
		return max;
	}
	
	//DFS finding the path that can add flow with value scale, using in the scaling algorithm
	public ArrayList<Vertex> DFS(int scale){
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		//Stack<Vertex> stak = new Stack<Vertex>();
		s.setData("visited");
		//stak.add(s);
		path.add(s);
		
		//while(!stak.isEmpty()){
		while(!path.isEmpty()){
			//Vertex cur = stak.peek();
			Vertex cur = path.get(path.size()-1);
			Iterator m=G.incidentEdges(cur);
			while(m.hasNext()){
				Edge adjacent = (Edge) m.next();
				int[] flows = (int[]) adjacent.getData();
				Vertex second = adjacent.getSecondEndpoint();
				if(second.getData()==null&&flows[1]+scale<=flows[0]){
					second.setData("forward");
					//stak.add(second);
					path.add(second);
					System.out.println(second.getName()+" add to stack");
					if(second==t) {
						System.out.println("Vertex in the DFS path are: ");
						for(Vertex v:path){
							System.out.println(v.getName());
						}
						return path;
					}
						break;
					}
				else if(second==cur&&scale<=flows[1]){
					Vertex first = adjacent.getFirstEndpoint();
					if(first.getData()==null){
						first.setData("backward");
						//stak.add(first);
						path.add(first);
						System.out.println(first.getName()+" add to stack");
						break;
						}
					}
				if(!m.hasNext()){
					//stak.pop();
					path.remove(path.size()-1);
				}
			}
			
		}
		return path;
	}
	
	//this DFS is similar as the former one and is used for F_F with the returned value of d (the min value that can
	//be increased in the residual graph)
	
	public int DFSforF_F(ArrayList<Vertex> path){
		int d = Integer.MAX_VALUE;
		//HashMap<Integer, ArrayList<Vertex>> result = new HashMap<Integer, ArrayList<Vertex>>();
		//Stack<Vertex> stak = new Stack<Vertex>();
		s.setData("visited");
		//stak.add(s);
		path.add(s);
		
		//while(!stak.isEmpty()){
		while(!path.isEmpty()){
			//Vertex cur = stak.peek();
			Vertex cur = path.get(path.size()-1);
			Iterator m=G.incidentEdges(cur);
			while(m.hasNext()){
				Edge adjacent = (Edge) m.next();
				int[] flows = (int[]) adjacent.getData();
				Vertex second = adjacent.getSecondEndpoint();
				if(second.getData()==null&&flows[1]<flows[0]){
					second.setData("forward");
					//stak.add(second);
					path.add(second);
					d=Math.min(d, flows[0]-flows[1]);
					System.out.println(second.getName()+" add to stack");
					if(second==t) {
						System.out.println("Vertex in the DFS path are: ");
						for(Vertex v:path){
							System.out.println(v.getName());
						}
						
						return d;
					}
						break;
					}
				else if(second==cur&&flows[1]>0){
					Vertex first = adjacent.getFirstEndpoint();
					if(first.getData()==null){
						first.setData("backward");
						//stak.add(first);
						path.add(first);
						d=Math.min(d, flows[1]);
						System.out.println(first.getName()+" add to stack");
						break;
						}
					}
				if(!m.hasNext()){
					//stak.pop();
					path.remove(path.size()-1);
				}
			}
			
		}
		
		return d;
	}
	
	//set all the Data of vertex to null
	public void unVisit(){
		Iterator m=G.vertices();
		while(m.hasNext()){
			Vertex v = (Vertex) m.next();
			if(v.getData()!=null)
				v.setData(null);
		}
	}
	
//	public ArrayList<Vertex> BFS(){
//		
//		ArrayList<Vertex> path = new ArrayList<Vertex>();
//		LinkedList<Vertex> que = new LinkedList<Vertex>();
//		que.add(s);
//		
//		while(!que.isEmpty()){
//			Vertex cur = que.poll();
//			if(cur.getData()!=null) continue;
//			cur.setData("visited");
//			path.add(cur);
//			
//			System.out.println(cur.getName()+" current vertex");
//			Iterator m=G.incidentEdges(cur);
//			while(m.hasNext()){
//				Edge adjacent = (Edge) m.next();
//				int[] flows = (int[]) adjacent.getData();
//				if(flows[1]<flows[0]){//if the forward flow is less than capacity
//					Vertex second = adjacent.getSecondEndpoint();
//					if(second.getData()==null) {
//						que.add(second);//if the second vertex has not been visited
//						System.out.println(second.getName()+" second vertex with forward edge");
//					}
//					if(second==t) {
//						System.out.println(second==t);
//						break;
//					}
//				}
//				if(flows[2]>0){
//					Vertex first = adjacent.getFirstEndpoint();
//					if(first.getData()==null) {
//						que.add(first);//if the first vertex has not been visited
//						System.out.println(first.getName()+" first vertex with backward edge");
//					}
//					
//				}
//				
//			}
//
//			if(!que.isEmpty()&&que.getLast()==t){
//				path.add(t);
//				for(Vertex v:path){
//					v.setData(null);
//				}
//				break;
//			}
//		}
//		
//		return path;
//		
//	}
	
	
	//given the path, increase the flow along the path by val
	public void Augment(ArrayList<Vertex> path, int val){
		for(int i=0;i<path.size()-1;i++){
			Vertex cur = path.get(i);
			Vertex next = path.get(i+1);
			Iterator m=G.incidentEdges(cur);
			while(m.hasNext()){
				Edge adjacent = (Edge) m.next();
				int[] curFlow = (int[]) adjacent.getData();
				if(next.getData().equals("forward")&&adjacent.getSecondEndpoint()==next){
					curFlow[1]+=val; //if it is a forward edge
					//curFlow[2]=curFlow[1];
					break;
				}
				else if(next.getData().equals("backward")&&adjacent.getFirstEndpoint()==next){
					curFlow[1]-=val;//if it is a backward edge
					//curFlow[2]=curFlow[1];
				}
				
			}
		}
		flow+=val; //increase the total flow of the flow network
	}

}
