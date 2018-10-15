package ob.app.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.print.PrintException;
import javax.sql.rowset.Joinable;
import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ob.app.MainApp;
import ob.app.bean.BasicDatafromTireCodeBean;
import ob.app.db.CreateConn;
import ob.app.print.BarCodePrinter;
import ob.app.tbl.AccessFile;
import ob.app.tbl.JoinTbl;
import ob.app.tbl.StkTbl;
import ob.app.util.SNListGetter;

public class ButtonBarController implements Initializable {

	int callStation;
	@FXML
	Button btnRefresh, btnUpdateAccessFile;
	@FXML
	Label lblNotUpdated;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setBean() {

	}

	private MainApp mainApp;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

	@FXML
	private void testPrint() {

		BarCodePrinter barCodePrinter = new BarCodePrinter();
		try {
			barCodePrinter.printScaleWgt("    PRINTER READY");
		} catch (PrintException | IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ButtonBarController.Print " + e);
		}
	}

	@FXML
	private void RefreshList() throws ClassNotFoundException, SQLException {
		// Reload the SNList
		SNListGetter snListGetter = new SNListGetter();
		ObservableList<BasicDatafromTireCodeBean> List_oL = snListGetter.getSNList();

		mainApp.addSNList(List_oL);
	}

	@FXML
	private void accessInsert() throws SQLException {

		mainApp.addAccessUpdate();

	}

	@FXML
	private void rebarcode() {
		mainApp.addReBarCoding();
	}

	@FXML
	private void accessFileShow() {
		 try {
		        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccessFile.fxml"));
		                Parent root1 = (Parent) fxmlLoader.load();
		                Stage stage = new Stage();
		                stage.setScene(new Scene(root1));  
		                stage.setMaximized(true);
		                stage.show();
		        } catch(Exception e) {
		           e.printStackTrace();
		          }


	}

}
