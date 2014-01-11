package org.tuxdna.graphs;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.util.Random;

public class DynamicSimpleGraph {
	public static void main(String args[]) {
		final Graph graph = new SingleGraph("Tutorial 1");

		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

		Runnable r = new Runnable() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					String nodeName = "Node" + i;
					int otherNodeId = (int) (Random.next() * i);
					String targetNodeName = "Node" + otherNodeId;
					graph.addNode(nodeName);
					graph.addEdge("Edge" + i, nodeName, targetNodeName);

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};

		Thread t = new Thread(r);
		t.start();
		graph.display();
	}
}