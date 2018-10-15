package ob.app.tbl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ob.app.bean.BCPendingBean;
import ob.app.bean.BasicDatafromTireCodeBean;
import ob.app.bean.FIReportBean;

public class BCPendingTbl {
	public boolean insertBCPendingTbl(Connection conn, BasicDatafromTireCodeBean bean, String qg, String changetypex,
			int pidOld) throws Exception {
		// qg is seperately sent since Both A+ and A tires should be barcodes as A;
		int sn = bean.getSn();
		int pid = bean.getPid();
		int tc = bean.getTireCode();

		// Insert BCPending table
		String sql = "insert into barcode.bcpending (sn,pid,tc,bcdate,qg,changetype,pidold) values(" + sn + "," + pid
				+ "," + tc + ",now(),'" + qg + "','" + changetypex + "'," + pidOld + ");";

		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null,
						"E01-Not updated BCPendingTbl.insertBCPendingTbl() table-:" + bean.getSn());
				return false;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "E02-BCPendingTbl.insertBCPendingTbl():-" + e);
			return false;
		} finally {
			if (keys != null)
				keys.close();
		}
	}

	public boolean CrossChleckInsert(Connection conn, int sn) throws SQLException {
		String sql = "select * from barcode.bcpending where sn = " + sn + "; ";
		ResultSet rs = null;
		boolean avl = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			if (rs.next()) {
				avl = true;
			} else {
				JOptionPane.showMessageDialog(null, "E03BCPendingTbl.CrossChleckInsert() SN is not inserted");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, " E04 BCPendingTbl.CrossChleckInsert()  " + e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return avl;
	}

	public boolean updateBCPendingTblAccessUpdate(Connection conn, int sn) throws Exception {
		// set repari boolean
		boolean updated = false;

		// Update stk table
		String sql = "update barcode.bcpending set updated=true,updateddate=now()  where sn=" + sn + "; ";

		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			int affected = stmt.executeUpdate();
			if (affected == 1) {
				updated = true;
			} else {
				JOptionPane.showMessageDialog(null, "E05 Not updated BCPendingTbl.updateBCPendingTbl table-:" + sn);

			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "E06-BCPendingTbl.updateBCPendingTbl() :-" + e);

		} finally {
			if (keys != null)
				keys.close();
		}
		return updated;
	}

	public boolean updateBCPTblReBC(Connection conn, BasicDatafromTireCodeBean beanTC, FIReportBean beanFI,
			String changetypex, String qg,int pidOld) throws Exception {
		// set repari boolean
		boolean updated = false;

		// Update stk table
		String sql = "update barcode.bcpending set updated=false,rebc=true,pid = " + beanTC.getPid() + ",tc= "
				+ beanTC.getTireCode() + ",qg= '" + qg + "',changetype='" + changetypex + "',pidold="+pidOld+"  where sn="
				+ beanTC.getSn() + "; ";

		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			int affected = stmt.executeUpdate();
			if (affected == 1) {
				updated = true;
			} else {
				JOptionPane.showMessageDialog(null,
						"  Not updated BCPendingTbl.updateBCPTblReBC() -:" + beanTC.getSn());

			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					" -BCPendingTbl.updateBCPendingTblREBCoding() SN  :-" + beanTC.getSn() + " " + e);

		} finally {
			if (keys != null)
				keys.close();
		}
		return updated;
	}

	public ObservableList<BCPendingBean> getNotUpdatedList(Connection conn) throws SQLException {
		ObservableList<BCPendingBean> oList = FXCollections.observableArrayList();

		String sql = "select * from barcode.bcpending where updated = false ";
		ResultSet rs = null;

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			while (rs.next()) {
				BCPendingBean bean = new BCPendingBean();
				bean.setSn(rs.getInt("sn"));
				bean.setPidNw(rs.getInt("pid"));
				bean.setBcDate(rs.getDate("bcdate"));
				bean.setQg(rs.getString("qg"));
				bean.setRebc((rs.getBoolean("rebc")));
				bean.setPidOld(rs.getInt("pidold"));

				oList.add(bean);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, " E07 BCPendingTbl.CrossChleckInsert()  " + e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return oList;

	}

	// Check Availbility
	public boolean serialNoAvailablityCheck(Connection conn, int sn) {

		boolean bln = false;

		try {

			Statement s = conn.createStatement();

			String selTable = "SELECT * FROM barcode.bcpending where sn = " + sn + ";";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				bln = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "BCPending.serialNoAvailablityCheck  E3" + e);
		}

		return bln;

	}

	public static BCPendingBean getRow(Connection conn,int sn) throws SQLException {
		BCPendingBean bean = new BCPendingBean();
		String sql = "select * from barcode.bcpending where sn = "+sn+" ";
		ResultSet rs = null;

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			if (rs.next()) {

				bean.setSn(rs.getInt("sn"));
				bean.setPidNw(rs.getInt("pid"));
				bean.setBcDate(rs.getDate("bcdate"));
				bean.setQg(rs.getString("qg"));
				bean.setRebc((rs.getBoolean("rebc")));
				bean.setUpdated(rs.getBoolean("updated"));

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, " E07 BCPendingTbl.CrossChleckInsert()  " + e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return bean;
	}
}
