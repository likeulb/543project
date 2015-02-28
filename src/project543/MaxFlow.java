package project543;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MaxFlow {
	private FlowNetwork f;
	
	public MaxFlow(FlowNetwork f){
		this.f=f;
	}
	
	public void F_F(){
		Iterator m;
		Edge e;
		ArrayList<Vertex> path = new ArrayList<Vertex>();
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
	
	public void Scaling(int maxCapacity){
		int n = (int)(Math.log(maxCapacity)/Math.log(2));
		int delta = (int)Math.pow(2, n);
		Iterator m;
		Edge e;
		while(delta>0){
			ArrayList<Vertex> path = f.DFS(delta);
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
}
