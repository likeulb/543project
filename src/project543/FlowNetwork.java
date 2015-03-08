package project543;

import java.io.*;
import java.util.*;
import java.text.*;

/**
* A class that represents a FlowNetwork.
* A SimpleGraph object, a source vertex s，a sink vertex t and the value of the flow
* 
*/
public class FlowNetwork {
	
	private SimpleGraph G;
	private Vertex s;
	private Vertex t;
	private int flow;
	
	/**
     * Constructor for the Flow Network with input SimpleGraph. Set every Edge
     * with an array representing the flow capacity and actual flow in this Edge
     * @param SimpleGraph     the SimpleGraph from the input file
     * @param s               the source vertex
     * @param t               the sink vertex
     * @flow                  the value of the flow
     */
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
				//data[0] is the capacity, data[1] is flow of this edge
				}
		}
		
		
	}
	/**
     * Return the source vertex
     * @return Source vertex s
     */
	public Vertex getS(){
		return this.s;
	}
	
	/**
     * Return the sink vertex
     * @return Sink vertex t
     */
	public Vertex getT(){
		return this.t;
	}
	
	/**
     * Return the SimpleGraph
     * @return SimpleGraph
     */
	public SimpleGraph getGraph(){
		return this.G;
	}
	
	/**
     * Return the value of the flow
     * @return value of the flow
     */
	public int getFlow(){
		return flow;
	}
	
	/**
     * Return the maximum flow capacity of all edges
     * @return maximum flow capacity
     */
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
	
	
	
	/**
     * Initialize preflow and labeling. 
     * Set each vertex with an array representing value of excess and height.
     * Set s with height equal to the number of vertices in the graph, other vertices with 0 height.
     * Set the edges adjacent to s with flow equal to capacity.
     * Set the vertices adjacent to s with excess equal to the capacity of the edge from s 
     * Set current flow value by adding the capacity of edges coming out of s
     */
	public void InitialPreFlow(){
		
		int numVertices = G.numVertices();
		Iterator m;
		Edge e;
		Vertex v;
		for(m=G.vertices();m.hasNext();){
			int[] label = new int[2];
			v= (Vertex) m.next();
			if(v==s){
				label[1] = numVertices;
				v.setData(label);
				System.out.println("Initial height of s to be "+ label[1]);
			}
			else{
				label[1]=0;
				v.setData(label);
			}
			
		}
		for(m=G.incidentEdges(s);m.hasNext();){
			e = (Edge) m.next();
			int[] flows = (int[]) e.getData();
			flows[1]=flows[0];
			flow+=flows[0];
			Vertex second=e.getSecondEndpoint();
			int[] label2 = (int[]) second.getData();
			label2[0] = flows[0];
			System.out.println("Initial excess of "+ second.getName()+" adjacent to s to be "+ label2[0]);
		}
		System.out.println("******current flow is "+ flow);
	}
	
	/**
     * Return the vertex that has excess greater than 0
     * @return Vertex v with excess
     */
	public Vertex vertexWithExcess(){
		Iterator m;
		Vertex v;
		for(m=G.vertices();m.hasNext();){
			v= (Vertex) m.next();
			int[] label = (int[]) v.getData();
			if(v!=s&&v!=t&&label[0]>0){
					System.out.println("Vertex "+ v.getName()+ " has excess of "+ label[0]);
					return v;
			}
		}
		
		return null;
		
		
	}
	
	/**
     * Perform pushing regarding the input vertex for preflow algorithm
     * @param Vertex v
     * @return True if pushing works and the excess decreased to 0; False if cannot push more excess and excess is still greater than 0
     */
	
	public boolean push(Vertex v){
		int[] label = (int[]) v.getData();
		boolean updated = false;
		
		Iterator m=G.incidentEdges(v);
		while(m.hasNext()){
			Edge adjacent = (Edge) m.next();
			int[] flows = (int[]) adjacent.getData();
			Vertex second = adjacent.getSecondEndpoint();
			int[] label2 = (int[]) second.getData();
			
			if(second!=v&&label[1]>label2[1]&&flows[1]<flows[0]){
				int d = Math.min(label[0], flows[0]-flows[1]);
				flows[1]+=d;
				label[0]-=d;
				label2[0]+=d;
				System.out.println("flow from "+v.getName()+" to "+ second.getName()+ " increased to "+flows[1]+" and excess of "+ v.getName()+ " decreased to "+ label[0]);
				updated = true;
				
			}
			
			if(second==v){
				Vertex first = adjacent.getFirstEndpoint();
				int[] label1 = (int[]) first.getData();
				if(label[1]>label1[1]&&flows[1]>0){
					int d = Math.min(label[0], flows[1]);
					flows[1]-=d;
					label[0]-=d;
					label1[0]+=d;
					if(first==s){
						flow-=d;
						System.out.println("******current flow is "+ flow);
						//decrease the flow value as it is a backward edge to s
					}
					System.out.println("flow from "+first.getName()+" to "+ v.getName()+ " decreased to "+flows[1]+" and excess of "+ v.getName()+ " decreased to "+ label[0]);
					updated = true;
					
				}
			}
			
			if(label[0]==0) {
				
				break;
			}
			if(!m.hasNext()&&label[0]!=0) return false;
			//if no more adjacent edges to push but excess is still greater than 0
		}
		return updated;
	}
	
	/**
     * Increase the height of the vertex for preflow algorithm
     * @param Vertex v
     */
	public void labeling(Vertex v){
		int[] label = (int[]) v.getData();
		label[1]++;
		System.out.println("height of "+v.getName()+" is increased to "+ label[1]);
	}
	
	/**
     * Return the value of flow by adding up the flow coming out of s
     * @return value of flow
     */
	
	public int CaculateFlowValueFromS(){
		Iterator m;
		Edge e;
		int flowValue=0;
		for(m=G.incidentEdges(s);m.hasNext();){
			e = (Edge) m.next();
			int[] flows = (int[]) e.getData();
			flowValue+=flows[1];
		}
		return flowValue;
	}
	
	/**
     * DFS to find s-t path in Gf that can increase the flow value by input scale, used in Scaling Max flow algorithm
     * @param scale of the flow value that can be increased along the path
     * @return the linkedlist of vertices along s-t path
     */
	
	public LinkedList<Vertex> DFS(int scale){
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		s.setData("visited");
		path.add(s);
		
		while(!path.isEmpty()){
			Vertex cur = path.peekLast();
			
			Iterator m=G.incidentEdges(cur);
			while(m.hasNext()){
				Edge adjacent = (Edge) m.next();
				int[] flows = (int[]) adjacent.getData();
				Vertex second = adjacent.getSecondEndpoint();
				
				if(second.getData()==null&&flows[1]+scale<=flows[0]){
					second.setData("forward");
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
						path.add(first);
						System.out.println(first.getName()+" add to stack");
						break;
						}
					}
				if(!m.hasNext()){
					path.removeLast();
				}
			}
			
		}
		return path;
	}
	
	/**
     * DFS to find s-t path in Gf and return the value of flow that can be added along this path, used in F-F algorithm
     * @param linkedlist of vertices along the s-t path
     * @return the value of flow that can be added along this s-t path
     */
	public int DFSforF_F(LinkedList<Vertex> path){
		LinkedList<Integer> d_size = new LinkedList<Integer>();
		s.setData("visited");
		path.add(s);
		
		while(!path.isEmpty()){
			Vertex cur = path.peekLast();
		
			Iterator m=G.incidentEdges(cur);
			while(m.hasNext()){
				Edge adjacent = (Edge) m.next();
				int[] flows = (int[]) adjacent.getData();
				Vertex second = adjacent.getSecondEndpoint();
				if(second.getData()==null&&flows[1]<flows[0]){
					second.setData("forward");
					path.add(second);
					if(d_size.isEmpty())
						d_size.add(flows[0]-flows[1]);
					else{
						d_size.add(Math.min(d_size.getLast(),flows[0]-flows[1]));
					}
					System.out.println(second.getName()+" add to stack");
					if(second==t) {
						System.out.println("Vertex in the DFS path are: ");
						for(Vertex v:path){
							System.out.println(v.getName());
						}
						
						return d_size.getLast();
					}
						break;
					}
				else if(second==cur&&flows[1]>0){
					Vertex first = adjacent.getFirstEndpoint();
					if(first.getData()==null){
						first.setData("backward");
						path.add(first);
						if(d_size.isEmpty())
							d_size.add(flows[1]);
						else{
							d_size.add(Math.min(d_size.getLast(),flows[1]));
						}
						System.out.println(first.getName()+" add to stack");
						break;
						}
					}
				if(!m.hasNext()){
					path.removeLast();
					if(!d_size.isEmpty())
						d_size.removeLast();
					
				}
			}
			
		}
		
		return 0; //when there is no s-t path in the Gf
	}
	
	/**
     * Set all vertices to be unvisited
     */
	public void unVisit(){
		Iterator m=G.vertices();
		while(m.hasNext()){
			Vertex v = (Vertex) m.next();
			if(v.getData()!=null)
				v.setData(null);
		}
	}
	
	/**
     * Augmenting the flow network regarding the input s-t path and the value
     * @param Linkedlist of vertices along the s-t path
     * @param the value of flow that will be increased along the s-t path
     */
	public void Augment(LinkedList<Vertex> path, int val){
		for(int i=0;i<path.size()-1;i++){
			Vertex cur = path.get(i);
			Vertex next = path.get(i+1);
			Iterator m=G.incidentEdges(cur);
			while(m.hasNext()){
				Edge adjacent = (Edge) m.next();
				int[] curFlow = (int[]) adjacent.getData();
				if(next.getData().equals("forward")&&adjacent.getSecondEndpoint()==next){
					curFlow[1]+=val; //if it is a forward edge
					break;
				}
				else if(next.getData().equals("backward")&&adjacent.getFirstEndpoint()==next){
					curFlow[1]-=val;//if it is a backward edge
				}
				
			}
		}
		flow+=val; //increase the total flow of the flow network
	}

}
