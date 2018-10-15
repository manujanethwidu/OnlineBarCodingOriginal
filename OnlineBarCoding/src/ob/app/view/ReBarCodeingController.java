package ob.app.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javax.print.PrintException;
import javax.print.attribute.standard.Fidelity;
import javax.swing.JOptionPane;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import ob.app.MainApp;
import ob.app.bean.BCPendingBean;
import ob.app.bean.BasicDatafromTireCodeBean;
import ob.app.bean.DefectBean;
import ob.app.bean.FIReportBean;
import ob.app.bean.PidFxBean;
import ob.app.db.CreateConn;
import ob.app.print.BarCodePrinter;
import ob.app.tbl.BCPendingTbl;
import ob.app.tbl.DefectsTbl;
import ob.app.tbl.FIReportTbl;
import ob.app.tbl.JoinTbl;
import ob.app.tbl.PidAccessTbl;
import ob.app.tbl.REBCHistoryTbl;
import ob.app.tbl.StkTbl;
import ob.app.util.BarCodeCreator;
import ob.app.util.SNAvlCheck;
import ob.app.util.SNListGetter;

public class ReBarCodeingController implements Initializable {

	@FXML
	TextField txtSN;
	@FXML
	Label lblTireSize, lblBrnSWMsg, lblPID, lblSystem;
	////////////
	// PID Change controllres
	@FXML
	TableView<PidFxBean> tblPid;
	@FXML
	TableColumn<PidFxBean, Number> clmnPid;
	@FXML
	TableColumn<PidFxBean, String> clmnSb;
	@FXML
	TableColumn<PidFxBean, String> clmnLt;
	@FXML
	TableColumn<PidFxBean, String> clmnCon;
	@FXML
	TableColumn<PidFxBean, String> clmnRs;
	@FXML
	TableColumn<PidFxBean, String> clmnTt;
	@FXML
	TableColumn<PidFxBean, String> clmnBr;
	@FXML
	TableColumn<PidFxBean, String> clmnSw;
	@FXML
	TextField txtFFSB, txtFFLT, txtFFRS, txtFFCon, txtFFTT, txtFFBR, txtFFSWMSG, txtFFPID, txtQg;
	@FXML
	AnchorPane apPIDChange;
	@FXML
	ComboBox<DefectBean> cmbDefect;
	@FXML
	Button btnEnter, btnRefresh;
	ObservableList<PidFxBean> filteredData = FXCollections.observableArrayList();
	ObservableList<PidFxBean> masterData = FXCollections.observableArrayList();
	ObservableList<DefectBean> defectList = FXCollections.observableArrayList();
	BasicDatafromTireCodeBean basicDatafrmTCBean = new BasicDatafromTireCodeBean();
	FIReportBean fiReportBean = new FIReportBean();

	String ChangeType = "No";
	// qg is after changing text filed . qgfrmStkTbl is originally assigned qulity
	// grade
	String qgfrmStkTbl = "";
	String qg = "";

	int pidfrmStkTbl = 0;
	private MainApp mainApp;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cmbDefect.setVisible(false);
		btnEnter.setVisible(false);
		apPIDChange.setVisible(false);
		// TODO Auto-generated method stub
		clmnPid.setCellValueFactory(cellData -> cellData.getValue().pidProperty());
		clmnSb.setCellValueFactory(cellData -> cellData.getValue().sbProperty());
		clmnCon.setCellValueFactory(cellData -> cellData.getValue().conProperty());
		clmnLt.setCellValueFactory(cellData -> cellData.getValue().ltProperty());
		clmnRs.setCellValueFactory(cellData -> cellData.getValue().rsProperty());
		clmnTt.setCellValueFactory(cellData -> cellData.getValue().ttProperty());
		clmnBr.setCellValueFactory(cellData -> cellData.getValue().brProperty());
		clmnSw.setCellValueFactory(cellData -> cellData.getValue().swmsgProperty());
		// Create mastser data(frimPID Tbl)
		pidOListCreator();
		// Add filtered data to the table
		tblPid.setItems(filteredData);
		addTextChangLitnerstoTxtFileds();
		// PID Tbl Selection
		tblPid.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, nw) -> {
			basicDatafrmTCBean.setSizebasic(nw.getSb());
			basicDatafrmTCBean.setLugtype(nw.getLt());
			basicDatafrmTCBean.setConfig(nw.getCon());
			basicDatafrmTCBean.setRimsize(nw.getRs());
			basicDatafrmTCBean.setTiretype(nw.getTt());
			basicDatafrmTCBean.setSwmsg(nw.getSwmsg());
			basicDatafrmTCBean.setBrand(nw.getBr());
			basicDatafrmTCBean.setMoldNo("???");
			basicDatafrmTCBean.setTireCode(0);
			basicDatafrmTCBean.setPid(nw.getPid());
			setTireSizeLables();
			ChangeType = "RBPID";
			lblPID.setText(Integer.toString(basicDatafrmTCBean.getPid()));
		});

		txtSN.textProperty().addListener((observable, oldValue, newSB) -> {
			if (newSB.length() == 9) {
				JoinTbl jtbl = new JoinTbl();
				StkTbl stkTbl = new StkTbl();

				CreateConn createConn = new CreateConn();
				try (Connection conn = createConn.GetConn()) {
					int sn = Integer.parseInt(txtSN.getText());
					// Get Tire Details form SN
					SNAvlCheck snCheck = new SNAvlCheck();
					boolean snAvl = snCheck.snAVlCheckinAllTbls(conn, sn);

					basicDatafrmTCBean = jtbl.getTireDetailFrmstkTblc(conn, Integer.parseInt(newSB));
					// Get Already BarCoded TireQuaility
					if (basicDatafrmTCBean.getSn() != 0) {
						qgfrmStkTbl = stkTbl.getQG(conn, Integer.parseInt(newSB)).toUpperCase();
						qg = qgfrmStkTbl;
						fiReportBean.setQg(qg);
						txtQg.setText(qg);
						// PID
						pidfrmStkTbl = basicDatafrmTCBean.getPid();
						setTireSizeLables();

					} else {
						JOptionPane.showMessageDialog(null, "Not avl in stock Tbl");
					}

					if (snAvl) {
						lblSystem.setText("BarCoded by New System");
						apPIDChange.setVisible(true);
						txtQg.setEditable(true);
					} else {// if(snAvl)
						lblSystem.setText("BarCoded by TTS System");
						apPIDChange.setVisible(false);
						txtQg.setEditable(false);
					}

				} catch (SQLException | ClassNotFoundException | IOException e) {
					JOptionPane.showMessageDialog(null, "ReBarCoding.Initialize  E1 :- " + e);
				}
			}
			if (newSB.length() > 9) {
				txtSN.setText(newSB.substring(0, 9));

			}
			if (newSB.length() < 9) {
				basicDatafrmTCBean = null;
				btnEnter.setVisible(false);
				apPIDChange.setVisible(false);

				txtQg.setText("");
				btnEnter.setVisible(false);
				cmbDefect.setVisible(false);

				lblBrnSWMsg.setText("");
				lblPID.setText("");
				lblTireSize.setText("");

				txtFFPID.setText("");
				txtFFSB.setText("");
				txtFFLT.setText("");
				txtFFCon.setText("");
				txtFFRS.setText("");
				txtFFTT.setText("");
				txtFFBR.setText("");
				txtFFSWMSG.setText("");
				apPIDChange.setVisible(false);

			}
		});
		stringFormatter();
		// QG Change Listner
		txtQg.textProperty().addListener((x, y, newQG) -> {
			qg = newQG.toUpperCase();
			fiReportBean.setQg(qg);
			fiReportBean.setDefectid(0);
			// Convert to Capital
			txtQg.setText(newQG.toUpperCase());
			if (newQG.toUpperCase().equals(qgfrmStkTbl.toUpperCase())) {
				cmbDefect.setVisible(false);
				btnEnter.setVisible(true);
			} else {
				btnEnter.setVisible(false);
				cmbDefect.setVisible(true);
				fiReportBean.setQg(qg);
			}
		});
		// Defect Bean Set
		// Load combobox
		CreateConn createConn = new CreateConn();
		try (Connection conn = createConn.GetConn()) {
			// Get Defects form database
			DefectsTbl def = new DefectsTbl();
			defectList = def.getDefects(conn);
			// Load defects to combo box
			cmbDefect.setItems(defectList);
			// Redner items in combobox
			cmbDefect.setCellFactory((comboBox) -> {
				return new ListCell<DefectBean>() {
					@Override
					protected void updateItem(DefectBean item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(item.getDefect());
						}
					}
				};
			});
			// render selected item in combo box
			cmbDefect.setConverter(new StringConverter<DefectBean>() {
				@Override
				public String toString(DefectBean defect) {
					if (defect == null) {
						return null;
					} else {
						return defect.getDefect();
					}
				}

				@Override
				public DefectBean fromString(String personString) {
					return null; // No conversion fromString needed.
				}
			});

			// Handle ComboBox event.
			cmbDefect.setOnAction((event) -> {
				DefectBean selectedDefect = cmbDefect.getSelectionModel().getSelectedItem();
				fiReportBean.setDefectid(selectedDefect.getDefectID());
				btnEnter.setVisible(true);

			});
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "RebarCodingController.Initialize " + e);
		}
	}

	@FXML
	private void enterBtn() {
		CreateConn createConn = new CreateConn();

		FIReportTbl fiReportTbl = new FIReportTbl();
		BCPendingTbl bcPendingTbl = new BCPendingTbl();
		StkTbl stkTbl = new StkTbl();
		REBCHistoryTbl rebcHistory = new REBCHistoryTbl();
		/////////////////////////
		// set change type
		/*
		 * only if qulity change or PID changed update firepeort,bcpending,stock
		 * TBl,RBCHistory
		 * 
		 */

		if (!(qg.equals(qgfrmStkTbl))) {
			ChangeType = "RBQG";
		}

		if ((pidfrmStkTbl != basicDatafrmTCBean.getPid())) {
			ChangeType = "RBPID";
		}
		if (!(qg.equals(qgfrmStkTbl)) && (pidfrmStkTbl != basicDatafrmTCBean.getPid())) {
			ChangeType = "RBPIDQG";
		}
		/////////////////////////
		int sn = Integer.parseInt(txtSN.getText());
		try (Connection conn = createConn.GetConn()) {
			conn.setAutoCommit(false);
			boolean blnFIReportAvl = fiReportTbl.serialNoAvailablityCheck(conn, sn);
			boolean blnStkTblAvl = stkTbl.serialNoAvailablityCheck(conn, sn);
			boolean blnBCPendingAvl = bcPendingTbl.serialNoAvailablityCheck(conn, sn);

			boolean blnBCPendingUpdate = true;
			boolean blnStkUpdate = true;
			boolean blnREBCHistoryInsert = true;
			boolean blnFIReportUpdate = true;

			if (!(qg.equals(qgfrmStkTbl)) || (pidfrmStkTbl != basicDatafrmTCBean.getPid())) {
				// QG or PID Change
				blnFIReportUpdate = false;
				blnBCPendingUpdate = false;
				blnStkUpdate = false;
				blnREBCHistoryInsert = false;

				blnBCPendingUpdate = bcPendingTbl.updateBCPTblReBC(conn, basicDatafrmTCBean, fiReportBean, ChangeType,
						qg,pidfrmStkTbl );
				blnStkUpdate = stkTbl.updateStkTblREBC(conn, basicDatafrmTCBean, fiReportBean, qg);
				blnFIReportUpdate = fiReportTbl.updateFIRepoertTblcREBC(conn, basicDatafrmTCBean, fiReportBean);
				// Update date in REBCHistory tbl
				blnREBCHistoryInsert = rebcHistory.insertHistory(conn, pidfrmStkTbl, basicDatafrmTCBean, qgfrmStkTbl,
						qg);

			}

			if (blnBCPendingUpdate && blnStkUpdate && blnFIReportUpdate && blnREBCHistoryInsert) {
				// snAvl true and quality or pid changed happend.
				// Exeuted correctly
				conn.commit();
				conn.setAutoCommit(false);
				reBarCode();

			} else {
				conn.rollback();
				JOptionPane.showMessageDialog(null, "Rolled Back All Changes", "REBarCodingController.insertFIReport",
						1);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "REBarCodingController.insertFIReport E1" + e, null, 0);
		}
	}

	private void reBarCode() throws IOException, PrintException {

		BarCodePrinter barCodePrinter = new BarCodePrinter();
		BarCodeCreator barCodeCreator = new BarCodeCreator();
		String barCode = barCodeCreator.create(basicDatafrmTCBean, qg);
		barCodePrinter.printScaleWgt(barCode);
		// Refresh form
		refreshBtn();

		txtSN.requestFocus();
		txtSN.setText("");

	}

	@FXML
	private void refreshBtn() {
		txtSN.setText("");
		txtQg.setText("");
		btnEnter.setVisible(false);
		cmbDefect.setVisible(false);

		// basicDatafrmTCBean=null;
		// fiReportBean=null;
		lblBrnSWMsg.setText("");
		lblPID.setText("");
		lblTireSize.setText("");

		txtFFPID.setText("");
		txtFFSB.setText("");
		txtFFLT.setText("");
		txtFFCon.setText("");
		txtFFRS.setText("");
		txtFFTT.setText("");
		txtFFBR.setText("");
		txtFFSWMSG.setText("");
		apPIDChange.setVisible(false);
		txtSN.clear();
		System.out.println("Refreshed");
	}

	private void setTireSizeLables() {
		String tireSize = basicDatafrmTCBean.getSizebasic() + " " + basicDatafrmTCBean.getLugtype() + " "
				+ basicDatafrmTCBean.getConfig() + " " + basicDatafrmTCBean.getTiretype()+" "+basicDatafrmTCBean.getRimsize();
		String brnswmsg = basicDatafrmTCBean.getBrand() + " " + basicDatafrmTCBean.getSwmsg();
		lblBrnSWMsg.setText(brnswmsg);
		lblTireSize.setText(tireSize);
		lblPID.setText(Integer.toString(basicDatafrmTCBean.getPid()));
	}

	private void stringFormatter() {
		UnaryOperator<Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("-?([1-9][0-9]*)?")) {
				return change;
			}
			return null;
		};
		// modified version of standard converter that evaluates an empty string
		// as zero instead of null:
		StringConverter<Integer> converter = new IntegerStringConverter() {
			@Override
			public Integer fromString(String s) {
				if (s.isEmpty())
					return 0;
				return super.fromString(s);
			}
		};
		TextFormatter<Integer> textFormatter = new TextFormatter<Integer>(converter, null, integerFilter);
		txtSN.setTextFormatter(textFormatter);
	}

	@FXML
	private void pidOListCreator() {
		JoinTbl jtbl = new JoinTbl();
		CreateConn createConn = new CreateConn();
		try (Connection conn = createConn.GetConn()) {

			masterData = jtbl.getAllPIDs(conn);

			filteredData.addAll(masterData);
		} catch (SQLException | ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(null, "PIDChangeController.Initialize  E1 :- " + e);
		}

	}

	private void addTextChangLitnerstoTxtFileds() {
		txtFFSB.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});

		txtFFLT.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});

		txtFFCon.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});
		txtFFRS.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});
		txtFFTT.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});
		txtFFBR.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});
		txtFFSWMSG.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});

		txtFFPID.textProperty().addListener((observable, oldValue, newSB) -> {
			filteredData.clear();
			for (PidFxBean p : masterData) {
				if (matchesFilter(p)) {
					filteredData.add(p);
				}
			}
		});

	}

	private boolean matchesFilter(PidFxBean bean) {
		String refValue = "";

		String refsb = (bean.getSb()).toLowerCase();
		String reflt = (bean.getLt()).toLowerCase();
		String refcon = (bean.getCon().toLowerCase());
		String refrs = (bean.getRs()).toLowerCase();
		String reftt = (bean.getTt()).toLowerCase();
		String refbr = (bean.getBr()).toLowerCase();
		String refswmsg = (bean.getSwmsg()).toLowerCase();
		String refpid = Integer.toString(bean.getPid()).toLowerCase();

		String txtValsb = txtFFSB.getText().toLowerCase();
		String txtVallt = txtFFLT.getText().toLowerCase();
		String txtValcon = txtFFCon.getText().toLowerCase();
		String txtValrs = txtFFRS.getText().toLowerCase();
		String txtValtt = txtFFTT.getText().toLowerCase();
		String txtValbr = txtFFBR.getText().toLowerCase();
		String txtValswmsg = txtFFSWMSG.getText().toLowerCase();
		String txtPid = txtFFPID.getText().toLowerCase();

		/*
		 * if (newText == null || newText.isEmpty()) { // No filter --> Add all. return
		 * true; }
		 */

		// String lowerCaseNewText = newText.toLowerCase();
		if ((refsb.indexOf(txtValsb) != -1) && (reflt.indexOf(txtVallt) != -1) && (refcon.indexOf(txtValcon) != -1)
				&& (refrs.indexOf(txtValrs) != -1) && (reftt.indexOf(txtValtt) != -1) && (refbr.indexOf(txtValbr) != -1)
				&& (refpid.indexOf(txtPid) != -1) && (refswmsg.indexOf(txtValswmsg) != -1)) {
			return true;
		}

		return false; // Does not match
	}

}
