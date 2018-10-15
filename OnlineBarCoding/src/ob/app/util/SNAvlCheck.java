package ob.app.util;

import java.sql.Connection;

import javax.swing.JOptionPane;

import ob.app.tbl.BCPendingTbl;
import ob.app.tbl.FIReportTbl;
import ob.app.tbl.StkTbl;

public class SNAvlCheck {
	public boolean snAVlCheckinAllTbls(Connection conn,int sn) {

		FIReportTbl fiReportTbl = new FIReportTbl();
		BCPendingTbl bcPendingTbl = new BCPendingTbl();
		StkTbl stkTbl = new StkTbl();
		
	

		boolean blnFIReportAvl = fiReportTbl.serialNoAvailablityCheck(conn, sn);

		boolean blnStkTblAvl = stkTbl.serialNoAvailablityCheck(conn, sn);
		boolean blnBCPendingAvl = bcPendingTbl.serialNoAvailablityCheck(conn, sn);

		boolean snAvl = true;
		if (!blnFIReportAvl) {
			snAvl = false;
			 JOptionPane.showMessageDialog(null, "SN is not available in FIReport");
		}
		if (!blnStkTblAvl) {
			snAvl = false;
			 JOptionPane.showMessageDialog(null, "SN is not available in Stock");
			// Database(stk Tbl)");
		}
		if (!blnBCPendingAvl) {
			snAvl = false;
			 JOptionPane.showMessageDialog(null, "SN is not available in bcpending Tbl");
		}
		return snAvl;

	}

	
}
