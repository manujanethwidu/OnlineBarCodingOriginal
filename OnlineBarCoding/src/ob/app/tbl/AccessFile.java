package ob.app.tbl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ob.app.bean.AccessBean;
import ob.app.bean.AccessTablDataFXBean;
import ob.app.bean.PidFxBean;
import ob.app.db.MsAccessDriver;

public class AccessFile {

	public boolean inserttoAccessFilea(AccessBean bean) {
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		String sql = "insert into tbqualitycontrol (config,tyresize,tyrerim,tyretype,brand,sidewall,barcode,qualitygrade,remarks,"
				+ "dateofmanufacture,Processid,tyrestatus,CreateDate,Status,stencilno,DispatchStatus,MergeStatus) "
				+ "values(?,?,?,?,?,?,?,?,?,now()),?,?,now(),?,?,?,?";
		try (Connection conn = msAccessDriver.getMsAccessConn();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			stmt.setString(1, bean.getConfig());
			stmt.setString(2, bean.getTyresize());
			stmt.setString(3, bean.getTyrerim());
			stmt.setString(4, bean.getTyretype());
			stmt.setString(5, bean.getBrand());
			stmt.setString(6, bean.getSidewall());
			stmt.setString(7, bean.getBarcode());
			stmt.setString(8, bean.getQualitygrade());
			stmt.setString(9, bean.getRemarks());
			stmt.setString(10, bean.getProcessIDnw());
			stmt.setString(11, bean.getTyrestatus());
			stmt.setInt(12, bean.getStatus());
			stmt.setString(13, bean.getStencilno());
			stmt.setString(14, bean.getDispatchStatus());
			stmt.setInt(15, bean.getMergeStatus());

			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				System.err.println("Now rows affected");
				JOptionPane.showMessageDialog(null, "AccessFile.InserttoAccessFile()" + bean.getStencilno());

				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.inserttoAccessFile() not inserted E1" + e);

			return false;
		}
	}

	public boolean DeleteRecordAccessFile(AccessBean bean) {
		String sno = bean.getStencilno();
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		String sql = "DELETE FROM tbqualitycontrol WHERE stencilno ='" + sno + "'";
		try (Connection conn = msAccessDriver.getMsAccessConn();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			// stmt.setString(1, bean.getConfig());

			int affected = stmt.executeUpdate();

			if (affected == 1) {
				return true;
			} else {
				System.err.println("Now rows affected");
				JOptionPane.showMessageDialog(null, "AccessFile.DeletRecord-:  " + sno);
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.DeletRecordFromAccessFile " + e);
			return false;
		}
	}

	public void DeleteAll() {

		MsAccessDriver msAccessDriver = new MsAccessDriver();
		String sql = "DELETE FROM tbqualitycontrol";
		try (Connection conn = msAccessDriver.getMsAccessConn();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			// stmt.setString(1, bean.getConfig());

			int affected = stmt.executeUpdate();

			if (affected == 1) {
				System.out.println("Deleted");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.DeleteAll " + e);

		}
	}

	public boolean serialNoAvailablityCheck(String sn) {
		// sn as in access file
		boolean bln = false;
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		try (Connection conn = msAccessDriver.getMsAccessConn();) {

			Statement s = conn.createStatement();

			String selTable = "SELECT * FROM tbqualitycontrol where stencilno = '" + sn + "'";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				bln = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.SerilNoAvailablityCheck  E3" + e);
		}

		return bln;

	}

	public boolean pidAvaialbilityCheck(int pid) {
		String ProecessID = "";
		// Convert pid to ProcessID in TTS
		if (pid > 199999) {
			pid = pid - 200000;
			ProecessID = "P-0" + Integer.toString(pid);
		} else {
			ProecessID = "P-" + Integer.toString(pid);
		}

		boolean bln = false;
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		try (Connection conn = msAccessDriver.getMsAccessConn();) {
			Statement s = conn.createStatement();

			String selTable = "SELECT * FROM ProcessID_Details where ProcessID = '" + ProecessID + "'";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				bln = true;
			} else {
				JOptionPane.showMessageDialog(null, "PID is not available in Access File " + ProecessID);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.pidAvaialbiltyCheck  E3" + e);
		}
		return bln;
	}

	public AccessBean getAccessBean(String ProcessID) throws SQLException {
		AccessBean bean = new AccessBean();
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		try (Connection conn = msAccessDriver.getMsAccessConn();) {

			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM ProcessID_Details where ProcessID = '" + ProcessID + "'";
			stmt.execute(sql);

			ResultSet rs = stmt.getResultSet();
			if (rs.next()) {
				bean.setConfig(rs.getString("Config"));
				bean.setTyresize(rs.getString("TyreSize"));
				bean.setTyrerim(rs.getString("TyreRim"));
				bean.setTyretype(rs.getString("TyreType"));
				bean.setBrand(rs.getString("Brand"));
				bean.setSidewall(rs.getString("Sidewall"));
				bean.setProcessIDnw(rs.getString("ProcessID"));
				return bean;
			} else {
				return null;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.getAccessBean  E4" + e);
			return null;
		}

	}

	public ObservableList<AccessTablDataFXBean> getAllEntries(Connection conn) throws SQLException {
		ObservableList<AccessTablDataFXBean> List_oL = FXCollections.observableArrayList();
		String sql = "SELECT * FROM tbqualitycontrol";

		ResultSet rs = null;

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			while (rs.next()) {
				AccessTablDataFXBean bean = new AccessTablDataFXBean();
				bean.setConfig(rs.getString("config"));
				bean.setTyresize(rs.getString("tyresize"));
				bean.setTyrerim(rs.getString("tyrerim"));
				bean.setTyretype(rs.getString("tyretype"));
				bean.setBrand(rs.getString("brand"));
				bean.setSidewall(rs.getString("sidewall"));
				bean.setStencilno(rs.getString("stencilno"));
				bean.setBarcode(rs.getString("barcode"));
				bean.setQualitygrade(rs.getString("qualitygrade"));
				bean.setRemarks(rs.getString("remarks"));
				bean.setDateofmanufacture(rs.getDate("dateofmanufacture"));
				bean.setProcessid(rs.getString("Processid"));
				bean.setPrevProcessid(rs.getString("PrevProcessid"));
				bean.setTyrestatus(rs.getString("tyrestatus"));
				bean.setDispatchStatus(rs.getString("DispatchStatus"));
				bean.setCreateDate(rs.getDate("CreateDate"));
				bean.setModifydatte(rs.getDate("Modifydatte"));
				bean.setStatus(rs.getInt("Status"));
				bean.setMergeStatus(rs.getInt("MergeStatus"));
				List_oL.add(bean);

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "AccessFile.getAllRows " + e);
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		return List_oL;

	}

	public ObservableList<PidFxBean> getAllPIDsAccess(Connection conn) throws SQLException {
		ObservableList<PidFxBean> List_oL = FXCollections.observableArrayList();
		String sql = "SELECT * FROM ProcessID_Details";

		ResultSet rs = null;

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			while (rs.next()) {
				PidFxBean bean = new PidFxBean();
				bean.setCon(rs.getString("Config"));
				bean.setSb(rs.getString("TyreSize"));
				bean.setRs(rs.getString("TyreRim"));
				bean.setTt(rs.getString("TyreType"));
				bean.setBr(rs.getString("Brand"));
				bean.setSwmsg(rs.getString("Sidewall"));
				bean.setPidAccess(rs.getString("ProcessID"));

				List_oL.add(bean);

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "AccessFile.getAllPIDsAccess " + e);
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		return List_oL;

	}

	public boolean ss(AccessBean bean) {
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		String sql = "update tbqualitycontrol set  config ='?',tyresize ='?',  tyrerim ='?',"
				+ "  tyretype ='?',  brand ='?',  sidewall ='?',  barcode ='?',  qualitygrade ='?' , "
				+ " Processid ='?',  tyrestatus ='?'," + "  Status =? ,"
				+ "  MergeStatus =?,  PrevProcessid = '?' where stencilno = '" + bean.getStencilno() + "'";
		try (Connection conn = msAccessDriver.getMsAccessConn();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			stmt.setString(1, bean.getConfig());
			stmt.setString(2, bean.getTyresize());
			stmt.setString(3, bean.getTyrerim());
			stmt.setString(4, bean.getTyretype());
			stmt.setString(5, bean.getBrand());
			stmt.setString(6, bean.getSidewall());
			stmt.setString(7, bean.getBarcode());
			stmt.setString(8, bean.getQualitygrade());
			stmt.setString(9, bean.getProcessIDnw());
			stmt.setString(10, bean.getTyrestatus());
			stmt.setInt(11, bean.getStatus());
			stmt.setInt(12, bean.getMergeStatus());
			stmt.setString(13, bean.getProcessIDOld());

			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				System.err.println("Now rows affected");
				JOptionPane.showMessageDialog(null,
						"AccessFile.updateAccessFileReBC not updated " + bean.getStencilno());

				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.updateAccessFileReBC not updated  " + e);

			return false;
		}
	}

	public boolean updateAccessFileReBC(AccessBean bean) {
		MsAccessDriver msAccessDriver = new MsAccessDriver();
		String sql = "update tbqualitycontrol set  config ='"+bean.getConfig()+"',tyresize ='"+ bean.getTyresize()+"',  tyrerim ='"+bean.getTyrerim()+"',"
				+ "  tyretype ='"+bean.getTyretype()+"',  brand ='"+bean.getBrand()+"',  sidewall ='"+bean.getSidewall()+"',  barcode ='"+ bean.getBarcode()+"',  qualitygrade ='"+bean.getQualitygrade()+"' , "
				+ " Processid ='"+bean.getProcessIDnw()+"',  tyrestatus ='"+bean.getTyrestatus()+"'," + "  Status ="+bean.getStatus()+" ,"
				+ "  MergeStatus ="+ bean.getMergeStatus()+",  PrevProcessid = '"+bean.getProcessIDOld()+"' where stencilno = '" + bean.getStencilno() + "'";
		try (Connection conn = msAccessDriver.getMsAccessConn();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			// stmt.setString(1, );
			// stmt.setString(2,);
			// stmt.setString(3, );
			// stmt.setString(4, );
			// stmt.setString(5, );
			// stmt.setString(6, );
			// stmt.setString(7,);
			// stmt.setString(8,());
			// stmt.setString(9, );
			// stmt.setString(10, );
			// stmt.setInt(11, );
			// stmt.setInt(12,);
			// stmt.setString(13, );

			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				System.err.println("Now rows affected");
				JOptionPane.showMessageDialog(null,
						"AccessFile.updateAccessFileReBC not updated " + bean.getStencilno());

				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessFile.updateAccessFileReBC not updated  " + e);

			return false;
		}
	}

}
