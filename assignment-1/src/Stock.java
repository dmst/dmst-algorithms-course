
public class Stock {

	private String stockDate;
	private double stockPrice;
	private int stockSpan;
	
	public Stock() {
		
	}
	
	public Stock(String stockDate, int stockSpan) {
		this.stockDate = stockDate;
		this.stockSpan = stockSpan;
	}

	public String getStockDate() {
		return stockDate;
	}
	
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
	
	public double getStockPrice() {
		return stockPrice;
	}
	
	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}
	
	public void setStockPrice(String stockPrice) throws NumberFormatException {
		this.stockPrice = Double.parseDouble(stockPrice);
	}

	public int getStockSpan() {
		return stockSpan;
	}

	public void setStockSpan(int stockSpan) {
		this.stockSpan = stockSpan;
	}
	
	public String toString() {
		return this.getStockDate() + "," + this.getStockPrice() + "," + this.getStockSpan();
	}
	
}
