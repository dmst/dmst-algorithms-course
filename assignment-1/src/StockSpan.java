import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;


public class StockSpan {

	public static void main(String[] args) {
				
		String method="";
		String fileName="";
		
		if(args.length>=2) {
			method=args[0];
			fileName=args[1];
		} else {
			System.out.println("Please provide correct parameters!");
			System.out.println("Usage to calculate with Simple method: StockSpan -n DJIA.csv");
			System.out.println("Usage to calculate with Stack method: StockSpan -s DJIA.csv");
			System.out.println("Usage to calculate 100 times both methods: StockSpan -b DJIA.csv");
		}
		
		try {
			ArrayList<Stock> quote = openFile(fileName);
			
			switch(method) {
				case "-n": calculateSimple(quote, true); break;
				case "-s": calculateStack(quote, true); break;
				case "-b": calculateBoth(quote); break;
				default: System.out.println("Invalid method parameter given!");break;
			
			}
		} catch(Exception ex) {
			System.out.println("Error occured:" + ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}
	
	private static void calculateSimple(ArrayList<Stock> quote, boolean showOutput) {
		
		if(quote!=null) {
		
			int k;
			boolean span_end;
			
			for(int i=0;i<quote.size();i++) {
				k=1;
				span_end = false;
				Stock currentDay = quote.get(i);
				while(i-k >= 0 && !span_end) {
					Stock previousDay = quote.get(i-k);
					if(previousDay.getStockPrice() <= currentDay.getStockPrice()) {
						k=k+1;
					} else {
						span_end = true;
					}
				}
				if(showOutput) {
					System.out.println(currentDay.getStockDate() + "," + k);
				}
			}
			
			
		}
	
	}
	
	private static void calculateStack(ArrayList<Stock> quote, boolean showOutput) {
		ArrayList<Stock> span = new ArrayList<Stock>();
		
		Stack<Integer> st = new Stack<Integer>();
		st.push(0);
		
		span.add(new Stock(quote.get(0).getStockDate(), 1));
		
		for(int i=1;i<quote.size();i++) {
			
			while(!st.empty() && quote.get(st.peek()).getStockPrice() <= quote.get(i).getStockPrice()) {
				st.pop();
			}
			
			if(st.empty()) {
				span.add(new Stock(quote.get(i).getStockDate(), i+1));
			} else {
				span.add(new Stock(quote.get(i).getStockDate(), i-st.peek()));
			}
			
			st.push(i);
		}
		
		if(showOutput) {
			for(int i=0;i<span.size();i++) {
				Stock stock = span.get(i);
				System.out.println(stock.getStockDate() + "," + stock.getStockSpan());
			}
		}
		
	}
	
	private static void calculateBoth(ArrayList<Stock> quote) {
		// We need to run both methods for 100 times and display the running time in milliseconds
		long simpleStart = System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			calculateSimple(quote, false);
		}
		long simpleStop = System.currentTimeMillis();
		System.out.println("Naive implementation took: " + (simpleStop - simpleStart) + " millis");
		
		long stackStart = System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			calculateStack(quote, false);
		}
		long stackStop = System.currentTimeMillis();
		System.out.println("Stack implementation took: " + (stackStop - stackStart) + " millis");
		
	}
	
	// This method opens the file and returns an arraylist of all stocks 
	private static ArrayList<Stock> openFile(String fileName) throws FileNotFoundException, IOException {
		// Check if file exists
		File f = new File(fileName);
		if(!f.exists()) {
			System.out.println("File not found: " + fileName);
			return null;
		}
		
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		String record = "";
		
		// Open file, read records sequentially and put them in the Stocks arraylist
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while((record = br.readLine())!=null) {
			String[] fields = record.split(",");
			if(!fields[0].toLowerCase().equals("date")) {
				Stock myStock = new Stock();
				myStock.setStockDate(fields[0]);
				myStock.setStockPrice(fields[1]);
				stocks.add(myStock);
			}
		}
		
		br.close();   // Close the file
		
		return stocks;
	}

}
