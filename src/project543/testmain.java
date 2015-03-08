package project543;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

public class testmain {
	
	public static void main(String[] args){
		
		SimpleGraph g1=new SimpleGraph();
		Hashtable table = GraphInput.LoadSimpleGraph(g1);
		Vertex s = (Vertex)table.get("s");
		Vertex t = (Vertex)table.get("t");
		
		FlowNetwork G1 = new FlowNetwork(g1,s,t);
		
		MaxFlow test1 = new MaxFlow(G1);
		
		long timestart = System.currentTimeMillis();
		
		//test1.F_F();
		//test1.Scaling(G1.getMaxCapacity());
		test1.preFlow();
		
		System.out.println("............");
		System.out.println(System.currentTimeMillis()-timestart+" ms");
		
		
	}

}
