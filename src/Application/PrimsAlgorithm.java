package Application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.io.*;


import Graph.*;

public class PrimsAlgorithm {
	public static int count = 0;
	public static Graph<Vertex, Edge<Vertex>> gph;
	public static PrintWriter out = new PrintWriter(System.out);
	public static List<Vertex> vert = new LinkedList<Vertex>();
	public static List<Integer> edWeight = new LinkedList<Integer>();
	/**
	 * 
	 * FileToGraph liest den Spannbaum ein und speichert ihn in eienem Graphen
	 * @param args
	 */
	public static void main(String[] args){
		String filename = "./data/bsp_mst_1.txt";
//		String filename2 = "./data/bsp_mst_2.txt";
//		String filename3 = "./data/bsp_mst_3.txt";
		gph = GraphRead.FileToGraph(filename, false, false, true);
		out.println("--------------------------------------");
		out.println(gph);
		out.println("--------------------------------------");
		minimalEdge();
		out.println("--------------------------------------");
		out.println("Gewicht des Minimalen Spannbaumes: ");
		getTotalWeight();
		out.println("--------------------------------------");
		out.println("Minimaler Spannbaum: ");
		for(int i = 0; i < vert.size(); i++) {
			out.println(vert.get(i));
		}
		out.flush();
	}

	/**
	 * minW: HashMap um Knotenpunkte mit dem dazugehörigen gewicht zu speichern
	 * pred: HashMap um Vorgänger im Spannbaum zu Speichern
	 * PriorityQueue: Speichert die noch Unbesuchten Knoten
	 *  
	 * 
	 */
	public static void minimalEdge() {
		HashMap<Vertex, Integer> minW = new HashMap<>() {
			{
				for (Vertex v : gph.getVertices()) {
					put(v, Integer.MAX_VALUE);
				}
				put(getSmallestID(), 0);
			}
		};
		HashMap<Vertex, Vertex> pred = new HashMap<>() {
			{
				put(getSmallestID(), null);
			}
		};
		
		PriorityQueue<Vertex> unvisited = new PriorityQueue<Vertex>(gph.getNumberVertices(),
				(a, b) -> a.getId() - b.getId());
		unvisited.addAll(gph.getVertices());

		while (unvisited.size() != 0) {
			Vertex minV = unvisited.peek();
			for (Vertex vex : unvisited) {
				if (minW.get(vex) < minW.get(minV)) {
					minV = vex;
				}
			}
			vert.add(minV);
			unvisited.remove(minV);

			for (Vertex v : gph.getNeighbours(minV)) {
				if (unvisited.contains(v) && weight(minV, v) < minW.get(v)) {
					pred.put(v, minV);
					minW.put(v, weight(minV, v));
					
				}
			}
		}
		
//		out.println(minW.toString());
		edWeight.addAll(minW.values());
	}

	/**
	 * Bekommt die Kleinste ID und somit den StartKnoten
	 * @return StartKnoten
	 */
	public static Vertex getSmallestID() {
		Vertex ve = null;
		int i = Integer.MAX_VALUE;
		for (Vertex v : gph.getVertices()) {
			if (v.getId() < i) {
				ve = v;
				i = v.getId();
			}
		}
		return ve;
	}
	
	/**
	 * Errechnet zum Schluss das Gesammtgewicht des Minimlane  Spannbaumes
	 * 
	 */
	public static void getTotalWeight() {
		int a = 0;
		for (int i = 0; i < edWeight.size(); i++) {
			a += edWeight.get(i);
		}
		out.println(a);
	}

	/**
	 * 
	 * @param a Knoten a
	 * @param b Knoten b
	 * @return Gibt das Gewicht der Kante zwischen den beiden Knotenpunkten zurück
	 */
	public static Integer weight(Vertex a, Vertex b) {
		List<Edge<Vertex>> EdA = new LinkedList<Edge<Vertex>>();
		EdA.addAll(gph.getIncidentEdges(a));

		for (Edge<Vertex> e : EdA) {
			if (e.getVertexA() == a && e.getVertexB() == b || e.getVertexA() == b && e.getVertexB() == a) {
				int w = e.getWeight();
				return w;
			} else {
			}
		}
		return -1;
	}

}
