package ob.app.tbl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import ob.app.bean.BasicDatafromTireCodeBean;

public class REBCHistory {
	public boolean insertHistory(Connection conn, int oldPid, BasicDatafromTireCodeBean bean, String oldQg,
			String newQg) throws Exception {
		// qg is seperately sent since Both A+ and A tires should be barcodes as A;

		int sn = bean.getSn();
		int newPid = bean.getPid();
		// Insert BCPending table
		String sql = "insert into barcode.rebchistory (sn,oldpid,newpid,oldqg,newqg,rebcdate) values(" + sn + "," + oldPid
				+ "," + newPid + ",'" + oldQg + "','" + newQg + "',now());";
 
		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "REBcHistory.insertHistory()  " + bean.getSn());
				return false;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "REBcHistory.insertHistory()  :-" + e);
			return false;
		} finally {
			if (keys != null)
				keys.close();
		}
	}
}
