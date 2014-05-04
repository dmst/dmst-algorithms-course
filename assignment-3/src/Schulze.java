import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Schulze {

	static String fileName = null;
		
	public static void main(String[] args) {
	
		// check for file name argument
		if(args.length==0) {
			System.out.println("No filename specified. Please specify a valid file!");
		} else {
			fileName = args[0];
		}
		
		try {
			String json = openFile(fileName);
			
			JSONObject myJSON = new JSONObject(json);
			JSONArray candidates = myJSON.getJSONArray("candidates");
			JSONArray ballots = myJSON.getJSONArray("ballots");
	
			int m = candidates.length();
			
			int[][] p = new int[m][m];
			
			step1(p, ballots, candidates);
			
			int[][] g = new int[m][m];
			
			step2(p, g);
			
			int[][] s = new int[m][m];
			int[][] pred = new int[m][m];
			
			step3(g, s, pred, m);
			
			ArrayList<ArrayList<Integer>> wins = new ArrayList<ArrayList<Integer>>();
			step4(s, wins, m);
			
			for(int i=0;i<m;i++) {
				System.out.print(candidates.get(i) + " = " + wins.get(i).size() + " [");
				for(int x=0;x<wins.get(i).size();x++) {
					System.out.print(candidates.get(wins.get(i).get(x)));
					if(x<wins.get(i).size()-1)
						System.out.print(",");
				}
				System.out.println("]");
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public static void printArr(int[][] arr) {
		for(int i=0;i<arr.length;i++) {
			//System.out.print(candidates.get(i) + " ");
			for(int j=0;j<arr.length;j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	public static int min(int a, int b) {
		if(a>b)
			return b;
		else
			return a;
	}
	
	public static void step4(int[][] s, ArrayList<ArrayList<Integer>> wins, int n) {
		for(int i=0;i<n;i++) {
			ArrayList<Integer> listi = new ArrayList<Integer>();
			wins.add(listi);
			for(int j=0;j<n;j++) {
				if(i!=j) {
					if(s[i][j] > s[j][i]) {
						listi.add(j);
					}
				}
			}
		}
		
	}
	
	public static void step3(int[][] w, int[][] s, int[][] pred, int n) {
		
		// reset minimum weights to be zero and NOT minus indefinite
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				if(w[i][j]==Integer.MIN_VALUE)
					w[i][j]=0;
		
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(w[i][j] > w[j][i]) {
					s[i][j] = w[i][j] - w[j][i];
					pred[i][j] = i;
				} else {
					s[i][j] = Integer.MIN_VALUE;
					pred[i][j] = -1;
				}
			}
		}
				
		for(int k=0;k<n;k++) {
			for(int i=0;i<n;i++) {
				if(i!=k) {
					for(int j=0;j<n;j++) {
						if(j!=i) {
							if(s[i][j] < min(s[i][k], s[k][j])) {
								s[i][j] = min(s[i][k], s[k][j]);
								pred[i][j] = pred[k][j];
							}
						}
					}
				}
			}
		}
		
	}
	
	public static void step2(int[][] p, int[][] g) {
		
		for(int i=0;i<g.length;i++)
			for(int j=0;j<g[i].length;j++)
				g[i][j]=Integer.MIN_VALUE;
		
		for(int i=0;i<p.length;i++) {
			for(int j=0;j<p.length;j++) {
				if(p[i][j]-p[j][i]>0)
					g[i][j]=p[i][j]-p[j][i];
			}
		}
		
	}
	
	public static void step1(int[][] p, JSONArray ballots, JSONArray candidates) {
		
		for(int i=0;i<candidates.length();i++)
			for(int j=0;i<candidates.length();i++)
				p[i][j]=0;
		
		for(int i=0;i<ballots.length();i++) {
			JSONArray b = ballots.getJSONArray(i);
			for(int j=0;j<b.length();j++) {
				int f = b.getInt(j);
				for(int k=j+1;k<b.length();k++) {
					int s = b.getInt(k);
					p[f][s] = p[f][s] + 1;
				}
			}
		}
		
	}
	
	public static String openFile(String fileName) throws FileNotFoundException, IOException {
		
		String line = "";
		String result = "";
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		while((line = reader.readLine()) != null) {
			result=result+line;
		}
		
		reader.close();
		
		return result;
	}
	
	
}
