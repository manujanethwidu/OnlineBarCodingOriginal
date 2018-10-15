package ob.app.tbl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import ob.app.bean.BasicDatafromTireCodeBean;
import ob.app.bean.FIReportBean;

public class ReBCPendingTbl {
	public boolean insertRBBCPendingTbl(Connection conn, BasicDatafromTireCodeBean bean, String qg, String changetypex)
			throws Exception {
		// qg is seperately sent since Both A+ and A tires should be barcodes as A;
		int sn = bean.getSn();
		int pid = bean.getPid();
		int tc = bean.getTireCode();

		// Insert BCPending table
		String sql = "insert into barcode.rebcpending (sn,pid,tc,bcdate,qg,changetype) values(" + sn + "," + pid + ","
				+ tc + ",now(),'" + qg + "','" + changetypex + "');";

		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null,
						"E01-Not updated RBBCPendingTbl.insertRBBCPendingTbl() table-:" + bean.getSn());
				return false;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "E02-RBBCPendingTbl.insertRBBCPendingTbl():-" + e);
			return false;
		} finally {
			if (keys != null)
				keys.close();
		}
	}

	// Check Availbility
	public boolean serialNoAvailablityCheck(Connection conn, int sn) {

		boolean bln = false;

		try {

			Statement s = conn.createStatement();

			String selTable = "SELECT * FROM quality.rebcpending where sn = " + sn + ";";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				bln = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "rebcpending.serialNoAvailablityCheck  E3" + e);
		}

		return bln;

	}

	public boolean updateREBCPending(Connection conn, BasicDatafromTireCodeBean bean, String qg) throws Exception {
		boolean aff = false;
		String sql = "update reBCPendingTbl.updateREBCPending set  pid = " + bean.getPid() + ",qg='" + qg
				+ "',tc = 0,updated=false  where sn=?; ";
		System.out.println(sql);
		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			stmt.setInt(1, bean.getSn());
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				aff = true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "ReBCPending.updateREBCPending() :-" + e);

		} finally {
			if (keys != null)
				keys.close();
		}
		return aff;
	}

}
