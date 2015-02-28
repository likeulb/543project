package project543;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class testmain {
	
	public static void main(String[] args){
		
		SimpleGraph g=new SimpleGraph();
		Hashtable table = GraphInput.LoadSimpleGraph(g);
		Vertex s = (Vertex)table.get("s");
		Vertex t = (Vertex)table.get("t");
		FlowNetwork G = new FlowNetwork(g,s,t);
		
		
		
		SimpleGraph h=G.getGraph();
		Iterator m;
		Edge e;
		int cur;
		for(m=h.edges();m.hasNext();){
			e=(Edge) m.next();
			int[] test = (int[]) e.getData();
		System.out.println(test[0]+" and "+test[1]);
		}
		
		MaxFlow test = new MaxFlow(G);
		test.F_F();
		System.out.println("............");
		//test.Scaling(G.getMaxCapacity());
		
		
		/*ArrayList<Vertex> vs = G.DFS(1);
		
		while(vs.size()>1){
			G.Augment(vs, 1);
			for(m=h.edges();m.hasNext();){
				e=(Edge) m.next();
				int[] test = (int[]) e.getData();
			System.out.println(test[0]+" and "+test[1]);
			}
			System.out.println("-------");
			G.unVisit();
			vs = G.DFS(1);
			System.out.println("-------");
			for(Vertex a:vs){
				System.out.println(a.getName());
			}
		}
		
		System.out.println(G.getFlow());*/
		
	}

}