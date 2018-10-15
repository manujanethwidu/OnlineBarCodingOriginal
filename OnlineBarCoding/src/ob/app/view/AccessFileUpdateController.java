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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.PixelFormat;
import ob.app.MainApp;
import ob.app.bean.AccessBean;
import ob.app.bean.BCPendingBean;
import ob.app.bean.PidFxBean;
import ob.app.db.CreateConn;
import ob.app.tbl.AccessFile;
import ob.app.tbl.BCPendingTbl;
import ob.app.tbl.PidAccessTbl;
import ob.app.util.AccessBeanCreator;
import ob.app.util.PendingPIDListCreator;
import ob.app.util.SNAvlCheck;

public class AccessFileUpdateController implements Initializable {

	@FXML
	TableView<BCPendingBean> tblPendingTires;
	@FXML
	TableColumn<BCPendingBean, Number> clmnSN;
	@FXML
	TableColumn<BCPendingBean, Number> clmnPID;
	@FXML
	TableColumn<BCPendingBean, Date> clmnBCDate;
	@FXML
	TableColumn<BCPendingBean, String> clmnQG;
	@FXML
	Label lblNotUpdated;
	///////////////
	/////
	@FXML
	TableView<PidFxBean> tblPendingPid;
	@FXML
	TableColumn<PidFxBean, String> clmnPid;
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
	int CountPendingTires, CountPendingPIDs = 0;
	////
	ObservableList<BCPendingBean> oList = FXCollections.observableArrayList();
	//
	ObservableList<PidFxBean> oL_AccessPID = FXCollections.observableArrayList();
	ObservableList<PidFxBean> oL_PSQLPID = FXCollections.observableArrayList();
	ObservableList<PidFxBean> oL_MatchedPID = FXCollections.observableArrayList();

	int pidOriginal = 0;

	private MainApp mainApp;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clmnPid.setCellValueFactory(cellData -> cellData.getValue().pidAccessProperty());
		clmnSb.setCellValueFactory(cellData -> cellData.getValue().sbProperty());
		clmnCon.setCellValueFactory(cellData -> cellData.getValue().conProperty());
		clmnLt.setCellValueFactory(cellData -> cellData.getValue().ltProperty());
		clmnRs.setCellValueFactory(cellData -> cellData.getValue().rsProperty());
		clmnTt.setCellValueFactory(cellData -> cellData.getValue().ttProperty());
		clmnBr.setCellValueFactory(cellData -> cellData.getValue().brProperty());
		clmnSw.setCellValueFactory(cellData -> cellData.getValue().swmsgProperty());
		try {
			updateTableView_BCPending();
			updatePIDTbl();

		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "AccessFileUpdateController.Initialize " + e);
		}
	}

	@FXML
	private void updatePIDTbl() {
		PendingPIDListCreator pending = new PendingPIDListCreator();
		oL_MatchedPID = pending.notUpdatedPIDListCriator();
		tblPendingPid.setItems(oL_MatchedPID);
		CountPendingPIDs = 0;
		oL_MatchedPID.forEach((PidFxBean value) -> {
			CountPendingPIDs++;
		});
	}

	@FXML
	private void uploadPIDs() {
		CreateConn createConn = new CreateConn();
		// Get not updated to Access File data from database
		try (Connection conn = createConn.GetConn()) {
			oL_MatchedPID.forEach((PidFxBean bean) -> {
				PidAccessTbl pidAccessTbl = new PidAccessTbl();
				try {
					pidAccessTbl.insertPIDPendingTbl(conn, bean);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "AccessFileUpdateController.uploadPIDs " + e);
				}
			});
			updatePIDTbl();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFileUpdateController.uploadPIDs " + e);
		}
		// Upload pending Barcode Tires
		try {
			uploadtAccessFileBCPending();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "AccessFileUpdateController.uploadPIDs " + e);
		}
	}

	private void updateTableView_BCPending() throws ClassNotFoundException {
		BCPendingTbl bcPendingTbl = new BCPendingTbl();
		CreateConn createConn = new CreateConn();
		// Get not updated to Access File data from database
		try (Connection conn = createConn.GetConn()) {
			oList = bcPendingTbl.getNotUpdatedList(conn);
			tblPendingTires.setItems(oList);

			CountPendingTires = 0;
			oList.forEach((BCPendingBean value) -> {
				CountPendingTires++;
			});
			lblNotUpdated.setText(Integer.toString(CountPendingTires));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFileUpdateController.updateTableView_AccessFileUpdate" + e);
		}
		// Set column cell value factory
		clmnSN.setCellValueFactory(cellData -> cellData.getValue().snProperty());
		clmnPID.setCellValueFactory(cellData -> cellData.getValue().pidNwProperty());
		clmnBCDate.setCellValueFactory(cellData -> cellData.getValue().bcDateProperty());
		clmnQG.setCellValueFactory(cellData -> cellData.getValue().qgProperty());

	}

	@FXML
	private void uploadtAccessFileBCPending() throws ClassNotFoundException {
		oList.forEach((BCPendingBean value) -> {

			String qg = "";
			AccessFile accessFile = new AccessFile();
			AccessBeanCreator accessBeanCreator = new AccessBeanCreator();

			// Check PID is available in access File
			boolean blnPIDinAccessFile = accessFile.pidAvaialbilityCheck(value.getPidNw());
			if (blnPIDinAccessFile) {// PID is in acess file
				// Create Access Bean
				pidOriginal = value.getPidOld();
				qg = value.getQg();
				AccessBean accessBean = accessBeanCreator.CreateAccessBean(value.getSn(), value.getPidNw(), pidOriginal,
						qg, value.isRebc());
				// Check access file already have the tire with SN
				boolean snAvlAccess = accessFile.serialNoAvailablityCheck(accessBean.getStencilno());
				if (!value.isRebc()) {
					// this branch for New barcodings
					if (!snAvlAccess) {// not in access file
						// Insert deta to Access File
						boolean insertedtoAccess = accessFile.inserttoAccessFilea(accessBean);
						insertedtoAccess = accessFile.serialNoAvailablityCheck(accessBean.getStencilno());
						if (insertedtoAccess) {
							CreateConn createConn = new CreateConn();
							try (Connection conn = createConn.GetConn()) {
								BCPendingTbl bcpTbl = new BCPendingTbl();
								boolean updateBCPending = bcpTbl.updateBCPendingTblAccessUpdate(conn, value.getSn());
								if (!updateBCPending) {
									delAccessRecord(value.getSn(), value.getPidNw(), value.getQg());
								} else {
									System.out.println("Updated");
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,
										"AccessFileUpdateController.uploadAccessFileBCPending() " + e);
							}
						} else {// insertedtoAccess
							JOptionPane.showMessageDialog(null, "'AccessFileUpdateCtrl.UploadAccessFileBCPending");
						}
					} else {// (!snAvlAccess)
						// Tire is already in access file
						JOptionPane.showMessageDialog(null, "SNo is already avaialble in Access File. Not updated ");
					}
				} else {
					// Re BarCoding

					// Fresh tires without TTS Update
					boolean blnFreshBC = false;
					int sn = value.getSn();
					CreateConn createConnx = new CreateConn();
					try (Connection conn = createConnx.GetConn()) {
						if (!snAvlAccess) {
							SNAvlCheck sna = new SNAvlCheck();
							blnFreshBC =sna.snAVlCheckinAllTbls(conn, value.getSn());
						}

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"AccessFileUpdateController.uploadAccessFileBCPending() " + e);
					}

					if ((snAvlAccess) || blnFreshBC) {// sn is avl in access file or fresh bc without updated
						// Check old PID is in access file databasel
						boolean blnOldPIDinAccessFile = accessFile.pidAvaialbilityCheck(pidOriginal);
						if (blnOldPIDinAccessFile) {
							CreateConn createConn = new CreateConn();
							try (Connection conn = createConn.GetConn()) {
								conn.setAutoCommit(false);
								BCPendingTbl bcpTbl = new BCPendingTbl();
								// Update BCPending Tbl
								boolean updateBCPending = bcpTbl.updateBCPendingTblAccessUpdate(conn, value.getSn());

								if (updateBCPending) {
									boolean blnAccessUpdateReBC = false;
									boolean blnAccessInsertReBC = false;
									// BC pending has updated
									if (!blnFreshBC) {
										blnAccessUpdateReBC = accessFile.updateAccessFileReBC(accessBean);
									} else {
										// Fresh BC not updated
										// Insert in to access file
										blnAccessInsertReBC = accessFile.inserttoAccessFilea(accessBean);
									}

									if (blnAccessUpdateReBC || blnAccessInsertReBC) {
										conn.commit();
									} else {
										conn.rollback();
									}

								}

							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,
										"AccessFileUpdateController.uploadAccessFileBCPending() " + e);
							}

						} else {
							JOptionPane.showMessageDialog(null, "Original PID is not available in Access File");
						}

					} else {// not in access file
						JOptionPane.showMessageDialog(null, "sNo is not available in Access File  ");
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "PID is not available in Access File");
			}
		});
		// Refresh the BCPending TableView
		updateTableView_BCPending();
	}

	private void delAccessRecord(int sn, int pid, String gq) {
		// if postgresql database is not updated delete the record in MSAccess file
		AccessBeanCreator accessBeanCreator = new AccessBeanCreator();
		AccessFile accessFile = new AccessFile();

		// below we need pid and sn only
		AccessBean accessBean = accessBeanCreator.CreateAccessBean(sn, pid, pidOriginal, "A", true);
		accessFile.DeleteRecordAccessFile(accessBean);
		JOptionPane.showMessageDialog(null, "Deleted", "Delete Record", 1);
	}

}
