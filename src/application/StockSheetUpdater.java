package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
			FileInputStream reportFile = new FileInputStream(this.salesReport);
			XSSFWorkbook reportBook = new XSSFWorkbook(reportFile);
			XSSFSheet reportSheet = reportBook.getSheetAt(2);
			
			FileInputStream stockFile = new FileInputStream(this.stockSheet);
			XSSFWorkbook stockBook = new XSSFWorkbook(stockFile);
			XSSFSheet stockSheet = stockBook.getSheetAt(0);
			
			updateEbookProfitsWithReport(reportSheet, stockSheet);
			
			stockBook.setForceFormulaRecalculation(true);
			stockFile.close();
			reportFile.close();
			
			FileOutputStream outputFile = new FileOutputStream(this.stockSheet);
			stockBook.write(outputFile);
			outputFile.close();
			stockBook.close();
			reportBook.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateEbookProfitsWithReport(XSSFSheet reportSheet, XSSFSheet stockSheet) {
		int rowNum = 1;
		Cell cell = reportSheet.getRow(rowNum).getCell(2);
		while ( !(cell.toString() == "") ) {
			String asin = cell.toString();
			double listPrice = getDoubleFromCell(reportSheet.getRow(rowNum).getCell(9));
			double netUnitsSold = getDoubleFromCell(reportSheet.getRow(rowNum).getCell(8));
		}
	}
	
	public double getDoubleFromCell(XSSFCell cell) {
		return Double.parseDouble(cell.toString());
	}
}
