package ob.app.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ob.app.bean.AccessBean;
import ob.app.bean.AccessTablDataFXBean;
import ob.app.bean.PidFxBean;
import ob.app.db.CreateConn;
import ob.app.db.MsAccessDriver;
import ob.app.tbl.AccessFile;
import ob.app.tbl.JoinTbl;

public class AccessFileController implements Initializable {

	@FXML
	TableView<AccessTablDataFXBean> tblAccessFile;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnconfig;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmntyresize;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmntyrerim;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmntyretype;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnbrand;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnsidewall;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnstencilno;

	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnqualitygrade;
	@FXML
	TableColumn<AccessTablDataFXBean, Date> clmndateofmanufacture;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnProcessid;
	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnPrevProcessid;

	@FXML
	TableColumn<AccessTablDataFXBean, String> clmnDispatchStatus;

	@FXML
	TableColumn<AccessTablDataFXBean, Date> clmnModifydatte;
	@FXML
	TableColumn<AccessTablDataFXBean, Number> clmnStatus;
	@FXML
	TableColumn<AccessTablDataFXBean, Number> clmnMergeStatus;

	ObservableList<AccessTablDataFXBean> filteredData = FXCollections.observableArrayList();
	ObservableList<AccessTablDataFXBean> masterData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clmnconfig.setCellValueFactory(cellData -> cellData.getValue().configProperty());
		clmnconfig.setCellValueFactory(cellData -> cellData.getValue().configProperty());
		clmntyresize.setCellValueFactory(cellData -> cellData.getValue().tyresizeProperty());
		clmntyrerim.setCellValueFactory(cellData -> cellData.getValue().tyrerimProperty());
		clmntyretype.setCellValueFactory(cellData -> cellData.getValue().tyretypeProperty());
		clmnbrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
		clmnsidewall.setCellValueFactory(cellData -> cellData.getValue().sidewallProperty());
		clmnstencilno.setCellValueFactory(cellData -> cellData.getValue().stencilnoProperty());
		clmnqualitygrade.setCellValueFactory(cellData -> cellData.getValue().qualitygradeProperty());
		clmndateofmanufacture.setCellValueFactory(cellData -> cellData.getValue().dateofmanufactureProperty());
		clmnProcessid.setCellValueFactory(cellData -> cellData.getValue().ProcessidProperty());
		clmnDispatchStatus.setCellValueFactory(cellData -> cellData.getValue().DispatchStatusProperty());
		clmnModifydatte.setCellValueFactory(cellData -> cellData.getValue().ModifydatteProperty());
		clmnStatus.setCellValueFactory(cellData -> cellData.getValue().StatusProperty());
		clmnMergeStatus.setCellValueFactory(cellData -> cellData.getValue().MergeStatusProperty());
		clmnPrevProcessid.setCellValueFactory(cellData -> cellData.getValue().PrevProcessidProperty());
		try {
			pidOListCreator();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tblAccessFile.setItems(filteredData);
	}

	@FXML
	private void pidOListCreator() throws IOException {

		MsAccessDriver driver = new MsAccessDriver();

		AccessBean bean = new AccessBean();
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		try (Connection conn = msAccessDriver.getMsAccessConn();) {
			AccessFile acfile = new AccessFile();
			filteredData.clear();
			masterData.clear();
			masterData = acfile.getAllEntries(conn);
			filteredData.clear();
			filteredData.addAll(masterData);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFileController.initialize " + e);
		}
	}

	@FXML
	private void delAll() {
		MsAccessDriver driver = new MsAccessDriver();

		AccessBean bean = new AccessBean();
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		try (Connection conn = msAccessDriver.getMsAccessConn();) {
			//AccessFile acfile = new AccessFile();
			//acfile.DeleteAll();
			//pidOListCreator();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFileController.initialize " + e);
		}

	}
}
