package project543;

import java.util.*;


/**
* A class to find the maximum flow of a flow network using F-F, scaling max flow, and preflow algorithm
*/
public class MaxFlow {
	private FlowNetwork f;
	
	/**
     * Constructor
     * @param flow network f
     */
	public MaxFlow(FlowNetwork f){
		this.f=f;
	}
	
	/**
     * F-F algorithm to find the maximum flow
     */
	public void F_F(){
		Iterator m;
		Edge e;
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		int d = f.DFSforF_F(path);
		while(path.size()>1){
			f.Augment(path, d);
			for(m=f.getGraph().edges();m.hasNext();){
				e=(Edge) m.next();
				int[] test = (int[]) e.getData();
			System.out.println(test[0]+" and "+test[1]);
			}
			System.out.println("-------");
			f.unVisit();
			path.clear();
			d = f.DFSforF_F(path);
			System.out.println("-------");
		}
		System.out.println("The value of the flow: "+ f.getFlow());
	}
	
	/**
     * Scaling max flow algorithm
     */
	public void Scaling(int maxCapacity){
		int n = (int)(Math.log(maxCapacity)/Math.log(2));
		int delta = (int)Math.pow(2, n);
		Iterator m;
		Edge e;
		while(delta>0){
			LinkedList<Vertex> path = f.DFS(delta);
			while(path.size()>1){
				f.Augment(path, delta);
				for(m=f.getGraph().edges();m.hasNext();){
					e=(Edge) m.next();
					int[] test = (int[]) e.getData();
				System.out.println(test[0]+" and "+test[1]);
				}
				System.out.println("-------");
				f.unVisit();
				path = f.DFS(delta);
				System.out.println("-------");
			}
			f.unVisit();
			delta/=2;
		}
		System.out.println("The value of the flow: "+ f.getFlow());
	}
	
	/**
     *Preflow algorithm to find the maximum flow
     */
	public void preFlow(){
		f.InitialPreFlow();
		Vertex v = f.vertexWithExcess();
		int max = f.getGraph().numVertices();
		while(v!=null){
			while(!f.push(v)){
				int[] label = (int[]) v.getData();
				if(label[1]==2*max+1) //the maximum label is 2*|V|+1
					break;
				f.labeling(v);
			}
			v = f.vertexWithExcess();
		}
		System.out.println("The value of the flow: "+ f.getFlow());
		
	}
}
