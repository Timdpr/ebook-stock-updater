package application;

import java.io.File;

public class StockSheetUpdater {
    
	private File salesReport;
	private File stockSheet;
	
	public StockSheetUpdater(File salesReport, File stockSheet) {
		this.salesReport = salesReport;
		this.stockSheet = stockSheet;
	}
	
	public void update() {
		System.out.println(salesReport.exists());
		System.out.println(stockSheet.exists());
	}
}
