package ob.app.tbl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ob.app.bean.FIReportBean;
import ob.app.bean.PidFxBean;
import ob.app.db.MsAccessDriver;

public class PidAccessTbl {
	public ObservableList<PidFxBean> getAllPIDsAccess(Connection conn) throws SQLException {
		ObservableList<PidFxBean> List_oL = FXCollections.observableArrayList();
		String sql = " select * from barcode.pidaccess";
		ResultSet rs = null;

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			while (rs.next()) {
				PidFxBean bean = new PidFxBean();
				bean.setCon(rs.getString("config"));
				bean.setSb(rs.getString("tyreSize"));
				bean.setRs(rs.getString("tyreRim"));
				bean.setTt(rs.getString("tyreType"));
				bean.setBr(rs.getString("brand"));
				bean.setSwmsg(rs.getString("sidewall"));
				bean.setPidAccess(rs.getString("processid"));

				List_oL.add(bean);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "PIDAccessTbl.getAllPIDsAccess " + e);
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return List_oL;
	}

	public boolean insertPIDPendingTbl(Connection conn, PidFxBean bean) throws Exception {
		// qg is seperately sent since Both A+ and A tires should be barcodes as A;

		String sb = bean.getSb();
		String con = bean.getCon();
		String rs = bean.getRs();
		String tt = bean.getTt();
		String br = bean.getBr();
		String lt = bean.getLt();
		String pid = bean.getPidAccess();
		String sw = bean.getSwmsg();
		// Insert BCPending table
		String sql = "insert into barcode.pidaccess (config,tyresize,tyrerim,tyretype,brand,sidewall,processid) "
				+ "values( '" + con + "','" + sb + "','" + rs + "','" + tt + "','" + br + "','" + sw + "','" + pid + "');";
		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null,
						"E01-Not updated PIDAccessTbl.insertPIDPendingTbl() table-:" + bean.getPidAccess());
				return false;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "E02-PIDAccessTbl.insertPIDPendingTbl():-" + e);
			return false;
		} finally {
			if (keys != null)
				keys.close();
		}
	}
	public boolean pidAvaialbilityCheck(Connection conn,int pid) {
		String ProecessID = "";
		// Convert pid to ProcessID in TTS
		if (pid > 199999) {
			pid = pid - 200000;
			ProecessID = "P-0" + Integer.toString(pid);
		} else {
			ProecessID = "P-" + Integer.toString(pid);
		}

		boolean bln = false;
		
		try {
			Statement s = conn.createStatement();

			String selTable = "SELECT * FROM barcode.pidaccess where processid = '" + ProecessID + "'";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				bln = true;
			} else {
				JOptionPane.showMessageDialog(null, "PID is not available in Access File " + ProecessID);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "PIDAccessTbl.getRow  E3" + e);
		}
		return bln;
	}
}
