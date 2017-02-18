package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	@FXML
	private Button getSalesReport;
	@FXML
	private Button getStockSheet;
	@FXML
	private Label errorLabel;
	@FXML
	private TextField exchangeField;
	
	private File salesReport;
	private File stockSheet;
	private double exchangeRate;
	
	@FXML protected void update(ActionEvent event) {
		try {
			StockSheetUpdater updater = new StockSheetUpdater(salesReport, stockSheet, exchangeRate);
			updater.update();
		} catch (NullPointerException e) {
			errorLabel.setText("Error: Make sure both spreadsheets are selected");
			e.printStackTrace();
		}
	}
	
	@FXML protected void getSalesReport(ActionEvent event) {
	    this.salesReport = getFile();
	    getSalesReport.setText("Selected");
	}
	
	@FXML protected void getStockSheet(ActionEvent event) {
		this.stockSheet = getFile();
		getStockSheet.setText("Selected");
	}
	
    @FXML protected void getExchangeRate(ActionEvent event) {
    	this.exchangeRate = Double.parseDouble(exchangeField.getText());
    }
	
	public File getFile() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(new Stage());
		return file;
	}
}
