package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	@FXML
	private Button getSalesReport;
	@FXML
	private Button getStockSheet;
	@FXML
	private Label errorLabel;
	
	private File salesReport;
	private File stockSheet;
	
	@FXML protected void update(ActionEvent event) {
		try {
			salesReport.exists();
			stockSheet.exists();
		} catch (NullPointerException e) {
			errorLabel.setText("Error: Make sure both spreadsheets are selected");
		}
		try {
		    StockSheetUpdater updater = new StockSheetUpdater(salesReport, stockSheet, errorLabel);
		    updater.update();
		} catch (NullPointerException e) {
			System.out.println("End of reportSheet rows");
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
	
	public File getFile() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(new Stage());
		return file;
	}
}