package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.Label;

public class StockSheetUpdater {
    
	private File salesReport;
	private File stockSheet;
	private Label errorLabel;
	
	public StockSheetUpdater(File salesReport, File stockSheet, Label errorLabel) {
		this.salesReport = salesReport;
		this.stockSheet = stockSheet;
		this.errorLabel = errorLabel;
	}
	
	public void update() {
		try { // Open both sheets
			FileInputStream reportFile = new FileInputStream(this.salesReport);
			XSSFWorkbook reportBook = new XSSFWorkbook(reportFile);
			XSSFSheet reportSheet = reportBook.getSheetAt(2);
			
			FileInputStream stockFile = new FileInputStream(this.stockSheet);
			XSSFWorkbook stockBook = new XSSFWorkbook(stockFile);
			XSSFSheet stockSheet = stockBook.getSheetAt(0);
			
			try { // Perform update
				updateEbookProfitsWithReport(reportSheet, stockSheet);
			} catch (NullPointerException np) {
				System.out.println("End of report sheet");
				stockBook.setForceFormulaRecalculation(true); // (set.. on next open)
				stockFile.close(); // Close files
				reportFile.close();
				try { 
					// Save changes, close workbooks
					FileOutputStream outputFile = new FileOutputStream(this.stockSheet);
					stockBook.write(outputFile);	
					outputFile.close();
					stockBook.close();
					reportBook.close();
					errorLabel.setText("Done!");
				} catch (FileNotFoundException fnf) {
					errorLabel.setText("Cannot write to file, make sure neither spreadsheet "
							+ "is already open");
				}
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateEbookProfitsWithReport(XSSFSheet reportSheet, XSSFSheet stockSheet) {
		Cell cell = reportSheet.getRow(0).getCell(0);
		
		// For a single row in the report sheet:
		int reportRowNum = 1;
		while ( !(cell == null) ) {
			try { // Populate needed values from report sheet:
				cell = reportSheet.getRow(reportRowNum).getCell(2);
			} catch (NullPointerException e) {
				System.out.println("Null pointer while getting cell");
			}
			String asin = cell.toString();
			double netUnitsSold = getDoubleFromCell(reportSheet.getRow(reportRowNum).getCell(8));
			String royaltyTemp = reportSheet.getRow(reportRowNum).getCell(4).toString();
			double amazonRoyalty = (Double.parseDouble(royaltyTemp.replaceAll("%", ""))) / 100;
			
			// Populate needed values from stock sheet:
			int stockRowNum = findRowNumber(stockSheet, asin);
			try {
				if (!(stockRowNum == 0)) {
				    double listPrice = getDoubleFromCell(stockSheet.getRow(stockRowNum).getCell(26));
				    double authorRoyalty = getDoubleFromCell(stockSheet.getRow(stockRowNum).getCell(27));
				    
				    // Calculate profit, round to 2dp, set as double:
				    BigDecimal BDprofit = new BigDecimal(
				    		((netUnitsSold * listPrice) * amazonRoyalty) * authorRoyalty);
				    BDprofit = BDprofit.setScale(2, BigDecimal.ROUND_HALF_UP);
				    double profit = BDprofit.doubleValue();
				    
				    // Add to eBook Profit column:
				    Cell profitCell = stockSheet.getRow(stockRowNum).getCell(16);
				    profit = profit + profitCell.getNumericCellValue();
				    profitCell.setCellValue(profit);
				} else {
					errorLabel.setText("Error: eBook not found in stock sheet, ASIN: " + asin);
					throw new RuntimeException();
				}
			} catch (NumberFormatException e) {
				errorLabel.setText("Error: Incorrect or missing eBook List Price or Author Royalty cell");
				throw new RuntimeException();
			}
			reportRowNum++;
		}
	}
	
	private static int findRowNumber(XSSFSheet sheet, String asin) {
		// Returns row number of row that given ASIN is in
	    for (Row row : sheet) {
	        for (Cell cell : row) {
	            if (cell.getCellTypeEnum() == CellType.STRING) {
	                if (cell.getRichStringCellValue().getString().trim().equals(asin)) {
	                    return row.getRowNum();  
	                }
	            }
	        }
	    }               
	    return 0;
	}
	
	public double getDoubleFromCell(XSSFCell cell) {
		return Double.parseDouble(cell.toString());
	}}