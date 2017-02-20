package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StockSheetUpdater {
    
	private File salesReport;
	private File stockSheet;
	private double exchangeRate;
	
	public StockSheetUpdater(File salesReport, File stockSheet, double exchangeRate) {
		this.salesReport = salesReport;
		this.stockSheet = stockSheet;
		this.exchangeRate = exchangeRate;
	}
	
	public void update() {
		try {
			// Open both sheets, perform update:
			FileInputStream reportFile = new FileInputStream(this.salesReport);
			XSSFWorkbook reportBook = new XSSFWorkbook(reportFile);
			XSSFSheet reportSheet = reportBook.getSheetAt(2);
			
			FileInputStream stockFile = new FileInputStream(this.stockSheet);
			XSSFWorkbook stockBook = new XSSFWorkbook(stockFile);
			XSSFSheet stockSheet = stockBook.getSheetAt(0);
			
			updateEbookProfitsWithReport(reportSheet, stockSheet);
			
			stockBook.setForceFormulaRecalculation(true); // (set.. on next open)
			stockFile.close(); // Close files
			reportFile.close();
			
			FileOutputStream outputFile = new FileOutputStream(this.stockSheet);
			stockBook.write(outputFile); // Save changes	
			outputFile.close();
			stockBook.close(); // Close workbooks
			reportBook.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateEbookProfitsWithReport(XSSFSheet reportSheet, XSSFSheet stockSheet) {
		int rowNum = 1;
		Cell cell = reportSheet.getRow(0).getCell(0);
		while ( !(cell == null) ) {
			// Populate needed values; calculate
			try {
				cell = reportSheet.getRow(rowNum).getCell(2);
			} catch (NullPointerException e) {
				System.out.println("Null pointer while getting cell");
			}
			String asin = cell.toString();
			double listPrice = getDoubleFromCell(reportSheet.getRow(rowNum).getCell(9));
			double netUnitsSold = getDoubleFromCell(reportSheet.getRow(rowNum).getCell(8));
			String royaltyTemp = reportSheet.getRow(rowNum).getCell(4).toString();
			double amazonRoyalty = (Double.parseDouble(royaltyTemp.replaceAll("%", ""))) / 100;
			
			/* Test:
			 * System.out.println("Row number = " + rowNum);
			 * System.out.println("asin = " + asin);
			 * System.out.println("listPrice = " + listPrice);
			 * System.out.println("netUnitsSold = " + netUnitsSold);
			 * System.out.println("royaltyTemp = " + royaltyTemp);
			 * System.out.println("amazonRoyalty = " + amazonRoyalty);
			 * System.out.println("this.exchangeRate = " + this.exchangeRate);
			 */
			
			double profit = (netUnitsSold * (listPrice * this.exchangeRate)) * amazonRoyalty;
			System.out.println(profit);
			rowNum++;
		}
		System.out.println("Out of loop");
	}
	
	public double getDoubleFromCell(XSSFCell cell) {
		return Double.parseDouble(cell.toString());
	}
}
