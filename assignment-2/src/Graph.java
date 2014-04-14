import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Collections;

class Node {
	public String start;
	public String end;
	public int weight;
}

class Vertex implements Comparable<Vertex> {
	public final String name;
	public ArrayList<Edge> adjacencies = new ArrayList<Edge>();
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex previous;

	public Vertex(String argName) {
		name = argName;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Vertex other) {
		return Double.compare(minDistance, other.minDistance);
	}
}

class Edge {
	public final Vertex target;
	public final double weight;

	public Edge(Vertex argTarget, double argWeight) {
		target = argTarget;
		weight = argWeight;
	}
}

public class Graph {

	static boolean nodirectional = false;
	
	public static void main(String[] args) {

		// ALL: If -u option specified, graph is no-directional

		// Show result of Dijkstra algorithm with starting node "start".
		// java Graph [-u] -s start example_graph.txt

		// Show result of calculation for all pairs
		// java Graph [-u] -a example_graph.txt

		// Displays the diameter of the graph
		// java Graph [-u] -d example_graph.txt

		
		String method = "";
		String fileName = "";
		String startNode = "";

		// Read all parameters and populate the correct variables
		for (int i = 0; i < args.length; i++) {

			if (args[i].toLowerCase().equals("-u")) {
				nodirectional = true;
			}

			if (args[i].toLowerCase().equals("-s")) {
				method = args[i].toLowerCase();
				try {
					startNode = args[i + 1];
				} catch (ArrayIndexOutOfBoundsException ex2) {
					System.out.println("You did not entered a starting node after -s parameter!");
					return;
				}
			}

			if (args[i].toLowerCase().equals("-a")) {
				method = args[i].toLowerCase();
			}

			if (args[i].toLowerCase().equals("-d")) {
				method = args[i].toLowerCase();
			}

			if (args[i].toLowerCase().contains(".txt")) {
				fileName = args[i].toLowerCase();
			}

		}

		// Check that all variables have been populated
		if (!method.equals("-s") && !method.equals("-a")
				&& !method.equals("-d")) {
			System.out.println("Invalid method specified. Valid parameters are -s or -a or -d (and -u is optional)");
			return;
		}

		if (fileName.equals("")) {
			System.out.println("No input filename specified. Please give a valid filename!");
			return;
		}

		try {
			ArrayList<Vertex> graph = openFile(fileName);

			if (method.equals("-s")) {
				shortestPath(graph, startNode);
			}

			if (method.equals("-a")) {
				allPairs2(graph);
			}
			
		} catch (Exception ex) {
			System.out.println("Error occured:" + ex.getMessage());
			ex.printStackTrace();
		}

	}

	private static void shortestPath(ArrayList<Vertex> graph, String startNode) {
		
		Vertex startV = getVertex(graph, startNode);
		computePaths(startV);

		String resultPred = "Predecessor matrix\n[";
		String resultDist = "Distance matrix\n[";
		
		for (Vertex v : graph)
		{
			if(v.previous==null) {
		    	resultPred = resultPred + "-1, ";
		    } else {
		    	resultPred = resultPred + v.previous.name + ", ";
		    }
			
		    resultDist = resultDist + v.minDistance + ", ";
		}
		
		resultPred = resultPred.substring(0, resultPred.length()-2) + "]";
		resultDist = resultDist.substring(0, resultDist.length()-2) + "]";
		
		System.out.println(resultPred);
		System.out.println(resultDist);
		
	}
	
	private static void allPairs2(ArrayList<Vertex> graph) {
				
//		for(Vertex v : graph) {
//			shortestPath(graph, v.name);
//		}
		
		
		//Vertex startV = getVertex(graph, startNode);
		
		String resultPred = "Predecessor matrix\n";
		String resultDist = "Distance matrix\n";
		
		for (Vertex v1 : graph) {
			computePaths(v1);
			
			resultPred = resultPred + "[";
			resultDist = resultDist + "[";
			
			for (Vertex v : graph)
			{
				//computePaths(v);
				
				if(v.previous==null) {
			    	resultPred = resultPred + "-1, ";
			    } else {
			    	resultPred = resultPred + v.previous.name + ", ";
			    }
				
			    resultDist = resultDist + v.minDistance + ", ";
			    
			    
			}
			
			resultPred = resultPred.substring(0, resultPred.length()-2) + "]\n";
			resultDist = resultDist.substring(0, resultDist.length()-2) + "]\n";
		
		}
				
		System.out.println(resultPred);
		System.out.println(resultDist);

		
	}
	
	private static void allPairs(ArrayList<Vertex> graph) {
		
		computePaths(graph.get(0));
		
		String resultPred = "Predecessor matrix\n";
		String resultDist = "Distance matrix\n";
				
		for(Vertex v : graph) {
			
			resultPred = resultPred + "[";
			resultDist = resultDist + "[";
			
			List<Vertex> path = getShortestPathTo(v);
			for(Vertex c : path) {
			//	resultPred = resultPred + "from: " + v.name + " to: " + c.name + " ";
				
				if(c.previous==null) {
			    	resultPred = resultPred + "-1, ";
			    } else {
			    	resultPred = resultPred + c.previous.name + ", ";
			    }
				
			//	resultDist = resultDist + "from: " + v.name + " to: " + c.name + " ";
			    resultDist = resultDist + c.minDistance + ", ";
			}
			
			resultPred = resultPred.substring(0, resultPred.length()-2) + "]\n";
			resultDist = resultDist.substring(0, resultDist.length()-2) + "]\n";
		}
		
		System.out.println(resultPred);
		System.out.println(resultDist);
		
	}

	// This method opens the file and returns an arraylist of all stocks
	private static ArrayList<Vertex> openFile(String fileName)
			throws FileNotFoundException, IOException, NumberFormatException {
		// Check if file exists
		File f = new File(fileName);
		if (!f.exists()) {
			System.out.println("File not found: " + fileName);
			return null;
		}

		ArrayList<Node> nodes = new ArrayList<Node>();
		String record = "";

		// Open file, read records sequentially and put them in the Stocks
		// arraylist
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while ((record = br.readLine()) != null) {
			String[] fields = record.split(" ");

			
			Node newNode = new Node();
			newNode.start = fields[0];
			newNode.end = fields[1];

			// Node weight is optinal in file, so we need to check first
			if (fields.length > 2) {
				newNode.weight = Integer.parseInt(fields[2]);
			} else {
				newNode.weight = 1; // If no weight in file, defaults to 1
			}

			nodes.add(newNode);
			
			if(nodirectional) {
				Node noDir = new Node();
				
				noDir.start = fields[1];
				noDir.end = fields[0];
				
				// Node weight is optinal in file, so we need to check first
				if (fields.length > 2) {
					noDir.weight = Integer.parseInt(fields[2]);
				} else {
					noDir.weight = 1; // If no weight in file, defaults to 1
				}
				
				nodes.add(noDir);
			}

		}

		br.close(); // Close the file

		ArrayList<Vertex> graph = new ArrayList<Vertex>();
		for(Node n : nodes) {
			Vertex startV = getVertex(graph, n.start);
			Vertex endV = getVertex(graph, n.end);
			
			startV.adjacencies.add(new Edge(endV, n.weight));
			
		}
		
		
		return graph;
	}
	
	public static Vertex getVertex(ArrayList<Vertex> graph, String vertexName) {
		
		Vertex v = null;
		
		for (Vertex searchVertex : graph) {
			if(searchVertex.name.equals(vertexName)) {
				v = searchVertex;
				return v;
			}
		}
		
		v = new Vertex(vertexName);
		graph.add(v);
		
		return v;
	}

	public static void computePaths(Vertex source) {
		source.minDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	public static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);
		Collections.reverse(path);
		return path;
	}

}
