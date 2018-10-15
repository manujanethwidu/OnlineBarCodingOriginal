package ob.app.tbl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import ob.app.bean.BasicDatafromTireCodeBean;
import ob.app.bean.FIReportBean;

public class StkTbl {
	public boolean updateStkTbl(Connection conn, BasicDatafromTireCodeBean bean, FIReportBean beanFI, String qg)
			throws Exception {
		// set repari boolean
		boolean ftr = false;
		if (!beanFI.getQg().equals("A")) {
			ftr = true;
		}
		// Update stk table
		String sql = "update stock.stk set avl=2,repair=" + ftr + ",bcdate=now(),tc="+bean.getTireCode()+",qg='" + qg + "' where sn=?; ";
	 
		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			stmt.setInt(1, bean.getSn());
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "Not updated stock.stk table-:" + bean.getSn());
				return false;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "stkTbl-E01-stkTbl.updateStkTbl() E1 :-" + e);
			return false;
		} finally {
			if (keys != null)
				keys.close();
		}
	}

	public boolean CrossChleckUpdate(Connection conn, int sn) throws SQLException {
		String sql = "select avl from stock.stk where sn = " + sn + "; ";
		ResultSet rs = null;
		boolean blnavl = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			if (rs.next()) {
				int avl = rs.getInt("avl");
				if (avl == 2)
					blnavl = true;
			} else {
				JOptionPane.showMessageDialog(null, "StkTbl.CrossChleckUpdate SN is not updated");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, " StkTbl.CrossChleckUpdate " + e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return blnavl;
	}

	public String getQG(Connection conn, int sn) throws SQLException {
		String qg = "";
		String sql = "select qg from stock.stk where sn = " + sn + "; ";
		ResultSet rs = null;
		boolean blnavl = false;
		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
			if (rs.next()) {
				qg = rs.getString("qg");

			} else {
				JOptionPane.showMessageDialog(null, "StkTbl.getQG SN is not AVL");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, " StkTbl.getQG " + e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return qg;
	}

	// Check Availbility
	public boolean serialNoAvailablityCheck(Connection conn, int sn) {

		boolean bln = false;

		try {

			Statement s = conn.createStatement();

			String selTable = "SELECT * FROM stock.stk where sn = " + sn + ";";
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				bln = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "StkTbl.serialNoAvailablityCheck  E3" + e);
		}

		return bln;

	}

	public boolean updateStkTblREBC(Connection conn, BasicDatafromTireCodeBean bean, FIReportBean beanFI, String qg)
			throws Exception {
		boolean aff = false;
		String sql = "update stock.stk set avl=2,pid = " + bean.getPid() + ",qg='" + qg
				+ "',tc = 0  where sn=?; ";
		
		ResultSet keys = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			stmt.setInt(1, bean.getSn());
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				aff = true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "stkTbl-E01-stkTbl.updateStkTblREBC() :-" + e);

		} finally {
			if (keys != null)
				keys.close();
		}
		return aff;
	}
}
