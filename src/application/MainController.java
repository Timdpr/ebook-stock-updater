package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private Button getSalesReport;
    @FXML
    private Button getStockSheet;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView imageView;

    private File salesReport;
    private File stockSheet;

    @FXML
    protected void update(ActionEvent event) {
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

    @FXML
    protected void getSalesReport(ActionEvent event) {
        this.salesReport = getFile();
        getSalesReport.setText("Selected");
    }

    @FXML
    protected void getStockSheet(ActionEvent event) {
        this.stockSheet = getFile();
        getStockSheet.setText("Selected");
    }

    public File getFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(new Stage());
        return file;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.setImage(new Image ("/tartarus-press.jpg"));
    }
}
